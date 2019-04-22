package com.itberries.technopark.itberries.services;

import com.itberries.technopark.itberries.models.Reward;

import java.util.List;

public interface IRewardService {
    /**
     * Получить список всех имеющихя в игре ачивок
     * @return
     */
    List<Reward> getRewards();

    /**
     * Получить достижения конкретного пользователя
     * @param userId идентификатор пользователя
     * @return список из уникальных номеров ачивок
     */
    List<Long> getRewardsByUserId(Long userId);

    /**
     * Проверить количество очков у пользователя
     * стоит ли дать ему ачивку?
     * @return  возвращаем новую ачивку пользователя (или null)
     */
    Reward updateRewardsByUser(Long userId);
}
