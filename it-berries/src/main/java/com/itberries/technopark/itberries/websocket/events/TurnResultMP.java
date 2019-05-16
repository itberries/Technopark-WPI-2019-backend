package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TurnResultMP extends Message {
    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;


    @JsonCreator
    public TurnResultMP(@JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "TurnResult{" +
                "payload=" + payload +
                '}';
    }

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private final String data;

        private final String completed;

        @JsonCreator
        public Payload(@JsonProperty("data") String data, @JsonProperty("completed") String completed) {
            this.data = data;
            this.completed = completed;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "data='" + data + '\'' +
                    ", completed='" + completed + '\'' +
                    '}';
        }

        public String getData() {
            return data;
        }

        public String getCompleted() {
            return completed;
        }
    }

}
