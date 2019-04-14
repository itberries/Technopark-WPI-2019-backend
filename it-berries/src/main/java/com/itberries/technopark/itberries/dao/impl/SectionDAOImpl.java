package com.itberries.technopark.itberries.dao.impl;

import com.itberries.technopark.itberries.dao.ISectionDAO;
import com.itberries.technopark.itberries.models.Section;
import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.models.Theory;
import com.itberries.technopark.itberries.responses.ThereIsNoSectionsException;
import com.itberries.technopark.itberries.rowmapper.SectionRowMapper;
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
import javax.xml.crypto.Data;
import java.util.List;

@Service
@Transactional
public class SectionDAOImpl implements ISectionDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static RowMapper<Section> sectionRowMapper = new SectionRowMapper();
    private static RowMapper<Subsection> subsectionRowMapper = new SubsectionRowMapper();

    @Autowired
    public SectionDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Section getSectionById(Long id) {
        final String sql = "SELECT sect.id, sect.name, sect.parent_id, sect.child_id FROM sections AS sect WHERE sect.id=:id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        try {
            return jdbcTemplate.queryForObject(sql, namedParameters, sectionRowMapper);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public Section getSectionByParentId(Long parentId) {
        final String sql = "SELECT sect.id, sect.name, sect.parent_id, sect.child_id FROM sections AS sect WHERE sect.parent_id=:id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", parentId);
        try {
            return jdbcTemplate.queryForObject(sql, namedParameters, sectionRowMapper);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Section> getSections() {
        final String blankSectionSql = "SELECT sect.id, sect.name, sect.parent_id, sect.child_id from sections AS sect";
        final String innerSubsectionsSql = "SELECT subsect.id, subsect.id_section, subsect.name, subsect.parent_id, subsect.child_id " +
                                            "FROM subsections AS subsect " +
                                            "WHERE subsect.id_section=:id";

        try {
            List<Section> sections = null;
            sections = jdbcTemplate.query(blankSectionSql, new MapSqlParameterSource(),
                    new BeanPropertyRowMapper(Section.class));

            if ((sections != null) && (!sections.isEmpty())) {
                for (Section section : sections) {
                    SqlParameterSource namedParameters
                            = new MapSqlParameterSource("id", section.getId());

                    List<Subsection> subsections = null;
                    subsections = jdbcTemplate.query(innerSubsectionsSql, namedParameters,
                            new BeanPropertyRowMapper(Subsection.class));
                    if (subsections != null) {
                        section.setSubsections(subsections);
                    }
                }

                return sections;
            } else {
                throw new ThereIsNoSectionsException();
            }
        } catch (DataAccessException ex) {
            throw new ThereIsNoSectionsException();
        }
    }

    @Override
    public boolean doesSectionExist(Long id) {

        final String sql = "SELECT sect.id, sect.name FROM sections AS sect WHERE sect.id=:id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        Section checkSect = null;
        try {
            checkSect = jdbcTemplate.queryForObject(sql, namedParameters, sectionRowMapper);
        } catch (DataAccessException ex) {
            return false;
        }

        if ((checkSect != null) && (checkSect.getId() == id))  {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean doesSubsectionMatchSection(Long id_subsection, Long id) {
        final String sql = "SELECT subsect.id, subsect.id_section, subsect.name FROM subsections AS subsect WHERE subsect.id=:id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        Subsection checkSubsect = null;
        try {
            checkSubsect = jdbcTemplate.queryForObject(sql, namedParameters, subsectionRowMapper);
        } catch (DataAccessException ex) {
            return false;
        }

        if ((checkSubsect != null) && (checkSubsect.getId_section() == id))  {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Theory> getTheoryBySectionIdAndSubsectionId(Long id_subsection) {
        final String sql = "SELECT t.id, t.id_subsection, s.name AS subsection_name, " +
                "t.text, t.img, s.id_section, t.page FROM theory t\n" +
                "JOIN subsections s on t.id_subsection = s.id\n" +
                "WHERE t.id_subsection = :id";

        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("id", id_subsection);
            return jdbcTemplate.query(sql, namedParameters,new BeanPropertyRowMapper(Theory.class));
        } catch (DataAccessException ex) {
            throw new ThereIsNoSectionsException();
        }

    }

    @Override
    public List<Subsection> getSubsectionsBySectionId(Long id) {
        final String subsectionsSql = "SELECT subsect.id, subsect.id_section, subsect.name " +
                "FROM subsections AS subsect " +
                "WHERE subsect.id_section=:id";

        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);

            return jdbcTemplate.query(subsectionsSql, namedParameters,
                    new BeanPropertyRowMapper(Subsection.class));
        } catch (DataAccessException ex) {
            throw new ThereIsNoSectionsException();
        }
    }
}
