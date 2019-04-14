package com.itberries.technopark.itberries.dao.impl;

import com.itberries.technopark.itberries.dao.ISubsectionDAO;
import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.models.Theory;
import com.itberries.technopark.itberries.responses.ThereIsNoSectionsException;
import com.itberries.technopark.itberries.responses.ThereIsNoTheoryException;
import com.itberries.technopark.itberries.rowmapper.SubsectionRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SubsectionDAOImpl implements ISubsectionDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static RowMapper<Subsection> subsectionRowMapper = new SubsectionRowMapper();

    @Autowired
    public SubsectionDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Subsection getSubsectionById(Long subsectionId) {
        final String sql = "SELECT subsect.id, subsect.id_section, subsect.name, subsect.parent_id, subsect.child_id " +
                "FROM subsections AS subsect " +
                "WHERE subsect.id=:subsectionId";
        SqlParameterSource namedParameters = new MapSqlParameterSource("subsectionId", subsectionId);
        try {
            return jdbcTemplate.queryForObject(sql, namedParameters, subsectionRowMapper);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public Subsection getSubsectionByParentId(Long parentId) {
        final String sql = "SELECT subsect.id, subsect.id_section, subsect.name, subsect.parent_id, subsect.child_id " +
                "FROM subsections AS subsect " +
                "WHERE subsect.parent_id=:parentId";
        SqlParameterSource namedParameters = new MapSqlParameterSource("parentId", parentId);
        try {
            return jdbcTemplate.queryForObject(sql, namedParameters, subsectionRowMapper);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public Subsection getSubsectionBySectionIdAndParentId(Long sectionId, Long parentId) {
        final String sql = "SELECT subsect.id, subsect.id_section, subsect.name, subsect.parent_id, subsect.child_id " +
                "FROM subsections AS subsect " +
                "WHERE subsect.parent_id=:parentId AND subsect.id_section=:sectionId";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("parentId", parentId)
                .addValue("sectionId",sectionId);
        try {
            return jdbcTemplate.queryForObject(sql, namedParameters, subsectionRowMapper);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Subsection> getSubsectionsBySectionName(String name) {
        try {
            // TODO: add table users_subsections which contain passed subsections for the user
            // TODO: add column responsible for subsection status - passed / not passed
            final String sql = "SELECT subsect.id, subsect.id_section, subsect.name\n" +
                    "FROM sections AS sect\n" +
                    "  JOIN subsections AS subsect\n" +
                    "  ON sect.id = subsect.id_section\n" +
                    "WHERE sect.name = :name";

            SqlParameterSource namedParameters = new MapSqlParameterSource("name", name);

           return jdbcTemplate.query(sql, namedParameters,new BeanPropertyRowMapper(Subsection.class));
        }catch (DataAccessException ex){
            throw  new ThereIsNoSectionsException();
        }
    }

    @Override
    public List<Theory> getTheoryPagesForSubsectionBySubsectionId(Long id) {
        try {
            final String sql = "SELECT t.id, t.id_subsection, s.name AS section_name, " +
                    "t.text, t.img, s.id_section, t.page FROM theory t\n" +
                    "JOIN subsections s on t.id_subsection = s.id\n" +
                    "WHERE t.id_subsection = :id";

            SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
            return jdbcTemplate.query(sql, namedParameters,new BeanPropertyRowMapper(Theory.class));
        } catch (DataAccessException ex){
            throw  new ThereIsNoTheoryException();
        }

    }
}
