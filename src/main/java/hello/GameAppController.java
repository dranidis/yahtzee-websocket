package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    SingleGameFactory singleGameFactory;

    /**
     * Method receives a message, handles it and
     * then sends the message to the broker
     * @param playerMsg
     * @return
     */
    @MessageMapping("/player")
    @SendTo("/topic/players")
    public PlayerMessage addPlayer(PlayerMessage playerMsg) {
        System.out.println("addPlayer : " + playerMsg);
        singleGameFactory.addPlayer(playerMsg.getName());
        return playerMsg;
    }

    @MessageMapping("/start")
    @SendTo("/topic/game")
    public GameMessage startGame() {
        System.out.println("startGame: ");
        GameMessage gameMsg = singleGameFactory.start();
        return gameMsg;
    }

    @MessageMapping("/roll")
    @SendTo("/topic/game")
    public GameMessage roll(KeepMessage keepMsg) {
        System.out.println("keepMsg: " + keepMsg);
        GameMessage gameMsg = singleGameFactory.rollKeeping(keepMsg.getName(), keepMsg.getKeep());
        return gameMsg;
    }

}