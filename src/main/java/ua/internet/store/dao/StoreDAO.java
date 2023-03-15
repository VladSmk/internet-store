package ua.internet.store.dao;

import com.mysql.cj.jdbc.CallableStatementWrapper;
import org.springframework.stereotype.Component;
import ua.internet.store.model.LeftFilter;
import ua.internet.store.model.Product;
import ua.internet.store.model.UpperFilter;
import ua.internet.store.model.User;

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
                        "SELECT * FROM internetshop.product\n" +
                            "INNER JOIN countries ON product.country_id=countries.country_id \n " +
                            "INNER JOIN cities ON product.city_id=cities.city_id \n " +
                            "INNER JOIN users ON product.author_id=users.id \n " +
                            "INNER JOIN colors ON product.color_id=colors.color_id \n " +
                            "INNER JOIN firms ON product.firm_id=firms.firm_id \n " +
                            "INNER JOIN `types` ON product.type_id=`types`.type_id;"
            );
            while (resultSet.next()){
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
                list.add(product);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("Error in findAllProduct(StoreDAO)");
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Product>[] arrayOfThreeList(ArrayList<Product> prodArrList){
        ArrayList<Product>[] arrayLists = new ArrayList[]{new ArrayList(), new ArrayList(), new ArrayList()};
        Product[] products = prodArrList.toArray(new Product[prodArrList.size()]);
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
            wantedUser.setAccountAge(resultSet.getInt("age"));
            wantedUser.setAccountBio(resultSet.getString("bio"));
            wantedUser.setAccountPhoto(resultSet.getString("photo"));
            return wantedUser;
        } catch (SQLException e) {
            System.out.println("Error in searchUserInDbById(StoreDAO)");
            throw new RuntimeException(e);
        }
    }
    public Product searchProductById(int productId){
        try {
            PreparedStatement preparedstatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.product\n" +
                            "INNER JOIN countries ON product.country_id=countries.country_id\n" +
                            "INNER JOIN cities ON product.city_id=cities.city_id \n" +
                            "INNER JOIN users ON product.author_id=users.id\n" +
                            "INNER JOIN colors ON product.color_id=colors.color_id\n" +
                            "INNER JOIN firms ON product.firm_id=firms.firm_id\n" +
                            "INNER JOIN `types` ON product.type_id=`types`.type_id\n" +
                            "WHERE product.id=?;"
            );
            preparedstatement.setInt(1, productId);
            ResultSet resultSet = preparedstatement.executeQuery();
            resultSet.next();
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
            return product;
        } catch (SQLException e) {
            System.out.println("Error in searchProductById(StoreDAO)");
            throw new RuntimeException(e);
        }
    }
    public boolean checkProductInBasket(int userId, int itemId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.basket WHERE user_id=? and product_id=?;"
            );
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, itemId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error in checkProductInBasket(StoreDAO)");
            throw new RuntimeException(e);
        }
    }
    public void saveProductInDb(Product product){


        try {
            PreparedStatement finalPreparedStatement = connection.prepareStatement(
                    "INSERT INTO internetshop.product" +
                            "         (`id`, `name`, `description`, `price`, `country_id`, `city_id`, `photo`, `author_id`, `color_id`, `firm_id`, `type_id`) " +
                            " VALUES (    ?,             ?,       ?,            ?,         ?,       ?,           ?,          ?,         ?,         ?,         ?);"
            );

            finalPreparedStatement.setInt(1, returnsMaxIdFromTable(
                    "product",
                    "id")
            );
            finalPreparedStatement.setString(2, product.getName());
            finalPreparedStatement.setString(3, product.getDescription());
            finalPreparedStatement.setDouble(4, product.getPrice());
            finalPreparedStatement.setString(7, product.getPhoto());
            finalPreparedStatement.setInt(8, Integer.parseInt(product.getAuthor_id()));

            finalPreparedStatement.setInt(
                    5,
                    returnsTheExistingItemId(
                            "countries",
                            "country_id",
                            "country",
                            product.getCountry_id()
                    )
            );

            finalPreparedStatement.setInt(
                    6,
                    returnsTheExistingItemId(
                            "cities",
                            "city_id",
                            "city",
                            product.getCity_id()
                    )
            );

            finalPreparedStatement.setInt(
                    9,
                    returnsTheExistingItemId(
                            "colors",
                            "color_id",
                            "color",
                            product.getColor_id()
                    )
            );

            finalPreparedStatement.setInt(
                    10,
                    returnsTheExistingItemId(
                            "firms",
                            "firm_id",
                            "firm",
                            product.getFirm_id()
                    )
            );

            finalPreparedStatement.setInt(
                    11,
                    returnsTheExistingItemId(
                            "types",
                            "type_id",
                            "type",
                            product.getType_id()
                    )
            );

            finalPreparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in saveProductInDb(StoreDAO)");
            throw new RuntimeException(e);
        }

    }
    public void editProductInDb(Product product){
//        System.out.println("id: "+product.getId());
//        System.out.println("name: "+product.getName());
//        System.out.println("desc: "+product.getDescription());
//        System.out.println("price: "+product.getPrice());
//        System.out.println("firm: "+product.getFirm_id());
//        System.out.println("color: "+product.getColor_id());
//        System.out.println("city: "+product.getCity_id());
//        System.out.println("country: "+product.getCountry_id());
//        System.out.println("photo: "+product.getPhoto());
//        System.out.println("author: "+product.getAuthor_id());
//        System.out.println("type: "+product.getType_id());
        try {
            PreparedStatement finalPreparedStatement = connection.prepareStatement(
                    "UPDATE internetshop.product SET `name`=?, `description`=?, `price`=?, `country_id`=?,\n" +
                            "                                `city_id`=?, `photo`=?, `author_id`=?, `color_id`=?,\n" +
                            "                                `firm_id`=?, `type_id`=? WHERE `id`=?;"
            );

            finalPreparedStatement.setInt(11, product.getId());
            finalPreparedStatement.setString(1, product.getName());
            finalPreparedStatement.setString(2, product.getDescription());
            finalPreparedStatement.setDouble(3, product.getPrice());
            finalPreparedStatement.setString(6, product.getPhoto());
            finalPreparedStatement.setInt(7, Integer.parseInt(product.getAuthor_id()));

            finalPreparedStatement.setInt(
                    4,
                    returnsTheExistingItemId(
                            "countries",
                            "country_id",
                            "country",
                            product.getCountry_id()
                    )
            );

            finalPreparedStatement.setInt(
                    5,
                    returnsTheExistingItemId(
                            "cities",
                            "city_id",
                            "city",
                            product.getCity_id()
                    )
            );

            finalPreparedStatement.setInt(
                    8,
                    returnsTheExistingItemId(
                            "colors",
                            "color_id",
                            "color",
                            product.getColor_id()
                    )
            );

            finalPreparedStatement.setInt(
                    9,
                    returnsTheExistingItemId(
                            "firms",
                            "firm_id",
                            "firm",
                            product.getFirm_id()
                    )
            );

            finalPreparedStatement.setInt(
                    10,
                    returnsTheExistingItemId(
                            "types",
                            "type_id",
                            "type",
                            product.getType_id()
                    )
            );
            finalPreparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error in editProductInDb(StoreDAO)");
        }


    }
    private static int returnsTheExistingItemId(String tableName, String stringIdName, String stringName, String value){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop."+tableName+";"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if (resultSet.getString(stringName).equals(value))
                    return resultSet.getInt(stringIdName);
            }
            int newId = returnsMaxIdFromTable(tableName, stringIdName);
            PreparedStatement preparedStatement2 = connection.prepareStatement(
                    "INSERT INTO internetshop."+tableName+"(`"+stringIdName+"`, `"+stringName+"`) " +
                            "VALUES ('"+newId+"', '"+value+"');"
            );
            preparedStatement2.executeUpdate();
            return newId;
        } catch (SQLException e) {
            System.out.println("Error in returnsTheExistingItemId(StoreDAO)");
            throw new RuntimeException(e);
        }
    }
    private static int returnsMaxIdFromTable(String tableName, String stringIdName){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT MAX("+stringIdName+") FROM internetshop."+tableName+";"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return (resultSet.getInt("MAX("+stringIdName+")")) + 1;
        } catch (SQLException e) {
            System.out.println("Error in returnsMaxIdFromTable(StoreDAO)");
        }
        return 0;
    }
    public ArrayList<Product> listProductByAuthorId(int authorId){
        try {
            ArrayList<Product> arrayList = new ArrayList<Product>();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.product WHERE author_id=?;"
            );
            preparedStatement.setInt(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getDouble("price"));
                arrayList.add(product);
            }
            return arrayList;
        } catch (SQLException e) {
            System.out.println("Error listProductByAuthorId(StoreDAO)");
        }
        return null;
    }
    public void deleteMyItemFromDb(int productId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM internetshop.product WHERE id=?;"
            );
            preparedStatement.setInt(1, productId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in deleteMyItemFromDb");
        }
    }
    public int getProductAuthorIdByProductId(int productId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.product WHERE product.id=?;"
            );
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("author_id");
            }
        } catch (SQLException e) {
            System.out.println("Error in getProductAuthorIdByProductName(StoreDAO)");
        }
        return 0;
    }
    public ArrayList<String> getAllNamesFromTable(String tableName, String columnName){
        try {
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add("-");
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop."+tableName+";"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                arrayList.add(resultSet.getString(columnName));
            }
            return arrayList;
        } catch (SQLException e) {
            System.out.println("Error in getAllNamesFromTable(StoreDAO)");
        }
        return null;
    }
    public ArrayList<Product> getProductsAfterFiltering(UpperFilter upperFilter){
        try {
            String author_id, type_id, country_id, city_id, color_id, firm_id;
            ArrayList<Product> productArrayList = new ArrayList<Product>();

            if(upperFilter.getAuthor().equals("-")) {
                author_id="product.author_id";
            } else {
                author_id = String.valueOf(returnsTheExistingItemId(
                                "users",
                                "id",
                                "username",
                                upperFilter.getAuthor()
                ));
            }
            if(upperFilter.getType().equals("-")){
                type_id = "product.type_id";
            } else {
                type_id = String.valueOf(returnsTheExistingItemId(
                                "types",
                                "type_id",
                                "type",
                                upperFilter.getType()
                ));
            }
            if(upperFilter.getCountry().equals("-")){
                country_id = "product.country_id";
            } else {
                country_id=String.valueOf(returnsTheExistingItemId(
                                "countries",
                                "country_id",
                                "country",
                                upperFilter.getCountry()
                ));
            }
            if(upperFilter.getCity().equals("-")){
                city_id = "product.city_id";
            } else {
                city_id=String.valueOf(returnsTheExistingItemId(
                                "cities",
                                "city_id",
                                "city",
                                upperFilter.getCity()
                ));
            }
            if(upperFilter.getColor().equals("-")){
                color_id = "product.color_id";
            } else {
                color_id=String.valueOf(returnsTheExistingItemId(
                        "colors",
                        "color_id",
                        "color",
                        upperFilter.getColor()
                ));
            }
            if(upperFilter.getFirm().equals("-")){
                firm_id = "product.firm_id";
            } else {
                firm_id=String.valueOf(returnsTheExistingItemId(
                        "firms",
                        "firm_id",
                        "firm",
                        upperFilter.getFirm()
                ));
            }
            System.out.println("YES");
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.product\n" +
                            "INNER JOIN countries ON product.country_id=countries.country_id\n" +
                            "INNER JOIN cities ON product.city_id=cities.city_id\n" +
                            "INNER JOIN users ON product.author_id=users.id\n" +
                            "INNER JOIN colors ON product.color_id=colors.color_id\n" +
                            "INNER JOIN firms ON product.firm_id=firms.firm_id\n" +
                            "INNER JOIN `types` ON product.type_id=`types`.type_id\n" +
                            "WHERE product.author_id="+author_id+"   and product.type_id="+type_id+" and \n" +
                            "      product.country_id="+country_id+" and product.city_id="+city_id+" " +
                            "      product.firm_id="+firm_id+"       and product.color_id="+color_id+" " +
                            "and price>=? and price<=?;"
            );
            System.out.println("YES2");
            preparedStatement.setDouble(1, upperFilter.getMinPrice());
            preparedStatement.setDouble(2, upperFilter.getMaxPrice());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.println("YES3");
                Product product = new Product();
                product.setId(resultSet.getInt("product.id"));
                product.setName(resultSet.getString("product.name"));
                product.setDescription(resultSet.getString("product.description"));
                product.setPrice(resultSet.getDouble("product.price"));
                product.setCountry_id(resultSet.getString("countries.country"));
                product.setCity_id(resultSet.getString("cities.city"));
                product.setPhoto(resultSet.getString("product.photo"));
                product.setAuthor_id(resultSet.getString("users.username"));
                product.setColor_id(resultSet.getString("colors.color"));
                product.setFirm_id(resultSet.getString("firms.firm"));
                product.setType_id(resultSet.getString("types.type"));
                System.out.println("YES4");
                productArrayList.add(product);
            }
            return productArrayList;
        } catch (SQLException e) {
            System.out.println("Error in getProductsAfterFilter(StoreDAO)");
        }
        return null;
    }

    public ArrayList<Product> listProductByPartOfAuthorName(String partOfName){
        try {
            ArrayList<Product> productArrayList = new ArrayList<Product>();
            int quantity = 0;
            String nameString;
            Product product;
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.product\n" +
                            "INNER JOIN countries ON product.country_id=countries.country_id\n" +
                            "INNER JOIN cities ON product.city_id=cities.city_id \n" +
                            "INNER JOIN users ON product.author_id=users.id\n" +
                            "INNER JOIN colors ON product.color_id=colors.color_id\n" +
                            "INNER JOIN firms ON product.firm_id=firms.firm_id\n" +
                            "INNER JOIN `types` ON product.type_id=`types`.type_id;"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if(partOfName.length()<=resultSet.getString("name").length()){
                    nameString = resultSet.getString("name");
                    for(int i=0; i<partOfName.length(); i++){
                        if (partOfName.toCharArray()[i]==nameString.toCharArray()[i])
                            quantity++;
                        else
                            break;
                        if (quantity==partOfName.length()) {
                            product = new Product();
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
                    }
                }
            }
            return productArrayList;
        } catch (SQLException e) {
            System.out.println("Error listProductByPartOfAuthorName(StoreDAO)");
        }
        return null;
    }



    public Double getMaxProductPrice(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT MAX(price) FROM internetshop.product;"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getDouble("MAX(price)");
        } catch (SQLException e) {
            System.out.println("Error in getMaxProductPrice");
        }
        return 0.0;
    }
    public Double getMinProductPrice(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT MIN(price) FROM internetshop.product;"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getDouble("MIN(price)");
        } catch (SQLException e) {
            System.out.println("Error in getMinProductPrice");
        }
        return 0.0;
    }
    public ArrayList<String> getListWithColor(){
        try {
            ArrayList<String> arrayList = new ArrayList<String>();
            PreparedStatement colorPreparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.colors;"
            );
            ResultSet resultSet = colorPreparedStatement.executeQuery();
            while(resultSet.next()){
                arrayList.add(resultSet.getString("color"));
            }
            return arrayList;
        } catch (SQLException e) {
            System.out.println("Error in getListWithColor(StoreDAO)");
        }
        return null;
    }
    public ArrayList<String> getListWithFirm(){
        try {
            ArrayList<String> arrayList = new ArrayList<String>();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.firms;"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                arrayList.add(resultSet.getString("firm"));
            }
            return arrayList;
        } catch (SQLException e) {
            System.out.println("Error in getListWithTypes(StoreDAO)");
        }
        return null;
    }
//    public ArrayList<String> filterToLeftFilter(String[] strings){
////        for(String str : strings){
////            try {
////                PreparedStatement preparedStatement = connection.prepareStatement(
////                        "SELECT * FROM internetshop.product\n" +
////                                "INNER JOIN countries ON product.country_id=countries.country_id\n" +
////                                "INNER JOIN cities ON product.city_id=cities.city_id\n" +
////                                "INNER JOIN users ON product.author_id=users.id\n" +
////                                "INNER JOIN colors ON product.color_id=colors.color_id\n" +
////                                "INNER JOIN firms ON product.firm_id=firms.firm_id\n" +
////                                "INNER JOIN `types` ON product.type_id=`types`.type_id\n" +
////                                "WHERE product.color_id="+str+";"
////                );
////            } catch (SQLException e) {
////                System.out.println("Error in ");
////            }
////
////
////        }
//
//        return null;
//    }

    public ArrayList<String> getRecurringItemFromTwoArray(String[] str1,String[] str2){
        ArrayList<String> arrayList = new ArrayList<String>();
        for(String s1 : str1)
            for (String s2 : str2)
                if (s1.equals(s2))
                    arrayList.add(s1);
        return arrayList;
    }
}
