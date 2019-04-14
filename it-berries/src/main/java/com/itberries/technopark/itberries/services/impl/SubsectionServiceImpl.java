package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.ISubsectionDAO;
import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.models.Theory;
import com.itberries.technopark.itberries.services.ISubsectionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
}
