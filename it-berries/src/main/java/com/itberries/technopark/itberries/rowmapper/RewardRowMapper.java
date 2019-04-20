package com.itberries.technopark.itberries.rowmapper;

import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.Reward;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RewardRowMapper implements RowMapper<Reward> {
    @Override
    public Reward mapRow(ResultSet resultSet, int i) throws SQLException {
        Reward reward = new Reward();
        reward.setId(resultSet.getLong("id"));
        reward.setImageUrl(resultSet.getString("image_url"));
        reward.setNote(resultSet.getString("note"));
        return reward;
    }
}
