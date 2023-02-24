package ua.internet.store.dao;

import org.springframework.stereotype.Component;
import ua.internet.store.model.Product;
import java.sql.*;
import java.util.ArrayList;

@Component
public class StoreDAO {

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

    public ArrayList<Product> findAllProduct(){
        ArrayList<Product> list = new ArrayList<Product>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    "SELECT * FROM internetshop.product;"
            );
            while (resultSet.next()){
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("photo"),
                        resultSet.getInt("author")
                );
                list.add(product);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("Error in findAllProduct(StoreDAO)");
            throw new RuntimeException(e);
        }
    }

}
