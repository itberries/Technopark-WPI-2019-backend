package com.itberries.technopark.itberries.websocket.games.services.strategies;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itberries.technopark.itberries.websocket.games.dao.IAnswerOnMatchDAO;
import com.itberries.technopark.itberries.websocket.games.models.GamePlayerStatus;
import com.itberries.technopark.itberries.websocket.games.services.ICheckAnswerService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.models.MatchAnswerList;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

public class CheckAnswerMatchService implements ICheckAnswerService {
    private IAnswerOnMatchDAO iAnswerOnMatchDAO;
    private final ObjectMapper objectMapper;
    private Type type = new TypeToken<Map<String, String>>() {
    }.getType();
    private Gson gson = new Gson();

    public CheckAnswerMatchService(IAnswerOnMatchDAO iAnswerOnMatchDAO, ObjectMapper objectMapper) {
        this.iAnswerOnMatchDAO = iAnswerOnMatchDAO;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean checkAnswerByGameId(Long gameId, String answer) throws IOException {
        String answerByGameId = iAnswerOnMatchDAO.findAnswerByGameId(gameId);
        //Получаем все пары корректных ответов
        MatchAnswerList correctMathPairs = gson.fromJson(answerByGameId, MatchAnswerList.class);
        //Конвертируем ответ пользователя
        Map<String, String> userAnswer = gson.fromJson(answer, type);
        String key, value;
        if (userAnswer.keySet().stream().findFirst().isPresent()) {//если ответ не пуст
            key = userAnswer.keySet().stream().findFirst().get();
            value = userAnswer.get(key);
        } else {
            return false;
        }

        //Если ответы совпадают
        Optional<Map<String, String>> first = correctMathPairs.getData()
                .stream()
                .filter(s -> s.containsKey(key) || s.containsKey(value)).findFirst();
        if (first.isPresent()) {
            Map<String, String> stringStringMap = first.get();

            return userAnswer.get(key).equals(stringStringMap.get(key))
                    || key.equals(stringStringMap.get(value));// [ 1 байт - 8 бит ]  [8 бит - 1 байт]
        }

        return false;
    }
}
