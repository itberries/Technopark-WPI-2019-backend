package com.itberries.technopark.itberries.dao;

import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.Step;

import java.util.List;

public interface IStepDAO {
    /**
     * Получить шаг по его идентификатору
     * @param stepId
     * @return
     */
    Step getStepById(Integer stepId);

    /**
     * Получить все шаги в заданной секции
     * @param subsectionId уникальный ключ секции
     * @return список шагов
     */
    List<Step> getStepsBySectionId(Integer subsectionId);

    /**
     * Получить номер шага, на котором оставновился пользователь
     * в заданной подсекции
     * @param userId - уникальный ключ пользователя
     * @param subsectionId - уникальный ключ подсекции
     * @return шаг
     */
    Step getCurrentStepByUserIdAndSubsectionId(Long userId, Integer subsectionId);

    /**
     * Получить все шаги, находящиеся в иерархии выше заданного шага
     * @param stepId уникальный ключ заданного шага
     * @return список шагов, находящихся выше по иерархии
     */
    List<Step> getAllStepParents(Integer stepId);

    /**
     * Получить все карточки для нужного шага по идентификатору
     * @param stepId
     * @return список карточек
     */
    List<Card> getCardsByStepId(Integer stepId);

}
