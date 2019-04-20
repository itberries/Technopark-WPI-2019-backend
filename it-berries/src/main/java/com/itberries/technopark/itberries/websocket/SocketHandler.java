package com.itberries.technopark.itberries.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.websocket.events.*;
import com.itberries.technopark.itberries.websocket.games.IGamePlayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Основной компонент для установки соединения
 * и перехвата сообщений по web сокетам
 */
@Component
public class SocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;
    private final Gson gson = new Gson();
    private IGamePlayService IGamePlayService;

    @Autowired
    public SocketHandler(ObjectMapper objectMapper, IGamePlayService IGamePlayService) {
        this.objectMapper = objectMapper;
        this.IGamePlayService = IGamePlayService;
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        User user = (User) session.getAttributes().get("user");
        final Long userId = user.getId();
        try {
            Message message = objectMapper.readValue(jsonTextMessage.getPayload(), Message.class);

            if (message.getClass() == TurnChain.class || message.getClass() == TurnMatch.class) {
                IGamePlayService.handleGameTurn((Turn) message, session, user);
                System.out.println("turn message recived");
            } else if (message.getClass() == JoinGame.class) {
                IGamePlayService.joinGame((JoinGame) message, session, user);
                System.out.println("join game message recived");
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            LOGGER.error("wrong json format response", ex);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.
        User user = (User) session.getAttributes().get("user");
        // service.registerUser(user.getId(), session);
        LOGGER.info("Connected user with id  " + user.getId());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user = (User) session.getAttributes().get("user");
        IGamePlayService.clearStateAfterCompletedGame(user);
        LOGGER.info("Disconnected user with id  " + user.getId());
    }

    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        LOGGER.warn("Transportation problem", throwable);

        User user = (User) session.getAttributes().get("user");
        IGamePlayService.clearStateAfterCompletedGame(user);
    }
}
