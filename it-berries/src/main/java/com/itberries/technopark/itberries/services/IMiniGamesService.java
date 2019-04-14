package com.itberries.technopark.itberries.services;

import com.itberries.technopark.itberries.models.CardInteractive;

import java.util.List;

public interface IMiniGamesService  {
    /**
     * Получает уникальный ключ игры по шагу
     * @param stepId - шаг подсекции
     * @return уникальный индентификатор игры
     */
    List<Long> getGamesIdByStepId(Long stepId);


    /**
     * Получить тип игры в строком виде при
     * заданном gameId
     * @param gameId - уникальный идентификатор игры
     * @return тип игры
     */
    String getGameTypeByGameId(Long gameId);

    /**
     * Получить все карточки  интерактива для нужного шага по идентификатору
     * @param stepId уникальный индентификатор шага
     * @return список карточек
     */
    List<CardInteractive> getCardsByStepId(Long stepId);
}
