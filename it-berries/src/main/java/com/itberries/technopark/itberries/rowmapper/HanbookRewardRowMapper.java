package com.itberries.technopark.itberries.rowmapper;

import com.itberries.technopark.itberries.models.HanbookReward;
import com.itberries.technopark.itberries.models.Reward;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HanbookRewardRowMapper implements RowMapper<HanbookReward> {
    @Override
    public HanbookReward mapRow(ResultSet resultSet, int i) throws SQLException {
        HanbookReward reward = new HanbookReward();
        reward.setId(resultSet.getLong("id"));
        reward.setScoreLimit(resultSet.getLong("score_limit"));
        reward.setRewardId(resultSet.getLong("reward_id"));
        return reward;
    }
}
