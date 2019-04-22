package com.itberries.technopark.itberries.services;

import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.models.Theory;

import java.util.List;

public interface ISubsectionService {
    List<Subsection> getSubsectionsBySectionId(Long sectionId);

    List<Subsection> getSubsectionsByName(String sectionName);

    List<Long> getOrderedSubsectionsIdentifiers(Long id);

    List<Theory> getTheoryPagesForSubsectionBySubsectionId(String name, Long id);
}
