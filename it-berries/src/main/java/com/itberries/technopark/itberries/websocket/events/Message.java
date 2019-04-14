package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = Turn.class, name = "turn"),
        @JsonSubTypes.Type(value = JoinGame.class, name = "joinGame"),
        @JsonSubTypes.Type(value = JoinGame.class, name = "deliveryStatus"),
        @JsonSubTypes.Type(value = JoinGame.class, name = "gameCompleted")})
public abstract class Message {

}
