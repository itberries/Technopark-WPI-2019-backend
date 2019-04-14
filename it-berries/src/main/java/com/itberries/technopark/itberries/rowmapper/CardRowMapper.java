package com.itberries.technopark.itberries.rowmapper;

import com.itberries.technopark.itberries.models.Card;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CardRowMapper implements RowMapper<Card> {
    @Override
    public Card mapRow(ResultSet resultSet, int i) throws SQLException {
        Card card = new Card();
        card.setId(resultSet.getLong("id"));
        card.setNote(resultSet.getString("note"));
        card.setImage(resultSet.getString("image"));
        card.setParentId(resultSet.getInt("parent_id"));
        card.setChildId(resultSet.getInt("child_id"));
        return card;
    }
}
