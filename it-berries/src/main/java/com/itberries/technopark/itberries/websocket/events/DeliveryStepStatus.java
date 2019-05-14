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

        private final Boolean right;

        @JsonCreator
        public Payload(@JsonProperty("result") String result,
                       @JsonProperty("right") Boolean right) {
            this.result = result;
            this.right = right;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "result='" + result + '\'' +
                    ", right='" + right + '\'' +
                    '}';
        }

        public Boolean getRight() {
            return right;
        }

        public String getResult() {
            return result;
        }
    }

}
