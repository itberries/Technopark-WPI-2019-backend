package com.itberries.technopark.itberries.dao;

import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.models.Theory;

import java.util.List;

public interface ISubsectionDAO {
    Subsection getSubsectionById(Long subsectionId);
    Subsection getSubsectionByParentId(Long parentId);
    Subsection getSubsectionBySectionIdAndParentId(Long sectionId, Long parentId);
    List<Subsection> getSubsectionsBySectionName(String name);
    List<Subsection> getSubsectionsBySectionId(Long sectionId);

    List<Theory> getTheoryPagesForSubsectionBySubsectionId(Long id);
}
