package com.itberries.technopark.itberries.websocket.games.dao.impl;

import com.itberries.technopark.itberries.websocket.games.dao.IAnswerOnQuestionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class AnswerOnQuestionImpl implements IAnswerOnQuestionDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public AnswerOnQuestionImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String findAnswerByGameId(Long gameId) {
        final String sql = "SELECT answer FROM answers_on_question WHERE game_id=:gameId";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("gameId", gameId), String.class);
    }
}
