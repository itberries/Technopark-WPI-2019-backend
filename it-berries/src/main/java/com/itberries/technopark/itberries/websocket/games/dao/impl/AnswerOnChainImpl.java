package com.itberries.technopark.itberries.websocket.games.dao.impl;


import com.itberries.technopark.itberries.websocket.games.dao.IAnswerOnChainDAO;
import com.itberries.technopark.itberries.websocket.games.dao.IAnswerOnMatchDAO;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AnswerOnChainImpl implements IAnswerOnChainDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AnswerOnChainImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String findAnswerByGameId(Long gameId) {
        final String sql = "SELECT answer FROM answers_on_chain WHERE game_id=:gameId";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("gameId", gameId), String.class);
    }
}
