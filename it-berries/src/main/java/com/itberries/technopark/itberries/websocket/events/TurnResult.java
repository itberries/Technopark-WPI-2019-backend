package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TurnResult extends Message {
    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;


    @JsonCreator
    public TurnResult(@JsonProperty("payload") Payload payload) {
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

        @JsonCreator
        public Payload(@JsonProperty("data") String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "data='" + data + '\'' +
                    '}';
        }

        public String getData() {
            return data;
        }
    }


//    public static void main(String[] args) {
//        Gson gson = new Gson();
//        TurnResult turnResult = new TurnResult(new TurnResult.Payload("false"), "match", 3L);
//        String jsonInString = gson.toJson(turnResult);
//        System.out.println(jsonInString);
//    }
}
