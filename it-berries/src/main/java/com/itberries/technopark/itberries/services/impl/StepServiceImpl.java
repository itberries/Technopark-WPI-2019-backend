package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.IStepDAO;
import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.Step;
import com.itberries.technopark.itberries.services.IStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Long> getOrderedStepsIdentifiers(Long id) {
        List<Long> orderedStepsIdentifiers = new ArrayList<Long>();
        List<Step> inorderedSteps = iStepDAO.getStepsBySectionId(Integer.parseInt(id.toString()));

        Long rootId = new Long(-1);
        Map<Long, Integer> stepsIdentifiersMap = new HashMap<>();
        for (Step step : inorderedSteps) {
            Long currentId = Long.parseLong(step.getId().toString());
            if (step.getParentId() == 0) {
                rootId = currentId;
            }
            stepsIdentifiersMap.put(currentId,step.getChildId());
        }

        for (int i = 0; i < stepsIdentifiersMap.size(); i++) {
            orderedStepsIdentifiers.add(rootId);
            rootId = Long.parseLong(stepsIdentifiersMap.get(rootId).toString());
        }
        return orderedStepsIdentifiers;
    }
}
