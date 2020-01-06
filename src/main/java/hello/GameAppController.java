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

    @MessageMapping("/player")
    @SendTo("/topic/players")
    public void addPlayer(PlayerMessage playerMsg) {
        singleGameFactory.addPlayer(playerMsg.getName());
    }

}