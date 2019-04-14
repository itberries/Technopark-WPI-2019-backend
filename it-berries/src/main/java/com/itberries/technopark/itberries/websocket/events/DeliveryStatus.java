package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliveryStatus extends Message {
    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;

    @JsonCreator
    public DeliveryStatus(@JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }


    public Payload getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "DeliveryStatus{" +
                "payload=" + payload +
                '}';
    }

    public static class Payload {


        private final String result;

        @JsonCreator
        public Payload(@JsonProperty("result") String result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "result='" + result + '\'' +
                    '}';
        }

        public String getResult() {
            return result;
        }
    }
}
