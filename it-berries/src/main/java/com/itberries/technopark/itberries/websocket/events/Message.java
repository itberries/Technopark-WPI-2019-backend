package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = Turn.class, name = "turn"),
        @JsonSubTypes.Type(value = JoinGame.class, name = "joinGame"),
        @JsonSubTypes.Type(value = TurnResultMP.class, name = "turnResultMP"),
        @JsonSubTypes.Type(value = DeliveryStatus.class, name = "deliveryStatus"),
        @JsonSubTypes.Type(value = DeliveryStepStatus.class, name = "deliveryStepStatus"),
        @JsonSubTypes.Type(value = RecoveryGame.class, name = "recoveryGame"),
        @JsonSubTypes.Type(value = JoinGame.class, name = "gameCompleted"),
        @JsonSubTypes.Type(value = JoinGame.class, name = "MPStartGameMessage")})
public abstract class Message {

}
