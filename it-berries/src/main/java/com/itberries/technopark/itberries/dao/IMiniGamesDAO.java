package com.itberries.technopark.itberries.dao;

import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.CardInteractive;

import java.util.List;

public interface IMiniGamesDAO {
//    /**
//     * Получает ответ на заданную игру в виде строки (json)
//     * @return ответ на игру в виде строки
//     */
//    String getAnswerGameByGameId(Long gameId);

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
     * Получить условия задач в виде карточек
     * @param stepId уникальный идентификатор шага
     * @return список карточек с условием задач
     */
    List<CardInteractive> getCardsByStepId(Long stepId);

}
