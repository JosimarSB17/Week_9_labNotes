package org.example.movies;

import java.util.List;

import static input.InputUtils.*;

public class MovieList {
    private static final String DB_PATH = "jdbc.sqlite:movie_watchlist.sqlite";
    private static DataBase database;

    public static void main(String[] args) {
        database = new DataBase(DB_PATH);
        addNewMovie();
        checkIfWatchedAndRate();
        displayAllMovies();

    }

    public static void addNewMovie() {
        do {
            String movieName = stringInput("Enter the movie name: ");
            boolean movieWatched = yesNoInput("Have you watched the movie?: ");
            int movieStars = 0;
            if (movieWatched) {
                movieStars = positiveIntInput("Rate the movie in stars out of 5?");

            }
            Movie movie = new Movie(movieName, movieStars, movieWatched);
            database.addNewMovie(movie);

        }while (yesNoInput("Add a new movie?"));
    }

    public static void displayAllMovies() {
        List<Movie> movies = database.getAllMovies();
        if (movies.isEmpty()) {
            System.out.println("There are no movies");
        }else{
            for (Movie movie : movies) {
                System.out.println(movie);
            }
        }
    }

    public static void checkIfWatchedAndRate() {
        List<Movie> unwatchedMovies = database.getAllMoviesByWatched(false);

        for (Movie movie : unwatchedMovies) {
            boolean has = yesNoInput("Have you watched " + movie.getName() + "?");
            if (has) {
                int stars = positiveIntInput("Rate the movie in stars out of 5?");
                movie.setWatched(true);
                movie.setStars(stars);
                database.updateMovie(movie);
            }
        }
    }

}
