package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Turn extends Message {

    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;

    @JsonCreator
    public Turn(@JsonProperty("payload") Payload payload) {
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


//    public static void main(String[] args) throws IOException {
//        Gson gson = new Gson();
//        ObjectMapper objectMapper = new ObjectMapper();
//        Turn turn = new Turn(new Turn.Payload(
//                ImmutableMap.of("1 байт", "8 бит"))
//                , "match", 3L);
//
//
//       System.out.println(gson.toJson(turn));
//
//        String jsonInString = "{\n" +
//                "  \"type\": \"turn\",\n" +
//                "  \"payload\": {\n" +
//                "    \"data\": {\n" +
//                "      \"1 байт\": \"8 бит\"\n" +
//                "    }\n" +
//                "  },\n" +
//                "  \"gameType\": \"match\",\n" +
//                "  \"stepId\": 3\n" +
//                "}";
//        System.out.println(jsonInString);
//        Message message = objectMapper.readValue(jsonInString, Message.class);
//
//    }
}
