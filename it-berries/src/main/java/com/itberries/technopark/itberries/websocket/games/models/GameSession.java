package com.itberries.technopark.itberries.websocket.games.models;

import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;

public class GameSession {
    private WebSocketSession webSocketSession;
    private LocalDateTime localDateTime;
    private boolean gameCompleted;

    public GameSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public GameSession(WebSocketSession webSocketSession, LocalDateTime localDateTime) {
        this.webSocketSession = webSocketSession;
        this.localDateTime = localDateTime;
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
