package com.itberries.technopark.itberries.dao.impl;

import com.itberries.technopark.itberries.dao.IRewardDao;
import com.itberries.technopark.itberries.models.Reward;
import com.itberries.technopark.itberries.models.Section;
import com.itberries.technopark.itberries.models.Subsection;
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
}
