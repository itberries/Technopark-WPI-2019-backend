package com.itberries.technopark.itberries.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import com.itberries.technopark.itberries.models.Event;


import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet resultSet, int i) throws SQLException {
        Event event = new Event();
        event.setId(resultSet.getLong("id"));
        event.setCity(resultSet.getString("city"));
        event.setDate(resultSet.getString("date"));
        event.setDescription(resultSet.getString("description"));
        event.setDetailsUrl(resultSet.getString("details_url"));
        event.setImageUrl(resultSet.getString("image_url"));
        event.setPreviewUrl(resultSet.getString("preview_url"));
        event.setTitle(resultSet.getString("title"));
        return event;
    }
}
