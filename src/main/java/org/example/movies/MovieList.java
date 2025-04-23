package org.example.movies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static input.InputUtils.*;

public class MovieList {
    private static final String DB_PATH = "jdbc.sqlite:movie_watchlist.sqlite";
    private static DataBase database;

    public static void main(String[] args) {
        database = new DataBase(DB_PATH);
        addNewMovie();
        checkIfWatchedAndRate();
        deleteWatchedMovies();
        displayAllMovies();
        searchMovie();

    }

    public static int getRating(){
        int rating = positiveIntInput();
        while(rating <= 0 || rating >= 5){
            System.out.println("Please enter a valid rating between 0 and 5");
            rating = positiveIntInput();
        }
        return rating;
    }

    public static String getTitle(){
        String title = stringInput("Please enter a title: ");
        while(title.isEmpty()){
            System.out.println("Please enter a valid title");
            title = stringInput("Please enter a valid title: ");
        }
        return title;
    }

    public static void addNewMovie() {
        do {
            String movieName = getTitle();
            boolean movieWatched = yesNoInput("Have you watched the movie?: ");
            int movieStars = 0;
            if (movieWatched) {
                movieStars = getRating();

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

    public static void deleteWatchedMovies(){
        System.out.println("Here are all the watched movies");

        List<Movie> watchedMovies = database.getAllMoviesByWatched(true);
        for (Movie movie : watchedMovies) {
            boolean delete = yesNoInput("Delete " + movie.getName() + "?");
            if (delete) {
                database.deleteMovie(movie);
            }
        }


    }

    public static void searchMovie() {
        String movieName = stringInput("Enter the movie name: ");
        List<Movie> movies = database.searchMovie(movieName);

        if (movies.isEmpty()) {
            System.out.println("There are no movies that match");
        }else {
            for (Movie movie : movies) {
                System.out.println(movie);
            }
        }
    }

}
