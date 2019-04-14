package com.itberries.technopark.itberries.rowmapper;

import com.itberries.technopark.itberries.models.Subsection;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubsectionRowMapper implements RowMapper<Subsection> {
    @Override
    public Subsection mapRow(ResultSet resultSet, int i) throws SQLException {
        Subsection subsection = new Subsection();

        subsection.setId(resultSet.getLong("id"));
        subsection.setId_section(resultSet.getLong("id_section"));
        subsection.setName(resultSet.getString("name"));
        subsection.setParentId(resultSet.getInt("parent_id"));
        subsection.setChildId(resultSet.getInt("child_id"));

        return subsection;
    }
}
