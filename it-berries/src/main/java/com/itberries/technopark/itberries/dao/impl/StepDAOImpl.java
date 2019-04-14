package com.itberries.technopark.itberries.dao.impl;

import com.itberries.technopark.itberries.dao.IStepDAO;
import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.Step;
import com.itberries.technopark.itberries.responses.ThereIsNoSuchStepException;
import com.itberries.technopark.itberries.rowmapper.CardRowMapper;
import com.itberries.technopark.itberries.rowmapper.StepRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StepDAOImpl implements IStepDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public StepDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Step getStepById(Integer stepId) {
        final String sql = "SELECT s.id, s.name, s.type, s.parent_id, s.child_id, s.subsection_id" +
                " FROM steps AS s" +
                " WHERE s.id = :stepId";
        try {
            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource()
                            .addValue("stepId", stepId),
                    new StepRowMapper()
            );
        } catch (DataAccessException ex) {
            throw new ThereIsNoSuchStepException();
        }

    }

    @Override
    public List<Step> getStepsBySectionId(Integer subsectionId) {
        final String sql = "select id, name, type, parent_id, child_id, subsection_id\n" +
                "from steps\n" +
                "where subsection_id = :subsectionId;";
        SqlParameterSource namedParameters = new MapSqlParameterSource("subsectionId", subsectionId);
        return jdbcTemplate.query(sql, namedParameters, new StepRowMapper());
    }

    @Override
    public Step getCurrentStepByUserIdAndSubsectionId(Long userId, Integer subsectionId) {
        final String sql = "select s.id, s.name, s.type, s.parent_id, s.subsection_id, s.child_id\n" +
                "from steps s\n" +
                "       join users_states us on us.step_id = s.id\n" +
                "where us.user_id = :userId\n" +
                "  and us.subsection_id = :subsectonId";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource()
                        .addValue("userId", userId)
                        .addValue("subsectonId", subsectionId),
                new StepRowMapper()
        );
    }

    @Override
    public List<Step> getAllStepParents(Integer stepId) {
        return null;
    }

    @Override
    public List<Card> getCardsByStepId(Integer stepId) {
        final String sql = "SELECT c.id, c.note, c.image, c.step_id, c.parent_id, c.child_id\n" +
                "FROM cards AS c\n" +
                "WHERE step_id = :stepId\n";
        SqlParameterSource namedParameters = new MapSqlParameterSource("stepId", stepId);
        return jdbcTemplate.query(sql, namedParameters, new CardRowMapper());
    }
}
