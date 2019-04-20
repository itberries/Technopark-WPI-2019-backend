package com.itberries.technopark.itberries.websocket.games.services;

import com.itberries.technopark.itberries.websocket.games.models.GamePlayerStatus;

import java.io.IOException;

public interface ICheckAnswerService {
    /**
     * Проверить ответ  (зависит от типа игры)
     * @param correctAnswer - индентификатор игры, для которой нужно проверить ответ
     */
    boolean checkAnswerByGameId(String correctAnswer, String answer) throws IOException;

}
