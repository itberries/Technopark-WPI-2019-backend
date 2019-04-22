package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.IRewardDao;
import com.itberries.technopark.itberries.dao.IUserDAO;
import com.itberries.technopark.itberries.models.HanbookReward;
import com.itberries.technopark.itberries.models.Reward;
import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.services.IRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardServiceImpl implements IRewardService {
    private IRewardDao iRewardDao;
    private IUserDAO userDAO;

    @Autowired
    public RewardServiceImpl(IRewardDao iRewardDao, IUserDAO userDAO) {
        this.iRewardDao = iRewardDao;
        this.userDAO = userDAO;
    }

    @Override
    public List<Reward> getRewards() {
        return iRewardDao.getRewards();
    }

    @Override
    public List<Long> getRewardsByUserId(Long userId) {
        return iRewardDao.getRewardsByUserId(userId);
    }

    @Override
    public Reward updateRewardsByUser(Long userId) {
        User userById = userDAO.getUserById(userId);
        Long score = userById.getScore();
        HanbookReward fisrtAbsentRewardByUserId = iRewardDao.getFisrtAbsentRewardByUserId(userId);
        //если пользователь набрал достаточно баллов для получения новой ачивки - выдать ее
        if (userById.getScore() >= fisrtAbsentRewardByUserId.getScoreLimit()) {
            iRewardDao.addNewUserReward(userId, fisrtAbsentRewardByUserId.getRewardId());
            return iRewardDao.getRewardById(fisrtAbsentRewardByUserId.getRewardId());
        }
        return null;
    }
}
