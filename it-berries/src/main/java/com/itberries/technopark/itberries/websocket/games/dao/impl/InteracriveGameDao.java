package com.itberries.technopark.itberries.websocket.games.dao.impl;

import com.itberries.technopark.itberries.websocket.games.dao.IInteracriveGameDao;
import com.itberries.technopark.itberries.websocket.games.dao.rowmappers.MPGameRowMapper;
import com.itberries.technopark.itberries.websocket.games.models.MPGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class InteracriveGameDao implements IInteracriveGameDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public InteracriveGameDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPGame> getTasks() {
        final String sql = "select *\n" +
                "from (select mg.id, mg.note as note1, mgt.type, answer, cr.note as note2, row_number() over (partition by mgt.type) num\n" +
                "      from mini_games mg\n" +
                "             join steps st on st.id = mg.step_id\n" +
                "             join cards cr on cr.step_id = st.id\n" +
                "             join mini_games_types mgt on mg.type_id = mgt.id\n" +
                "             join answers_on_chain a on mg.id = a.game_id) X\n" +
                "where num <= 1\n" +
                "UNION\n" +
                "select *\n" +
                "from (select mg.id, mg.note as note1, mgt.type, answer, cr.note as note2, row_number() over (partition by mgt.type) num\n" +
                "      from mini_games mg\n" +
                "             join steps st on st.id = mg.step_id\n" +
                "             join cards cr on cr.step_id = st.id\n" +
                "             join mini_games_types mgt on mg.type_id = mgt.id\n" +
                "             join answers_on_match aom on mg.id = aom.game_id) X\n" +
                "where num <= 1\n" +
                "UNION\n" +
                "select *\n" +
                "from (select mg.id, mg.note as note1, mgt.type, answer, cr.note as note2, row_number() over (partition by mgt.type) num\n" +
                "      from mini_games mg\n" +
                "             join steps st on st.id = mg.step_id\n" +
                "             join cards cr on cr.step_id = st.id\n" +
                "             join mini_games_types mgt on mg.type_id = mgt.id\n" +
                "             join answers_on_question a2 on mg.id = a2.game_id) X\n" +
                "where num <= 1";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), new MPGameRowMapper());
    }
}
