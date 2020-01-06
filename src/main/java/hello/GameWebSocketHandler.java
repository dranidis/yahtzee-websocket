package hello;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.asdt.yahtzee.game.Game;
import com.asdt.yahtzee.game.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class GameWebSocketHandler extends TextWebSocketHandler {

    Game game = new Game();

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        String joined = String.join(",", game.getPlayers().keySet());
        broadcast(new TextMessage(joined));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        ObjectMapper objectMapper = new ObjectMapper();

        // convert json file to map
        Map<?, ?> map = objectMapper.readValue(payload, Map.class);

        // iterate over map entries and print to console
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        String command = (String) map.get("command");
        switch (command) {
        case "addplayer":
            addPlayer((String) map.get("player"));
            break;
        default:
            System.out.println("Unknown command:" + command);
        }

    }

    private void addPlayer(String string) {

        game.addPlayer(string);

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Player> players = game.getPlayers();
        try {
            String payload = objectMapper.writeValueAsString(players);
            broadcast(new TextMessage(payload));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // String joined = String.join(",", game.getPlayers().keySet());
    }

    protected void broadcast(TextMessage message) {
        System.out.println("Broadcasting: " + message);
        for (WebSocketSession s : sessions) {
            try {
                s.sendMessage(message);
            } catch (IOException e) {
                System.out.println("Cannot send message:" + message);
                // e.printStackTrace();
            }
        }

    }

}
