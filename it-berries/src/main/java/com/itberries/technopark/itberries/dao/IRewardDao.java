package com.itberries.technopark.itberries.dao;

import com.itberries.technopark.itberries.models.Reward;

import java.util.List;

public interface IRewardDao {

    /**
     * Получить список всех возможных наград пользователя
     * @return
     */
    List<Reward> getRewards();
}
