package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class TurnMatch extends Turn {
    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;

    @JsonCreator
    public TurnMatch(@JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }


    public Payload getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Turn{" +
                "payload=" + payload +
                '}';
    }

    public static class Payload {


        private final Map<String, String> data;

        @JsonCreator
        public Payload(@JsonProperty("data") Map<String, String> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "data=" + data +
                    '}';
        }

        public Map<String, String> getData() {
            return data;
        }
    }
}
