package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

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

    /**
     * Тип игры (singleplayer, multiplayer)
     */
    private String mode;

    @Override
    public String toString() {
        return "JoinGame{" +
                "gameType='" + gameType + '\'' +
                ", stepId=" + stepId +
                ", mode='" + mode + '\'' +
                '}';
    }

    @JsonCreator
    public JoinGame(@JsonProperty("gameType") String gameType,
                    @JsonProperty("stepId") Long stepId,
                    @JsonProperty("mode") String mode) {
        this.gameType = gameType;
        this.stepId = stepId;
        this.mode = mode;
    }

    public String getGameType() {
        return gameType;
    }

    public Long getStepId() {
        return stepId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public static void main(String[] args) {
        JoinGame joinGame = new JoinGame("match", 3L, "singleplayer");
        Gson gson = new Gson();
        String s = gson.toJson(joinGame);
        System.out.println(s);
    }
}
