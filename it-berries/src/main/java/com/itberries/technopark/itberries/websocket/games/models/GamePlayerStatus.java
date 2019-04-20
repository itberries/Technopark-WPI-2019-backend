package com.itberries.technopark.itberries.websocket.games.models;

/**
 * Состояние игры пользователя
 */
public class GamePlayerStatus {
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
