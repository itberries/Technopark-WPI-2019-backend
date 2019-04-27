package com.itberries.technopark.itberries.websocket.games.services.strategies;

import com.itberries.technopark.itberries.websocket.games.services.ICheckAnswerService;

import java.io.IOException;

public class CheckAnswerQuestionService implements ICheckAnswerService {

    @Override
    public boolean checkAnswerByGameId(String correctAnswer, String answer) throws IOException {
        return correctAnswer.equals(answer);
    }
}
