package ua.internet.store.mapper;

import org.springframework.jdbc.core.RowMapper;
import ua.internet.store.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getInt("age"),
                resultSet.getString("photo"),
                resultSet.getString("bio")
        );
    }
}
