package com.itberries.technopark.itberries.websocket.games.models;

import com.itberries.technopark.itberries.websocket.events.Turn;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameSession {
    private WebSocketSession webSocketSession;
    private LocalDateTime localDateTime;
    private boolean gameCompleted;
    private  List<Turn> turns = new ArrayList<>();

    public GameSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public GameSession(WebSocketSession webSocketSession, LocalDateTime localDateTime) {
        this.webSocketSession = webSocketSession;
        this.localDateTime = localDateTime;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }

    public void setGameCompleted(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
