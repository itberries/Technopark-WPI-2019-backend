package com.itberries.technopark.itberries.websocket.games.services;

import java.io.IOException;

public interface ICheckAnswerService {
    /**
     * Проверить ответ  (зависит от типа игры)
     * @param gameId - индентификатор игры, для которой нужно проверить ответ
     */
    boolean checkAnswerByGameId(Long gameId, String answer) throws IOException;

}
