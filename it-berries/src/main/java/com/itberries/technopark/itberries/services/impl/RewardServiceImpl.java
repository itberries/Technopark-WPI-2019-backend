package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.IRewardDao;
import com.itberries.technopark.itberries.models.Reward;
import com.itberries.technopark.itberries.services.IRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardServiceImpl implements IRewardService {
    private IRewardDao iRewardDao;

    @Autowired
    public RewardServiceImpl(IRewardDao iRewardDao) {
        this.iRewardDao = iRewardDao;
    }

    @Override
    public List<Reward> getRewards() {
        return iRewardDao.getRewards();
    }
}
