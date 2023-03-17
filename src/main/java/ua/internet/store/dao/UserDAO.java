package ua.internet.store.dao;
import org.springframework.stereotype.Component;
import ua.internet.store.model.User;
import ua.internet.store.model.UserPassword;
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

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO internetshop.users(`id`, `username`, `password`, `bio`, `age`, `photo`) VALUES (?, ?, ?, ?, ?, ?);"
            );
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(id) FROM internetshop.users;");

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
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
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
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM internetshop.users WHERE username=?;"
            );
            preparedStatement.setString(1, user.getAccountName());
            ResultSet resultSet = preparedStatement.executeQuery();
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
    public User searchAccountById(int userId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.users WHERE id=?;"
            );
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getInt("age"),
                        resultSet.getString("photo"),
                        resultSet.getString("bio")
                );
            }
            else
                return null;
        } catch (SQLException e) {
            System.out.println("Error in searchAccountById(UserDAO)");
            throw new RuntimeException(e);
        }
    }
    public void deleteAccountFromDb(int userId){
        try {
            PreparedStatement preparedStatement2 = connection.prepareStatement(
                    "DELETE FROM internetshop.basket WHERE user_id=?;"
            );
            preparedStatement2.setInt(1, userId);
            preparedStatement2.executeUpdate();


            PreparedStatement preparedStatement1 = connection.prepareStatement(
                    "DELETE FROM internetshop.product WHERE author_id=?;"
            );
            preparedStatement1.setInt(1, userId);
            preparedStatement1.executeUpdate();


            PreparedStatement preparedStatement3 = connection.prepareStatement(
                    "DELETE FROM internetshop.users WHERE id=?;"
            );
            preparedStatement3.setInt(1, userId);
            preparedStatement3.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in deleteAccountFromDb(UserDAO)");
            throw new RuntimeException(e);
        }
    }
    public String searchPasswordById(int userId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT password FROM internetshop.users WHERE id=?;"
            );
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getString("password");
            else
                return null;
        } catch (SQLException e) {
            System.out.println("Error in searchPasswordById(userDAO)");
            throw new RuntimeException(e);
        }
    }
    public void setNewPassword(UserPassword userPassword){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE internetshop.users SET password=? WHERE id=?;"
            );
            preparedStatement.setString(1, userPassword.getNewPassword1());
            preparedStatement.setInt(2, userPassword.getUserId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in changePassword(UserDAO)");
            throw new RuntimeException(e);
        }
    }
    public boolean passwordVerification(UserPassword userPassword){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.users WHERE id=? and `password`=?;"
            );
            preparedStatement.setInt(1, userPassword.getUserId());
            preparedStatement.setString(2, userPassword.getOldPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error in passwordVerification(UserDAO)");
            throw new RuntimeException(e);
        }
    }
    public void updateUserInDb(User user){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE internetshop.users SET `username`=?, `bio`=?, `age`=?, `photo`=? WHERE id=?;"
            );
            preparedStatement.setString(1, user.getAccountName());
            preparedStatement.setString(2, user.getAccountBio());
            preparedStatement.setInt(3, user.getAccountAge());
            preparedStatement.setString(4, user.getAccountPhoto());
            preparedStatement.setInt(5, user.getAccountId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in updateUserInDb(UserDAo)");
            throw new RuntimeException(e);
        }

    }

}
