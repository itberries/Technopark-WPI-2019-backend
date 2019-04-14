package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.ISectionDAO;
import com.itberries.technopark.itberries.models.Section;
import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.models.Theory;
import com.itberries.technopark.itberries.services.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
}
