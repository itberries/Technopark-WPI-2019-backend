package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;



@JsonSubTypes({@JsonSubTypes.Type(value = TurnMatch.class, name = "turnMatch"),
        @JsonSubTypes.Type(value = TurnChain.class, name = "turnChain"),
        @JsonSubTypes.Type(value = TurnQuestion.class, name = "turnQuestion")})
public abstract class Turn extends Message {
    /**
     * Тип игры (singleplayer, multiplayer)
     */
    private String mode;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Turn() {
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
