package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import java.util.Map;

public class TurnQuestion extends Turn {

    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;

    @JsonCreator
    public TurnQuestion(@JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "TurnQuestion{" +
                "payload=" + payload +
                '}';
    }

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        /**
         * Номер правильного ответа в задании question
         */
        private final String data;

        @JsonCreator
        public Payload(@JsonProperty("data") String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "data=" + data +
                    '}';
        }

        public String getData() {
            return data;
        }


    }
    public static void main(String[] args) {
        TurnQuestion turnQuestion = new TurnQuestion(new TurnQuestion.Payload("4"));
        Gson gson = new Gson();
        System.out.println(gson.toJson(turnQuestion));

    }
}
