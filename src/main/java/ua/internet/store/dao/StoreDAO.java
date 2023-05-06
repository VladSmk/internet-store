package ua.internet.store.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ua.internet.store.mapper.ProductMapper;
import ua.internet.store.model.Product;
import ua.internet.store.model.UpperFilter;
import ua.internet.store.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class StoreDAO {

    public final JdbcTemplate jdbcTemplate;
    @Autowired
    public StoreDAO(JdbcTemplate jdbcTemplate, Environment environment){
        this.jdbcTemplate = jdbcTemplate;
        this.environment = environment;
    }

    public final Environment environment;

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
        return jdbcTemplate.query("SELECT * FROM internetshop.product\n" +
                "INNER JOIN countries ON product.country_id=countries.country_id \n " +
                "INNER JOIN cities ON product.city_id=cities.city_id \n " +
                "INNER JOIN users ON product.author_id=users.id \n " +
                "INNER JOIN colors ON product.color_id=colors.color_id \n " +
                "INNER JOIN firms ON product.firm_id=firms.firm_id \n " +
                "INNER JOIN `types` ON product.type_id=`types`.type_id;", new ResultSetExtractor<ArrayList<Product>>() {
            @Override
            public ArrayList<Product> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                ArrayList<Product> list = new ArrayList<Product>();
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
            }
        });
    } //+
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
    } //+
    public User searchUserInDbById(int userId){
        return jdbcTemplate.query("SELECT * FROM internetshop.users WHERE id=?;", new Object[]{userId}, new ResultSetExtractor<User>() {
            @Override
            public User extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                resultSet.next();
                User wantedUser = new User();
                wantedUser.setAccountId(resultSet.getInt("id"));
                wantedUser.setAccountName(resultSet.getString("username"));
                wantedUser.setAccountPassword(resultSet.getString("password"));
                wantedUser.setAccountAge(resultSet.getInt("age"));
                wantedUser.setAccountBio(resultSet.getString("bio"));
                wantedUser.setAccountPhoto(resultSet.getString("photo"));
                return wantedUser;
            }
        });
    } //+
    public Product searchProductById(int productId){
        return jdbcTemplate.query("SELECT * FROM internetshop.product\n" +
                "INNER JOIN countries ON product.country_id=countries.country_id\n" +
                "INNER JOIN cities ON product.city_id=cities.city_id \n" +
                "INNER JOIN users ON product.author_id=users.id\n" +
                "INNER JOIN colors ON product.color_id=colors.color_id\n" +
                "INNER JOIN firms ON product.firm_id=firms.firm_id\n" +
                "INNER JOIN `types` ON product.type_id=`types`.type_id\n" +
                "WHERE product.id=?;", new Object[]{productId}, new ResultSetExtractor<Product>() {
            @Override
            public Product extractData(ResultSet resultSet) throws SQLException, DataAccessException {
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
            }
        });
    }
    public boolean checkProductInBasket(int userId, int itemId){
        return jdbcTemplate.query("SELECT * FROM internetshop.basket WHERE user_id=? and product_id=?;", new Object[]{userId, itemId}, new ResultSetExtractor<Boolean>() {
            @Override
            public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                return resultSet.next();
            }
        });
    } //+
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
    private static int returnsMaxIdFromTable(String tableName, final String stringIdName){
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
//        return jdbcTemplate.query("SELECT MAX(" + stringIdName + ") FROM internetshop." + tableName + ";", new ResultSetExtractor<Integer>() {
//            @Override
//            public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
//                resultSet.next();
//                return (resultSet.getInt("MAX("+stringIdName+")")) + 1;
//            }
//        });
    }
    public List<Product> listProductByAuthorId(int authorId){
        return jdbcTemplate.query("SELECT * FROM internetshop.product\n" +
                "INNER JOIN countries ON product.country_id=countries.country_id\n" +
                "INNER JOIN cities ON product.city_id=cities.city_id \n" +
                "INNER JOIN users ON product.author_id=users.id\n" +
                "INNER JOIN colors ON product.color_id=colors.color_id\n" +
                "INNER JOIN firms ON product.firm_id=firms.firm_id\n" +
                "INNER JOIN `types` ON product.type_id=`types`.type_id\n" +
                "WHERE product.author_id=?;", new Object[]{authorId}, new ProductMapper()
        );
    } //+
    public void deleteMyItemFromDb(int productId){
        jdbcTemplate.update("DELETE FROM internetshop.basket WHERE product_id=?;", productId);
        jdbcTemplate.update("DELETE FROM internetshop.product WHERE id=?;", productId);
    } //+
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
    } //+
    public List<String> getAllNamesFromTable(String tableName, final String columnName){
        return jdbcTemplate.query("SELECT * FROM "+tableName+";",  new ResultSetExtractor<List<String>>() {
            @Override
            public List<String> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                List<String> list = new ArrayList<String>();
                list.add("-");
                while(resultSet.next())
                    list.add(resultSet.getString(columnName));
                return list;
            }
        });
    } //+
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
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internetshop.product\n" +
                            "INNER JOIN countries ON product.country_id=countries.country_id\n" +
                            "INNER JOIN cities ON product.city_id=cities.city_id\n" +
                            "INNER JOIN users ON product.author_id=users.id\n" +
                            "INNER JOIN colors ON product.color_id=colors.color_id\n" +
                            "INNER JOIN firms ON product.firm_id=firms.firm_id\n" +
                            "INNER JOIN `types` ON product.type_id=`types`.type_id\n" +
                            "WHERE product.author_id="+author_id+"   and product.type_id="+type_id+" and \n" +
                            "      product.country_id="+country_id+" and product.city_id="+city_id+" and \n" +
                            "      product.firm_id="+firm_id+"       and product.color_id="+color_id+" and price>=? and price<=?;"
            );
            preparedStatement.setDouble(1, upperFilter.getMinPrice());
            preparedStatement.setDouble(2, upperFilter.getMaxPrice());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
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
                productArrayList.add(product);
            }
            return productArrayList;
        } catch (SQLException e) {
            System.out.println("Error in getProductsAfterFilter(StoreDAO)");
        }
        return null;
    } //
    public ArrayList<Product> listProductByPartOfAuthorName(String partOfName){
        try {
            ArrayList<Product> productArrayList = new ArrayList<Product>();
            int quantity = 0;
            String nameString;
            boolean b;
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
                    b = true;
                    quantity=0;
                    for(int i=0; i<partOfName.length(); i++){
                        if (partOfName.toCharArray()[i]==nameString.toCharArray()[i] && b)
                            quantity++;
                        else
                            b = false;
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

}
