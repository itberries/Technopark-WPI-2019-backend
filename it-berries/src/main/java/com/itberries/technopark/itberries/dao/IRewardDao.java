package com.itberries.technopark.itberries.dao;

import com.itberries.technopark.itberries.models.HanbookReward;
import com.itberries.technopark.itberries.models.Reward;

import java.util.List;

public interface IRewardDao {

    /**
     * Получить список всех возможных наград пользователя
     * @return
     */
    List<Reward> getRewards();

    /**
     * Получить список ачивок конкретного пользователя
     * @param userId уникальный идентификатор пользователя
     * @return список уникальных ключей ачивок
     */
    List<Long> getRewardsByUserId(Long userId);


    /**
     * Получаем первую из следующих ачивок, которых
     * еще нет у пользователя
     * @param userId уникальный идентификатор пользователя
     * @return инфо об ачивке, которой еще нет бользователя (reward_id, score_limit)
     */
    HanbookReward getFisrtAbsentRewardByUserId(Long userId);

    /**
     * Добавить новую ачивку пользвателю
     * @param userId уникальный идентификатор пользователя
     * @param rewardId уникальный идентификатор ачивки
     */
    void addNewUserReward(Long userId, Long rewardId);

    /**
     * Получить инфо об ачивке по id
     * @param rewardId уникальный ключ ачивки
     * @return инфо об ачивке
     */
    Reward getRewardById(Long rewardId);
}
