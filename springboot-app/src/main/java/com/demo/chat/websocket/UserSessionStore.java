package com.demo.chat.websocket;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserSessionStore {

    private final Map<String, List<String>> users = new ConcurrentHashMap<>();

    public String connect(String user) {
        String sessionId = UUID.randomUUID().toString();
        List<String> sessions = users.get(user);
        if (sessions == null) {
            sessions = new ArrayList<>();
            sessions.add(sessionId);
            users.put(user, sessions);
        } else {
            sessions.add(sessionId);
        }
        return sessionId;
    }

    public void disconnect(String sessionId) {
        if (sessionId == null) return;
        for (Map.Entry<String, List<String>> entry : users.entrySet()) {
            List<String> sessions = entry.getValue();
            if (sessions.remove(sessionId)) {
                if (sessions.isEmpty()) {
                    users.remove(entry.getKey());
                }
                break;
            }
        }
    }

    public List<String> getSessions(String user) {
        return users.get(user);
    }
}
