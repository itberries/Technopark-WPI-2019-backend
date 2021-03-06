package com.itberries.technopark.itberries.dao.impl;

import com.itberries.technopark.itberries.dao.IUserStateDAO;
import com.itberries.technopark.itberries.models.UserState;
import com.itberries.technopark.itberries.responses.CantUpdateStateException;
import com.itberries.technopark.itberries.responses.ThereIsNoSuchUserException;
import com.itberries.technopark.itberries.rowmapper.UserStateRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserStateDAOImpl implements IUserStateDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static RowMapper<UserState> userStateRowMapper = new UserStateRowMapper();

    public UserStateDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserState getUserState(Long userId) {
        final String sql = "SELECT us.id, us.user_id, us.section_id, us.subsection_id, us.step_id, us.has_passed_application \n" +
                "FROM users_states AS us \n" +
                "WHERE us.user_id = :userId";
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("userId", userId);
            return jdbcTemplate.queryForObject(sql, namedParameters, userStateRowMapper);
        } catch (DataAccessException ex) {
            throw new ThereIsNoSuchUserException();
        }
    }

    @Override
    public boolean setUserState(Long userId, UserState userState) {
        final String sql = "UPDATE users_states SET section_id = :sectId, subsection_id = :subsectionId, step_id = :stepId, has_passed_application = :hasPassed" +
                " WHERE user_id = :userId";
        try {
            Integer affectedRows = jdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("sectId", userState.getSectionId())
                    .addValue("subsectionId", userState.getSubsectionId())
                    .addValue("stepId", userState.getStepId())
                    .addValue("hasPassed", userState.getHasPassedApplication())
                    .addValue("userId", userId));
            return (affectedRows == 1);
        } catch (DataAccessException ex) {
            throw new CantUpdateStateException();
        }
    }

    @Override
    public boolean setCurrentUserSection(Long userId, Integer destSectionId) {
        final String sql = "UPDATE users_states SET section_id = :sectId WHERE user_id = :userId";
        try {
            int affectedRows = jdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("sectId", destSectionId)
                    .addValue("userId", userId));
            return (affectedRows == 1);
        } catch (DataAccessException ex) {
            throw new CantUpdateStateException();
        }
    }

    @Override
    public void initUserState(Long userId) {
        final String sql = "INSERT into users_states (user_id, step_id, section_id, subsection_id, has_passed_application)\n" +
                "VALUES (:userId, 1, 1, 1, false)";
        jdbcTemplate.update(sql, new MapSqlParameterSource().addValue("userId", userId));
    }

    @Override
    public UserState getCurrentStateByStepId(Long stepId) {
        final String sql = "SELECT NULL AS id,\n" +
                "       NULL AS user_id,\n" +
                "       ss.id_section AS section_id,\n" +
                "       st.subsection_id AS subsection_id,\n" +
                "       st.id AS step_id,\n" +
                "       0 AS has_passed_application\n" +
                "FROM steps st\n" +
                "JOIN subsections ss\n" +
                "ON st.subsection_id = ss.id\n" +
                "WHERE st.id = :stepId";
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("stepId", stepId);
            return jdbcTemplate.queryForObject(sql, namedParameters, userStateRowMapper);
        } catch (DataAccessException ex) {
            return null;
        }
    }
}
