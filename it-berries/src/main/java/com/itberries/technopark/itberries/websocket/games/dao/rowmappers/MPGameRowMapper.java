package com.itberries.technopark.itberries.websocket.games.dao.rowmappers;

import com.itberries.technopark.itberries.websocket.games.models.MPGame;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MPGameRowMapper implements RowMapper<MPGame> {
    @Override
    public MPGame mapRow(ResultSet resultSet, int i) throws SQLException {
        return new MPGame(resultSet.getLong("id"),
                resultSet.getString("task"),
                resultSet.getString("answer"),
                resultSet.getString("type"),
                resultSet.getString("note"));
    }
}
