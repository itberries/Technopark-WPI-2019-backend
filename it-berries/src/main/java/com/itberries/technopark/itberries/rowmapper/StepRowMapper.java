package com.itberries.technopark.itberries.rowmapper;

import com.itberries.technopark.itberries.models.Step;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StepRowMapper implements RowMapper<Step> {
    @Override
    public Step mapRow(ResultSet resultSet, int i) throws SQLException {
        Step step = new Step();
        step.setChildId(resultSet.getInt("child_id"));
        step.setParentId(resultSet.getInt("parent_id"));
        step.setName(resultSet.getString("name"));
        step.setType(resultSet.getString("type"));
        step.setId(resultSet.getInt("id"));
        return step;
    }
}
