package com.itberries.technopark.itberries.dao.impl;

import com.itberries.technopark.itberries.dao.IMiniGamesDAO;
import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.CardInteractive;
import com.itberries.technopark.itberries.rowmapper.CardInteractiveRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MiniGamesDAOImpl implements IMiniGamesDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public MiniGamesDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


//    @Override
//    public String getAnswerGameByGameId(Long gameId) {
//        final String sql = "SELECT answer FROM answers_on_match WHERE game_id=:gameId";
//        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("gameId", gameId), String.class);
//    }

    @Override
    public List<Long> getGamesIdByStepId(Long stepId) { //может вернуться НЕСКОЛЬКО игр, относящихся к одному шагу
        final String sql = "SELECT id FROM mini_games WHERE step_id=:stepId";
        return jdbcTemplate.queryForList(sql, new MapSqlParameterSource("stepId", stepId), Long.class);
    }

    @Override
    public String getGameTypeByGameId(Long gameId) {
        final String sql = "select type\n" +
                "from mini_games_types\n" +
                "       join mini_games m2 on mini_games_types.id = m2.type_id\n" +
                "       where m2.id = :gameId";
        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource("gameId", gameId), String.class);
    }

    @Override
    public List<CardInteractive> getCardsByStepId(Long stepId) {
        final String sql = "select mg.id, mg.note, mg.image, mg.parent_id, mg.child_id, c.id AS card_id, c.note AS card_note, " +
                "c.image AS card_image, c.parent_id AS card_parent_id, c.child_id AS card_child_id\n" +
                "from mini_games AS mg\n" +
                "join cards AS c on mg.step_id = c.step_id\n" +
                "where mg.step_id = :stepId";
        SqlParameterSource namedParameters = new MapSqlParameterSource("stepId", stepId);
        return jdbcTemplate.query(sql, namedParameters, new CardInteractiveRowMapper());
    }
}
