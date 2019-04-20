package com.itberries.technopark.itberries.websocket.games.dao;

public interface IAnswerOnChainDAO {
    /**
     * Получить ответ для конкретной игры
     * @param gameId уникальный идентфиикатор игры
     * @return ответ на задание в json
     */
    String findAnswerByGameId(Long gameId);
}
