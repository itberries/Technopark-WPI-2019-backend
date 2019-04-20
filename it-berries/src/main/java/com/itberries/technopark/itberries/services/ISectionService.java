package com.itberries.technopark.itberries.services;

import com.itberries.technopark.itberries.models.Section;
import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.models.Theory;

import java.util.List;

public interface ISectionService {
    List<Section> getSections();
    List<Subsection> getSubsectionsBySectionId(Long id);

    List<Long> getOrderedSectionsIdentifiers();

    boolean doesSectionExist(Long id);
    boolean doesSubsectionMatchSection(Long id_subsection, Long id);

    List<Theory> getTheoryBySectionIdAndSubsectionId(Long id_subsection);
}
