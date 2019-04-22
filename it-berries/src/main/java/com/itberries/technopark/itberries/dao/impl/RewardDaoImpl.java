package com.itberries.technopark.itberries.dao.impl;

import com.itberries.technopark.itberries.dao.IRewardDao;
import com.itberries.technopark.itberries.models.HanbookReward;
import com.itberries.technopark.itberries.models.Reward;
import com.itberries.technopark.itberries.models.Section;
import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.rowmapper.HanbookRewardRowMapper;
import com.itberries.technopark.itberries.rowmapper.RewardRowMapper;
import com.itberries.technopark.itberries.rowmapper.SectionRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RewardDaoImpl implements IRewardDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static RowMapper<Reward> rewardRowMapper = new RewardRowMapper();
    private static RowMapper<HanbookReward> hanbookRewardRowMapper = new HanbookRewardRowMapper();

    @Autowired
    public RewardDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reward> getRewards() {
        final String sql = "select r.id as id, r.image_url as image_url, r.note as note\n" +
                "from rewards r";
        return jdbcTemplate.query(sql, new RewardRowMapper());
    }

    public List<Long> getRewardsByUserId(Long userId) {
        final String sql = "select r.id \n" +
                "from rewards r\n" +
                "       join users_rewards ur on r.id = ur.reward_id\n" +
                "where user_id = :userId";
        return jdbcTemplate.queryForList(sql,
                new MapSqlParameterSource().addValue("userId", userId),
                Long.class);
    }

    public HanbookReward getFisrtAbsentRewardByUserId(Long userId) {
        final String sql = "Select * from hanbook_rewards\n" +
                "where id not in\n" +
                "      (select r.id from rewards r join users_rewards ur on r.id=ur.id where user_id = :userId)\n" +
                "order by score_limit\n" +
                "limit 1";

        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource().addValue("userId", userId),
                new HanbookRewardRowMapper()
        );
    }

    @Override
    public void addNewUserReward(Long userId, Long rewardId) {
        final String sql = "insert into users_rewards (user_id, reward_id)\n" +
                "values (:userId, :rewardId)";

        jdbcTemplate.update(sql,
                new MapSqlParameterSource("userId", userId)
                        .addValue("rewardId", rewardId));
    }

    @Override
    public Reward getRewardById(Long rewardId) {
        final String sql = "select id, image_url, note\n" +
                "from rewards\n" +
                "where id = :rewardId";
        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource()
                        .addValue("rewardId", rewardId),
                new RewardRowMapper()
        );
    }
}
