package org.example.movies;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private String dataBasePath;

    DataBase(String dataBasePath) {

        this.dataBasePath = dataBasePath;

        try{
        Connection connection = DriverManager.getConnection(dataBasePath);
        Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS " +
                    "movies (id integer primary key," +
                    " name TEXT unique CHECK(length(name) >= 1)," +
                    " stars INTEGER CHECK(stars >= 0 AND stars <= 5)," +
                    " watched BOOLEAN)");

        } catch (SQLException e) {
            System.out.println("Error creating movie table" );
        }

    }

    public void addNewMovie(Movie movie ) {

        String insertSQL = "Insert INTO movies (name, star, watched) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(dataBasePath);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)){

             preparedStatement.setString(1, movie.getName());
             preparedStatement.setInt(2, movie.getStars());
             preparedStatement.setBoolean(3, movie.isWatched());

             preparedStatement.execute();

        }catch (SQLException e) {
            System.out.println("Error adding movie" );
        }
    }

    public List<Movie> getAllMovies() {
        try (Connection connection = DriverManager.getConnection(dataBasePath);
             Statement statement = connection.createStatement();
        ){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM movies ORDER BY name ");

            List<Movie> movies = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int stars = resultSet.getInt("stars");
                boolean watched = resultSet.getBoolean("watched");
                movies.add(new Movie(id, name, stars, watched));
                Movie movie = new Movie(id, name, stars, watched);
                movies.add(movie);
            }
            return movies;

        }
        catch (SQLException e) {
            System.out.println("Error getting all movies");
            return null;
        }
    }

    public List<Movie> getAllMoviesByWatched(boolean watchedStatus) {
        try(Connection connection = DriverManager.getConnection(dataBasePath);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM movies WHERE watched = ?");){
            preparedStatement.setBoolean(1, watchedStatus);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Movie> movies = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int stars = resultSet.getInt("stars");
                boolean watched = resultSet.getBoolean("watched");
                Movie movie = new Movie(id, name, stars, watched);
                movies.add(movie);
            }
            return movies;

        }catch (SQLException e){
            System.out.println("Error getting all movies");
            return null;
        }
    }

    public void updateMovie(Movie movie) {

        String sql = "UPDATE movies SET stars = ?, watched = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(dataBasePath);
        PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setInt(1, movie.getStars());
            preparedStatement.setBoolean(2, movie.isWatched());
            preparedStatement.setInt(3, movie.getId());

            preparedStatement.execute();

        }catch (SQLException e) {
            System.out.println("Error updating movie because " + e);
        }
    }
    public void deleteMovie(Movie movie) {
        try(Connection connection = DriverManager.getConnection(dataBasePath);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM watchlist WHERE id = ?"))
        {
            preparedStatement.setInt(1, movie.getId());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println("There was a problem deleting the movie because " + e);
        }
    }

    public List<Movie> searchMovie(String movieName) {
        String sql = "SELECT * FROM movies WHERE UPPER(name) LIKE UPPER(?)";

        try(Connection connection = DriverManager.getConnection(dataBasePath);
        PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, "%" + movieName + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Movie> movies = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int stars = resultSet.getInt("stars");
                boolean watched = resultSet.getBoolean("watched");
                Movie movie = new Movie(id, name, stars, watched);
                movies.add(movie);
            }
            return movies;

        }catch (SQLException e){
            System.out.println("There was a problem searching the movie because " + e);
            return null;
        }
    }

}
