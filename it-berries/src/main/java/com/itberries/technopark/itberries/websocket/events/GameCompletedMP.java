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

        private final Integer score;

        private final Reward reward;

        /**
         * lose, win, draw
         */
        private final String gameStatus;

        private String note;

        @JsonCreator
        public Payload(@JsonProperty("score") Integer score,
                       @JsonProperty("reward") Reward reward,
                       @JsonProperty("gameStatus") String gameStatus,
                       @JsonProperty("note") String note) {
            this.score = score;
            this.reward = reward;
            this.gameStatus = gameStatus;
            this.note = note;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "score=" + score +
                    ", reward=" + reward +
                    ", gameStatus='" + gameStatus + '\'' +
                    ", note='" + note + '\'' +
                    '}';
        }

        public Integer getScore() {
            return score;
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

        public Integer getResult() {
            return score;
        }
    }

}
