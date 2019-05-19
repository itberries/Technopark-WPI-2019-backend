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
import java.util.List;

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

    @Override
    public List<User> getLeaderboardForUser(Long userId) {
        final String sql = "WITH result AS\n" +
                "(\n" +
                "        (SELECT u.id, u.score, DENSE_RANK()  OVER (ORDER BY u.score DESC) AS rank\n" +
                "         FROM users u\n" +
                "         WHERE u.score > 0\n" +
                "         LIMIT 10)\n" +
                "        UNION\n" +
                "        (\n" +
                "          WITH summary AS (\n" +
                "            SELECT u.id, u.score, DENSE_RANK() OVER (ORDER BY u.score DESC) AS rank\n" +
                "            FROM users u)\n" +
                "            SELECT s.*\n" +
                "            FROM summary s\n" +
                "            WHERE s.id = :userId\n" +
                "        )\n" +
                ") SELECT r.*\n" +
                "FROM result r\n" +
                "ORDER BY r.rank\n";
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("userId", userId);
            return jdbcTemplate.query(sql,namedParameters,userRowMapper);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<User> getLeaderboardForUserAmongFriends(Long userId, List<Long> friendsIds) {
        final String sql = "WITH result AS\n" +
                "(\n" +
                "        (SELECT u.id, u.score, DENSE_RANK()  OVER (ORDER BY u.score DESC) AS rank\n" +
                "         FROM users u\n" +
                "         WHERE u.score > 0 AND u.id IN (:userIds)\n" +
                "         LIMIT 10)\n" +
                "        UNION\n" +
                "        (\n" +
                "          WITH summary AS (\n" +
                "            SELECT u.id, u.score, DENSE_RANK() OVER (ORDER BY u.score DESC) AS rank\n" +
                "            FROM users u)\n" +
                "            SELECT s.*\n" +
                "            FROM summary s\n" +
                "            WHERE s.id = :userId\n" +
                "        )\n" +
                ") SELECT r.*\n" +
                "FROM result r\n" +
                "ORDER BY r.rank\n";
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("userId", userId );
            namedParameters.addValue("userIds", friendsIds );
            return jdbcTemplate.query(sql,namedParameters,userRowMapper);
        } catch (DataAccessException ex) {
            return null;
        }
    }
}
