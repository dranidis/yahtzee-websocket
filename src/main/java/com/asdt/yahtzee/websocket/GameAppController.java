package com.asdt.yahtzee.websocket;

import java.util.List;
import java.util.Map;

import com.asdt.yahtzee.game.InvalidScoringCategory;
import com.asdt.yahtzee.game.UnknownScoringCategory;
import com.asdt.yahtzee.websocket.messages.GameResponse;
import com.asdt.yahtzee.websocket.messages.KeepMessage;
import com.asdt.yahtzee.websocket.messages.PlayerListMessage;
import com.asdt.yahtzee.websocket.messages.PlayerMessage;
import com.asdt.yahtzee.websocket.messages.ScoreMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
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

    @Autowired
    private SimpMessagingTemplate messageSender;

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
        System.out.println("Session attributes: " + sessionAttributes.entrySet().toString());

        String playerName = playerMsg.getName();
        String sessionId = (String) sessionAttributes.get("sessionId");

        System.out.println("GameAppController:addPlayer Session id: " + sessionId);
        PlayerCatalog.getInstance().updateName(sessionId, playerName);

        // the name might have changed
        playerName = PlayerCatalog.getInstance().getName(sessionId);

        messageSender.convertAndSend("/topic/name" + headerAccessor.getSessionId(), playerName);


        // List<String> players = singleGameFactory.addPlayer(playerName);
        List<String> players = PlayerCatalog.getInstance().getListofNames();

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
    public GameResponse roll(ScoreMessage scoreMsg) throws UnknownScoringCategory, InvalidScoringCategory {
        System.out.println("scoreMsg: " + scoreMsg);
        GameResponse gameMsg = singleGameFactory.scoreCategory(scoreMsg.getName(), scoreMsg.getCategory());
        return gameMsg;
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors") // sent to /user/{sessionid}/queue/errors. Client subscribes to /user/queue/errors
    public String handleException(Throwable exception) {
        System.out.println("Exception thrown: " + exception.getMessage());
        return exception.getMessage();
    }

}