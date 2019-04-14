package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.IStepDAO;
import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.Step;
import com.itberries.technopark.itberries.services.IStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StepServiceImpl implements IStepService {

    private IStepDAO iStepDAO;

    @Autowired
    public StepServiceImpl(IStepDAO iStepDAO) {
        this.iStepDAO = iStepDAO;
    }

    @Override
    public List<Step> getStepsBySectionId(Integer subsectionId) {
        return iStepDAO.getStepsBySectionId(subsectionId);
    }

    @Override
    public Step getCurrentStepByUserIdAndSubsectionId(Long userId, Integer subsectionId) {
        return iStepDAO.getCurrentStepByUserIdAndSubsectionId(userId, subsectionId);
    }

    @Override
    public List<Card> getCardsByStepId(Integer stepId) {
        return iStepDAO.getCardsByStepId(stepId);
    }
}
