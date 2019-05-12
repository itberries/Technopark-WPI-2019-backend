package com.itberries.technopark.itberries.rowmapper;

import com.itberries.technopark.itberries.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setScore(resultSet.getLong("score"));
        try {
            user.setRank(resultSet.getLong("rank"));
        } catch (Exception ex) {
            user.setRank(Long.parseLong("-1"));
        }
        return user;
    }
}
