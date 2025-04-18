package org.example.movies;

import java.sql.*;

public class HelloDataBases {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:hello.sqlite";
        Connection connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();

       // String createTable = "CREATE TABLE cats (name TEXT, age INTEGER)";
      //  statement.execute(createTable);

       // String insertData = "INSERT INTO cats VALUES ('Mary', 10)";
       // statement.execute(insertData);

      //  String insertData = "INSERT INTO cats VALUES ('Hello Kitty', 20)";
       // statement.execute(insertData);

        String selectData = "SELECT * FROM cats";
        ResultSet resultSet = statement.executeQuery(selectData);

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            System.out.println("Name: " + name + ", Age: " + age);
        }
    }
}
