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
                    "INSERT INTO internetshop.users(`id`, `username`, `password`, `bio`, `age`, `photo`) VALUES (?, ?, ?, ?, ?, ?);"
            );
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT MAX(id) FROM internetshop.users");

            resultSet.next();
            int maxId = resultSet.getInt("MAX(id)") + 1;

            preparedStatement.setInt(1, maxId);
            preparedStatement.setString(2, user.getAccountName());
            preparedStatement.setString(3, user.getAccountPassword());
            preparedStatement.setString(4, user.getAccountBio());
            preparedStatement.setInt(5, user.getAccountAge());
            preparedStatement.setString(6, user.getAccountPhoto());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error in signUpNewAccountInDb(UserDAO)");
            throw new RuntimeException(e);
        }
    }
    public boolean signInAccountWithDb(User user) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.users WHERE username=? and password=?;"
            );
            preparedStatement.setString(1, user.getAccountName());
            preparedStatement.setString(2, user.getAccountPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return true;
            else
                return false;
        } catch (SQLException e) {
            System.out.println("Error in signInAccountWithDb(UserDAO)");
            throw new RuntimeException(e);
        }
    }

    public int searchIdByName(User user){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT id FROM internetshop.users WHERE username=?;"
            );
            preparedStatement.setString(1, user.getAccountName());
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            System.out.println("Error in searchIdByName(UserDAO)");
        }
        return 0;
    }

    public void addProductToBasket(int userId, int itemId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO internetshop.basket(user_id, product_id) VALUES (?, ?);"
            );
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, itemId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in addProductToBasket(UserDAO)");
            throw new RuntimeException(e);
        }
    }

    public void deleteProductFromBasket(int userId, int itemId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM internetshop.basket WHERE user_id=? and product_id=?;"
            );
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, itemId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in deleteProductFromBasket(UserDAO)");
            throw new RuntimeException(e);
        }
    }


}
