package ua.internet.store.dao;

import org.springframework.stereotype.Component;
import ua.internet.store.model.Product;

import java.sql.*;
import java.util.ArrayList;

@Component
public class BasketDAO {

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

    public ArrayList<Product> searchAllUserItemInBasket(int userid){
        try {
            ArrayList<Product> productArrayList = new ArrayList<Product>();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM product INNER JOIN basket ON product.id=basket.product_id and basket.user_id=?\n" +
                            "INNER JOIN countries ON product.country_id=countries.country_id\n" +
                            "INNER JOIN cities ON product.city_id=cities.city_id \n" +
                            "INNER JOIN users ON product.author_id=users.id\n" +
                            "INNER JOIN colors ON product.color_id=colors.color_id\n" +
                            "INNER JOIN firms ON product.firm_id=firms.firm_id\n" +
                            "INNER JOIN `types` ON product.type_id=`types`.type_id;"
            );
            preparedStatement.setInt(1, userid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getDouble("price"));
                product.setCountry_id(resultSet.getString("country"));
                product.setCity_id(resultSet.getString("city"));
                product.setPhoto(resultSet.getString("photo"));
                product.setAuthor_id(resultSet.getString("username"));
                product.setColor_id(resultSet.getString("color"));
                product.setFirm_id(resultSet.getString("firm"));
                product.setType_id(resultSet.getString("type"));
                productArrayList.add(product);
            }
            return productArrayList;
        } catch (SQLException e) {
            System.out.println("Error in searchAllUserItemInBasket(BasketDAO)");
            throw new RuntimeException(e);
        }
    }


}
