package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itberries.technopark.itberries.models.Reward;

public class GameCompletedMP extends Message {

    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;

    @JsonCreator
    public GameCompletedMP(@JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }

    public Payload getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "GameCompleted{" +
                "payload=" + payload +
                '}';
    }

    public static class Payload {

        private final Integer coins;

        private final Reward reward;

        /**
         * lose, win, draw
         */
        private final String gameStatus;

        private String note;

        @JsonCreator
        public Payload(@JsonProperty("coins") Integer coins,
                       @JsonProperty("reward") Reward reward,
                       @JsonProperty("gameStatus") String gameStatus,
                       @JsonProperty("note") String note) {
            this.coins = coins;
            this.reward = reward;
            this.gameStatus = gameStatus;
            this.note = note;
        }



        public String getGameStatus() {
            return gameStatus;
        }

        public String getNote() {
            return note;
        }

        public Reward getReward() {
            return reward;
        }

        public Integer getCoins() {
            return coins;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "coins=" + coins +
                    ", reward=" + reward +
                    ", gameStatus='" + gameStatus + '\'' +
                    ", note='" + note + '\'' +
                    '}';
        }
    }

}
