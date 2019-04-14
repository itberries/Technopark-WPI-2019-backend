package com.itberries.technopark.itberries.rowmapper;

import com.itberries.technopark.itberries.models.UserState;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserStateRowMapper implements RowMapper<UserState> {
    @Override
    public UserState mapRow(ResultSet resultSet, int i) throws SQLException {
        UserState us = new UserState();

        us.setId(resultSet.getLong("id"));
        us.setUserId(resultSet.getLong("user_id"));
        us.setSectionId(resultSet.getLong("section_id"));
        us.setSubsectionId(resultSet.getLong("subsection_id"));
        us.setStepId(resultSet.getLong("step_id"));

        return us;
    }
}
