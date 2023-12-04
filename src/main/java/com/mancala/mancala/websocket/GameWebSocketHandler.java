package com.mancala.mancala.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public GameWebSocketHandler() {
        System.out.println("GameWebSocketHandler instantiated");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String httpSessionId = extractHttpSessionId(session);
        if (httpSessionId != null) {
            sessions.put(httpSessionId, session);
            System.out.println("WebSocket Session Established with HTTP Session ID: " + httpSessionId);
        }
    }

    private String extractHttpSessionId(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri != null && uri.getQuery() != null) {
            String[] params = uri.getQuery().split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if ("sessionId".equals(keyValue[0]) && keyValue.length > 1) {
                    return keyValue[1];
                }
            }
        }
        System.out.println("Failed to extract session ID");
        return null;
    }

    public void notifyClient(String sessionId) throws IOException {
        WebSocketSession wsSession = sessions.get(sessionId);
        if (wsSession != null && wsSession.isOpen()) {
            wsSession.sendMessage(new TextMessage("update"));
        } else if (wsSession == null) {
            System.out.println("session was null, id: " + sessionId);
            printAllSessions();
        } else
        {
            System.out.println("session was not open, id: " + sessionId);
            printAllSessions();
        }
    }

    private void printAllSessions() {
        System.out.println("Current WebSocket Sessions:");
        sessions.forEach((key, value) -> System.out.println("Session ID: " + key + ", Is Open: " + value.isOpen()));
    }
}

