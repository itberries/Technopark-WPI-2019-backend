package com.itberries.technopark.itberries.websocket.games.models;

import com.google.gson.Gson;
import com.itberries.technopark.itberries.websocket.events.Turn;
import com.itberries.technopark.itberries.websocket.events.TurnMatch;
import com.itberries.technopark.itberries.websocket.games.services.strategies.models.MatchAnswerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * Состояние игры пользователя
 */
public class GamePlayerStatus {
    private static final Logger LOGGER = LoggerFactory.getLogger(GamePlayerStatus.class);
    /**
     * Тип игры (match)
     */
    private String type;

    /**
     * Количество правильных ответов
     */
    private int correctAnswers;

    /**
     * Полное количество вопросов
     */
    private int totalQuestions;

    /**
     * Список вопросов на конкретную задачу
     */
    private String task;

    /**
     * Идентификатор шага, к которому относится игра
     */
    private Long stepId;

    /**
     * Идентификатор игры
     */
    private Long gameId;


    /**
     * Правильный ответ на конкретную игру
     */
    private String correctAnswer;

    /**
     * Задачи, на которые еще не было
     * дано верных ответов (пары в мэтчах)
     */
    private String unAnsweredTask;

    public GamePlayerStatus(String type,
                            int correctAnswers,
                            int totalQuestions,
                            String task,
                            Long stepId,
                            Long gameId, String correctAnswer) {
        this.type = type;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.task = task;
        this.stepId = stepId;
        this.gameId = gameId;
        this.correctAnswer = correctAnswer;
        this.unAnsweredTask =  this.correctAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public boolean increaseCorrectAnswersAmount(Turn turn) {
        Gson gson = new Gson();
        boolean result = Boolean.FALSE;
        if (!"match".equals(this.type)) {
            this.correctAnswers += 1;
            result = Boolean.TRUE;
        } else {
            TurnMatch turnMatch = (TurnMatch) turn;
            //все пары
            MatchAnswerList matchAnswerList = gson.fromJson(this.unAnsweredTask, MatchAnswerList.class);
            //ответ пользователя
            Map<String, String> answer = turnMatch.getPayload().getData();

            List<Map<String, String>> data1 = matchAnswerList.getData();

            if (Boolean.TRUE.equals(removeData(data1, answer))) { //если действительно пришла новая верная пара, которой до этого не было
                this.correctAnswers += 1;
                result = Boolean.TRUE;
            }
            matchAnswerList.setData(data1);
            this.unAnsweredTask = gson.toJson(matchAnswerList);

            LOGGER.info(String.format("MATCH ANSWER LIST: %s", matchAnswerList.toString()));
        }
        return result;
    }

    private boolean removeData(List<Map<String, String>> data1, Map<String, String> answer) {
        Optional<Map.Entry<String, String>> first = answer.entrySet().stream().findFirst();
        if (first.isPresent()) {
            Map.Entry<String, String> stringStringEntry = first.get();
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();
            int idx = -1;
            for (int i = 0; i < data1.size(); i++) {
                Map<String, String> stringStringMap = data1.get(i);
                if (stringStringMap.containsKey(key) && stringStringMap.get(key).equals(value)) {
                    idx = i;
                    break;
                } else if (stringStringMap.containsKey(value) && stringStringMap.get(value).equals(key)) {
                    idx = i;
                    break;
                }
            }

            if (idx != -1) {
                data1.remove(idx);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }
}
