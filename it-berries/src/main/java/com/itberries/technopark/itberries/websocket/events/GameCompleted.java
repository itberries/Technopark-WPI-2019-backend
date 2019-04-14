package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameCompleted extends Message {
    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;

    @JsonCreator
    public GameCompleted(@JsonProperty("payload") Payload payload) {
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

        @JsonCreator
        public Payload(@JsonProperty("score") Integer score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "score=" + score +
                    '}';
        }

        public Integer getResult() {
            return score;
        }
    }
}
