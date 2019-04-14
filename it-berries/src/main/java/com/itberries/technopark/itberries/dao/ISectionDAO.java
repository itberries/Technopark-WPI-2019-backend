package com.itberries.technopark.itberries.dao;

import com.itberries.technopark.itberries.models.Section;
import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.models.Theory;

import java.util.List;

public interface ISectionDAO {
    Section getSectionById(Long id);
    Section getSectionByParentId(Long parentId);
    List<Section> getSections();
    List<Subsection> getSubsectionsBySectionId(Long id);
    boolean doesSectionExist(Long id);
    boolean doesSubsectionMatchSection(Long id_subsection, Long id);
    List<Theory> getTheoryBySectionIdAndSubsectionId(Long id_subsection);
}
