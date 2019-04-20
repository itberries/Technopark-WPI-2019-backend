package com.itberries.technopark.itberries.services;

import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.Step;

import java.util.List;

public interface IStepService {
    /**
     * Получить все шаги в заданной секции
     *
     * @param subsectionId уникальный ключ секции
     * @return список шагов
     */
    List<Step> getStepsBySectionId(Integer subsectionId);

    /**
     * Получить номер шага, на котором оставновился пользователь
     * в заданной подсекции
     *
     * @param userId       - уникальный ключ пользователя
     * @param subsectionId - уникальный ключ подсекции
     * @return шаг
     */
    Step getCurrentStepByUserIdAndSubsectionId(Long userId, Integer subsectionId);

    /**
     * Получить все карточки для нужного шага по идентификатору
     * @param stepId
     * @return список карточек
     */
    List<Card> getCardsByStepId(Integer stepId);

    List<Long> getOrderedStepsIdentifiers(Long id);
}


