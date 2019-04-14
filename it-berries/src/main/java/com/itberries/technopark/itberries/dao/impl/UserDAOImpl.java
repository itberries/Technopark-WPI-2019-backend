package com.itberries.technopark.itberries.dao.impl;

import com.itberries.technopark.itberries.dao.IUserDAO;
import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.responses.DataBaseUserException;
import com.itberries.technopark.itberries.responses.DublicateUserException;
import com.itberries.technopark.itberries.responses.ThereIsNoSuchUserException;
import com.itberries.technopark.itberries.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserDAOImpl implements IUserDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static RowMapper<User> userRowMapper = new UserRowMapper();

    @Autowired
    public UserDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(Long id) {
        try {
            final String sql = "select * from users where id=:id";
            SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
            return jdbcTemplate.queryForObject(sql, namedParameters, userRowMapper);
        } catch (DataAccessException ex) {
            throw new ThereIsNoSuchUserException();
        }


    }

    @Override
    public Integer setUser(Long id) {
        try {
            final String sql = "insert into users (id) values (:id)";
            return jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
        } catch (DuplicateKeyException ex) {
            throw new DublicateUserException();
        } catch (DataAccessException ex) {
            throw new DataBaseUserException();
        }
    }

    @Override
    public void updateScore(Integer value, Long userId) {
        final String sql = "update  users set score = score + :value where id =:userId";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("value", value)
                .addValue("userId", userId));
    }

    @Override
    public void setScore(Integer value, Long userId) {
        final String sql = "update  users set score = :value where id =:userId";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("value", value)
                .addValue("userId", userId));
    }
}
