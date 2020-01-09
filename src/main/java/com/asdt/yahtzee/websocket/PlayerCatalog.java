package com.asdt.yahtzee.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asdt.yahtzee.websocket.messages.PlayerListMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Remembers users connected by their session id
 */

@Component
public class PlayerCatalog {

    @Autowired
    private SimpMessagingTemplate messageSender;
    
    private static PlayerCatalog instance = new PlayerCatalog();

    private PlayerCatalog() {
    }

    public static PlayerCatalog getInstance() {
        return instance;
    }

    // HTTP sessionId, name
    private Map<String, String> connectedPlayers = new HashMap<>();;

    public void playerConnected(String id) {
        // String player = connectedPlayers.get(id);
        // if (player == null) {
        //     System.out.println("New session id:" + id);
        //     connectedPlayers.put(id, "noname");
        // } else {
        //     System.out.println("User " + player + " connects again!");
        // }
    }

	public void updateName(String sessionId, String playerName) {
        connectedPlayers.put(sessionId, playerName);
	}

	public void disconnected(String httpSessionId) {
        connectedPlayers.remove(httpSessionId);
        messageSender.convertAndSend("/topic//players", new PlayerListMessage(getListofNames()));
	}

	public List<String> getListofNames() {
		return new ArrayList<String>(connectedPlayers.values());
	}

	public void print() {
        System.out.println(connectedPlayers.entrySet().toString());
	}

}