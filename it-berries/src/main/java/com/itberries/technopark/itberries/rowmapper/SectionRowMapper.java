package com.itberries.technopark.itberries.rowmapper;

import com.itberries.technopark.itberries.models.Section;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SectionRowMapper implements RowMapper<Section> {
    @Override
    public Section mapRow(ResultSet resultSet, int i) throws SQLException {
        Section section = new Section();

        section.setId(resultSet.getLong("id"));
        section.setName(resultSet.getString("name"));
        section.setParentId(resultSet.getInt("parent_id"));
        section.setChildId(resultSet.getInt("child_id"));
        section.setSubsections(null);

        return section;
    }
}
