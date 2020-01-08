package com.asdt.yahtzee.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Remembers users connected by their session id
 */

@Component
public class PlayerCatalog {
    private static PlayerCatalog instance = new PlayerCatalog();

    private PlayerCatalog() {
    }

    public static PlayerCatalog getInstance() {
        return instance;
    }

    private Map<String, String> players = new HashMap<>();;

    public void playerConnected(String id) {
        String player = players.get(id);
        if (player == null) {
            System.out.println("New session id:" + id);
            players.put(id, "noname");
        } else {
            System.out.println("User " + player + " connects again!");
        }
    }

	public void updateName(String sessionId, String playerName) {
        players.put(sessionId, playerName);
	}

}