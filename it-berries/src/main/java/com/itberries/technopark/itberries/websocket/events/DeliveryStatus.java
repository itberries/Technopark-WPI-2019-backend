package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

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

    public static void main(String[] args) {
        DeliveryStatus deliveryStatus = new DeliveryStatus( new DeliveryStatus.Payload("READY_TO_START_MP_GAME"));
        Gson gson = new Gson();
        System.out.println(gson.toJson(deliveryStatus));
    }
}
