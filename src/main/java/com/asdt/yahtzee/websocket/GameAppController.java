package com.asdt.yahtzee.websocket;

import java.util.List;
import java.util.Map;

import com.asdt.yahtzee.websocket.messages.GameResponse;
import com.asdt.yahtzee.websocket.messages.KeepMessage;
import com.asdt.yahtzee.websocket.messages.PlayerListMessage;
import com.asdt.yahtzee.websocket.messages.PlayerMessage;
import com.asdt.yahtzee.websocket.messages.ScoreMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * Handles the application destination prefixes
 * 
 * /app/...
 * 
 */
@Controller
public class GameAppController {

    @Autowired
    SingleGame singleGameFactory;

    /**
     * Method receives a message, handles it and then sends the message to the
     * broker
     * 
     * @param playerMsg
     * @return
     */
    @MessageMapping("/player")
    @SendTo("/topic/players")
    public PlayerListMessage addPlayer(PlayerMessage playerMsg, SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        
        String playerName = playerMsg.getName();
        String sessionId = (String) sessionAttributes.get("SessionId");
        PlayerCatalog.getInstance().updateName(sessionId, playerName);
        
        List<String> players = singleGameFactory.addPlayer(playerName);
        return new PlayerListMessage(players);
    }

    @MessageMapping("/start")
    @SendTo("/topic/game")
    public GameResponse startGame() {
        System.out.println("startGame: ");
        GameResponse gameMsg = singleGameFactory.start();
        return gameMsg;
    }

    @MessageMapping("/roll")
    @SendTo("/topic/game")
    public GameResponse roll(KeepMessage keepMsg) {
        System.out.println("keepMsg: " + keepMsg);
        GameResponse gameMsg = singleGameFactory.rollKeeping(keepMsg);
        return gameMsg;
    }

    @MessageMapping("/score")
    @SendTo("/topic/game")
    public GameResponse roll(ScoreMessage scoreMsg) {
        System.out.println("scoreMsg: " + scoreMsg);
        GameResponse gameMsg = singleGameFactory.scoreCategory(scoreMsg.getName(), scoreMsg.getCategory());
        return gameMsg;
    }

}