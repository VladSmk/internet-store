package ua.internet.store.dao;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import ua.internet.store.model.Product;
import ua.internet.store.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

//    public ArrayList<Product>[] arrayOfThreeList(){
//        ArrayList<Product> arrayList1 = new ArrayList<Product>();
//        ArrayList<Product> arrayList2 = new ArrayList<Product>();
//        ArrayList<Product> arrayList3 = new ArrayList<Product>();
//
//        Product[] products = findAllProduct().toArray(new Product[findAllProduct().size()]);
//        for(int i=0; i<products.length; i++){
//            if(i%3==0) {
//                arrayList1.add(products[i]);
//            } else if (i%3==1) {
//                arrayList2.add(products[i]);
//            } else {
//                arrayList3.add(products[i]);
//            }
//        }
//        ArrayList<Product>[] arrayLists = new ArrayList[]{arrayList1, arrayList2, arrayList3};
//        return arrayLists;
//    }
    public ArrayList<Product>[] arrayOfThreeList(){
        ArrayList<Product>[] arrayLists = new ArrayList[]{new ArrayList(), new ArrayList(), new ArrayList()};
        Product[] products = findAllProduct().toArray(new Product[findAllProduct().size()]);
        for(int i=0; i<products.length; i++){
            if(i%3==0) {
                arrayLists[0].add(products[i]);
            } else if (i%3==1) {
                arrayLists[1].add(products[i]);
            } else {
                arrayLists[2].add(products[i]);
            }
        }
        return arrayLists;
    }

    public User searchUserInDbById(int userId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.users WHERE id=?;"
            );
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            User wantedUser = new User();
            wantedUser.setAccountId(resultSet.getInt("id"));
            wantedUser.setAccountName(resultSet.getString("username"));
            wantedUser.setAccountPassword(resultSet.getString("password"));
            return wantedUser;
        } catch (SQLException e) {
            System.out.println("Error in searchUserInDbById(StoreDAO)");
            throw new RuntimeException(e);
        }
    }

    public Product createProductById(int productId){
        try {
            PreparedStatement preparedstatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.product WHERE id=?"
            );
            preparedstatement.setInt(1, productId);
            ResultSet resultSet = preparedstatement.executeQuery();
            resultSet.next();
            Product product = new Product(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("photo"),
                    resultSet.getInt("author")
            );
            return product;
        } catch (SQLException e) {
            System.out.println("Error in createProductById(StoreDAO)");
            throw new RuntimeException(e);
        }
    }

}
