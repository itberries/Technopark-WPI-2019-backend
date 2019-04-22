package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.ISubsectionDAO;
import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.models.Theory;
import com.itberries.technopark.itberries.services.ISubsectionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SubsectionServiceImpl implements ISubsectionService {
    final ISubsectionDAO subsectionDAO;

    public SubsectionServiceImpl(ISubsectionDAO subsectionDAO) {
        this.subsectionDAO = subsectionDAO;
    }

    @Override
    public List<Subsection> getSubsectionsByName(String sectionName) {
        return subsectionDAO.getSubsectionsBySectionName(sectionName);
    }

    @Override
    public List<Theory> getTheoryPagesForSubsectionBySubsectionId(String name, Long id) {
        return subsectionDAO.getTheoryPagesForSubsectionBySubsectionId(id);
    }

    @Override
    public List<Long> getOrderedSubsectionsIdentifiers(Long id) {
        List<Long> orderedSubsectionsIdentifiers = new ArrayList<Long>();
        List<Subsection> inorderedSubsections = subsectionDAO.getSubsectionsBySectionId(id);

        Long rootId = new Long(-1);
        Map<Long, Integer> subsectionsIdentifiersMap = new HashMap<>();
        for (Subsection subsection : inorderedSubsections) {
            if (subsection.getParentId() == 0) {
                rootId = subsection.getId();
            }
            subsectionsIdentifiersMap.put(subsection.getId(),subsection.getChildId());
        }

        for (int i = 0; i < subsectionsIdentifiersMap.size(); i++) {
            orderedSubsectionsIdentifiers.add(rootId);
            rootId = Long.parseLong(subsectionsIdentifiersMap.get(rootId).toString());
        }
        return orderedSubsectionsIdentifiers;
    }

    @Override
    public List<Subsection> getSubsectionsBySectionId(Long sectionId) {
        return subsectionDAO.getSubsectionsBySectionId(sectionId);
    }
}
