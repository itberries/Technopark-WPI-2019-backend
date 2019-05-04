package com.itberries.technopark.itberries.websocket.games.models;

import org.springframework.web.socket.WebSocketSession;

public class WebSocketData {
    private String mode;
    private WebSocketSession webSocketSession;

    public WebSocketData(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }
}
