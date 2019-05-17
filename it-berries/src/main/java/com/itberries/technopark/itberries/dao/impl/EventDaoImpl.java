package com.itberries.technopark.itberries.dao.impl;

import com.itberries.technopark.itberries.dao.IEventDao;
import com.itberries.technopark.itberries.models.Event;
import com.itberries.technopark.itberries.rowmapper.EventRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EventDaoImpl implements IEventDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public EventDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> getAllEvents() {
        final String sql = "select title, date, city, preview_url, image_url, description, details_url from events";
        return jdbcTemplate.query(sql, new EventRowMapper());
    }
}
