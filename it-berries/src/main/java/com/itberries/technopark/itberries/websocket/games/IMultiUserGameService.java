package com.itberries.technopark.itberries.websocket.games;

import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.websocket.events.DeliveryStatus;
import com.itberries.technopark.itberries.websocket.events.JoinGame;
import com.itberries.technopark.itberries.websocket.events.Turn;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface IMultiUserGameService {
    void joinGame(JoinGame joinGameMessage, WebSocketSession webSocketSession, User user) throws IOException;
    void handleGameTurn(Turn turn, WebSocketSession webSocketSession, User user) throws IOException;
    void startTimer(DeliveryStatus deliveryStatus, WebSocketSession webSocketSession, User user);
}
