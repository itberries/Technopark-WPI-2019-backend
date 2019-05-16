package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliveryStepStatus extends Message {
    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;

    @JsonCreator
    public DeliveryStepStatus(@JsonProperty("payload") Payload payload) {
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

        private final String data;

        @JsonCreator
        public Payload(@JsonProperty("result") String result,
                       @JsonProperty("data") String data) {
            this.result = result;
            this.data = data;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "result='" + result + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }

        public String getData() {
            return data;
        }

        public String getResult() {
            return result;
        }
    }

}
