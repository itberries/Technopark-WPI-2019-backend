package com.itberries.technopark.itberries.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.websocket.events.*;
import com.itberries.technopark.itberries.websocket.games.IGamePlayService;
import com.itberries.technopark.itberries.websocket.games.IMultiUserGameService;
import com.itberries.technopark.itberries.websocket.games.models.WebSocketData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Основной компонент для установки соединения
 * и перехвата сообщений по web сокетам
 */
@Component
public class SocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);
    private final String GAME_MODE_SINGLEPLAYER = "singleplayer";
    private final String GAME_MODE_MULTIPLAYER = "nultiplayer";

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private Map<User, WebSocketData> sessionData = new HashMap<>();
    private final ObjectMapper objectMapper;
    private final Gson gson = new Gson();
    private IGamePlayService IGamePlayService;
    private IMultiUserGameService iMultiUserGameService;

    @Autowired
    public SocketHandler(ObjectMapper objectMapper,
                         IGamePlayService IGamePlayService,
                         IMultiUserGameService iMultiUserGameService) {
        this.objectMapper = objectMapper;
        this.IGamePlayService = IGamePlayService;
        this.iMultiUserGameService = iMultiUserGameService;
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        User user = (User) session.getAttributes().get("user");
        final Long userId = user.getId();
        try {
            Message message = objectMapper.readValue(jsonTextMessage.getPayload(), Message.class);


            if (message.getClass() == JoinGame.class) {
                //проверяем какой mode игры
                JoinGame joinGame = (JoinGame) message;
                if (GAME_MODE_SINGLEPLAYER.equals(joinGame.getMode())) {
                    IGamePlayService.joinGame(joinGame, session, user);
                    LOGGER.info("JoinGame message for SINGLEPLAYER received");
                } else {
                    iMultiUserGameService.joinGame(joinGame, session, user);
                    LOGGER.info("JoinGame message for MULTIPLAYER received");
                }
            } else if (message.getClass() == Turn.class) {
                Turn turn = (Turn) message;
                if (GAME_MODE_SINGLEPLAYER.equals(turn.getMode())) {
                    IGamePlayService.handleGameTurn(turn, session, user);
                    LOGGER.info("Turn message for SINGLEPLAYER received");
                } else {
                    iMultiUserGameService.handleGameTurn(turn, session, user);
                    LOGGER.info("Turn message for MULTIPLAYER received");
                }
            } else if (message.getClass() == DeliveryStatus.class) {
                iMultiUserGameService.startTimer((DeliveryStatus) message, session, user);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            LOGGER.error("wrong json format response", ex);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = (User) session.getAttributes().get("user");
        LOGGER.info("Connected user with id  " + user.getId());
        sessionData.put(user, new WebSocketData(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user = (User) session.getAttributes().get("user");

        if ("singleplayer".equals(sessionData.get(user).getMode())) {
            IGamePlayService.clearStateAfterCompletedGame(user);
        }
        LOGGER.info("Disconnected user with id  " + user.getId());
    }

    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        LOGGER.warn("Transportation problem handle", throwable);
        User user = (User) session.getAttributes().get("user");
        if ("singleplayer".equals(sessionData.get(user).getMode())) {
            IGamePlayService.clearStateAfterCompletedGame(user);
        }
    }
}
