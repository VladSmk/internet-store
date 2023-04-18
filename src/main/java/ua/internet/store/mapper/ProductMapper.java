package ua.internet.store.mapper;

import org.springframework.jdbc.core.RowMapper;
import ua.internet.store.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Product(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDouble("price"),
                resultSet.getString("country_id"),
                resultSet.getString("city_id"),
                resultSet.getString("photo"),
                resultSet.getString("author_id"),
                resultSet.getString("color_id"),
                resultSet.getString("firm_id"),
                resultSet.getString("type_id")
                );
    }
}
