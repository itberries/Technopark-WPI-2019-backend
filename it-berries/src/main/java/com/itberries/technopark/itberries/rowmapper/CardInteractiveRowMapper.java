package com.itberries.technopark.itberries.rowmapper;

import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.CardInteractive;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CardInteractiveRowMapper implements RowMapper<CardInteractive> {
    @Override
    public CardInteractive mapRow(ResultSet resultSet, int i) throws SQLException {
        Card card = new Card();
        card.setId(resultSet.getLong("card_id"));
        card.setNote(resultSet.getString("card_note"));
        card.setImage(resultSet.getString("card_image"));
        card.setParentId(resultSet.getInt("card_parent_id"));
        card.setChildId(resultSet.getInt("card_child_id"));

        CardInteractive cardInteractive = new CardInteractive();
        cardInteractive.setId(resultSet.getLong("id"));
        cardInteractive.setNote(resultSet.getString("note"));
        cardInteractive.setImage(resultSet.getString("image"));
        cardInteractive.setParentId(resultSet.getInt("parent_id"));
        cardInteractive.setChildId(resultSet.getInt("child_id"));
        cardInteractive.setCard(card);
        return cardInteractive;
    }
}
