package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinGame extends Message {
    /**
     * Тип хода (имеет отличия в зависимости от типа игры)
     * match содержит в себе список key-value
     */
    private String gameType;

    /**
     * Номер шага
     */
    private Long stepId;

    @Override
    public String toString() {
        return "JoinGame{" +
                "gameType='" + gameType + '\'' +
                ", stepId=" + stepId +
                '}';
    }

    @JsonCreator
    public JoinGame(@JsonProperty("gameType") String gameType,
                    @JsonProperty("stepId") Long stepId) {
        this.gameType = gameType;
        this.stepId = stepId;
    }

    public String getGameType() {
        return gameType;
    }

    public Long getStepId() {
        return stepId;
    }

//    public static void main(String[] args) {
//        JoinGame joinGame = new JoinGame("match", 3L);
//        Gson gson = new  Gson();
//        String s = gson.toJson(joinGame);
//        System.out.println(s);
//    }
}
