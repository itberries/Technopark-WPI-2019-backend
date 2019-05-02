package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TurnChain extends Turn {


    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;




    @JsonCreator
    public TurnChain(@JsonProperty("payload") Payload payload) {
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


        private final List<String> data;

        @JsonCreator
        public Payload(@JsonProperty("data") List<String> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "data=" + data +
                    '}';
        }

        public List<String> getData() {
            return data;
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        data.add("http");
        data.add("://");
        data.add("circ");
        data.add(".org");
        data.add("/");
        data.add("slon");
        data.add(".txt");

        TurnChain turnChain = new TurnChain(new TurnChain.Payload(data));
        Gson gson = new Gson();
        String s = gson.toJson(turnChain);
        System.out.println(s);
    }
}
