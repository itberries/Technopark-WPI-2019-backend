package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.ISectionDAO;
import com.itberries.technopark.itberries.models.Section;
import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.models.Theory;
import com.itberries.technopark.itberries.services.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SectionServiceImpl implements ISectionService {
    final ISectionDAO iSectionDAO;

    @Autowired
    public SectionServiceImpl(ISectionDAO iSectionDAO) {
        this.iSectionDAO = iSectionDAO;
    }

    @Override
    public List<Section> getSections() {
        return iSectionDAO.getSections();
    }

    @Override
    public boolean doesSubsectionMatchSection(Long id_subsection, Long id) {
        return iSectionDAO.doesSubsectionMatchSection(id_subsection, id);
    }

    @Override
    public List<Theory> getTheoryBySectionIdAndSubsectionId(Long id_subsection) {
        return iSectionDAO.getTheoryBySectionIdAndSubsectionId(id_subsection);
    }

    @Override
    public boolean doesSectionExist(Long id) {
        return iSectionDAO.doesSectionExist(id);
    }

    @Override
    public List<Subsection> getSubsectionsBySectionId(Long id) {
        return iSectionDAO.getSubsectionsBySectionId(id);
    }

    @Override
    public List<Long> getOrderedSectionsIdentifiers() {
        List<Long> orderedSectionsIdentifiers = new ArrayList<Long>();
        List<Section> inorderedSectionsWithSubsections = iSectionDAO.getSections();

        Long rootId = new Long(-1);
        Map<Long, Integer> sectionsIdentifiersMap = new HashMap<>();
        for (Section section : inorderedSectionsWithSubsections) {
            if (section.getParentId() == 0) {
                rootId = section.getId();
            }
            sectionsIdentifiersMap.put(section.getId(),section.getChildId());
        }

        for (int i = 0; i < sectionsIdentifiersMap.size(); i++) {
            orderedSectionsIdentifiers.add(rootId);
            rootId = Long.parseLong(sectionsIdentifiersMap.get(rootId).toString());
        }
        return orderedSectionsIdentifiers;
    }
}
