package ua.internet.store.dao;

import org.springframework.stereotype.Component;
import ua.internet.store.model.User;

import java.sql.*;

@Component
public class UserDAO {
    private static Connection connection = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error in MySQL Driver");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/internetshop",
                    "root",
                    "p@ssw0rd"
            );
        } catch (SQLException e) {
            System.out.println("Error in connection or statement");
            e.printStackTrace();
        }
    }

    public void signUpNewAccountInDb(User user){
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO internetshop.users(`id`, `username`, `password`) VALUES (?, ?, ?);"
            );
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT MAX(id) FROM internetshop.users");

            resultSet.next();
            int maxId = resultSet.getInt("MAX(id)") + 1;

            preparedStatement.setInt(1, maxId);
            preparedStatement.setString(2, user.getAccountName());
            preparedStatement.setString(3, user.getAccountPassword());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error in saveAccountInDb");
            throw new RuntimeException(e);
        }
    }

    public User searchAccountByName(String accountName){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.users WHERE username=?;"
            );
            preparedStatement.setString(1, accountName);
            resultSet = preparedStatement.executeQuery();
            user.setAccountId(resultSet.getInt("id"));
            user.setAccountName(resultSet.getString("username"));
            user.setAccountPassword(resultSet.getString("password"));
            return user;
        } catch (SQLException e) {
            System.out.println("Error in searchAccountByName");
            throw new RuntimeException(e);
        }
    }






}
