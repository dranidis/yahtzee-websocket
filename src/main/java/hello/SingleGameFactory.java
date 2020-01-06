package hello;

import com.asdt.yahtzee.game.Game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SingleGameFactory {
    private Game game;

    public void addPlayer(String name) {
        if (game == null) {
            game = new Game();
        }
        game.addPlayer(name);
    }

    public GameMessage start() {
        game.startRound();
        System.out.println("Game started with " + game.getPlayers().keySet());

        String currentPlayer = game.getNextPlayer();
        game.rollKeeping(currentPlayer);
        int[] dice = game.getDice();
        Sheet sheet = new Sheet(game.getCurrentPlayer().getScored());
        return new GameMessage(currentPlayer, sheet, dice, 1, "");
    }

    public GameMessage rollKeeping(String currentPlayer, int[] keep) {
        int roll = game.getCurrentPlayer().getRoll();
        if (roll <= 3) {
            game.rollKeeping(currentPlayer, keep);
            int[] dice = game.getDice();
            Sheet sheet = new Sheet(game.getCurrentPlayer().getScored());
            return new GameMessage(currentPlayer, sheet, dice, roll, "");
        }
        // TODO: throw exception?
        return null;
    }

    @Autowired
    private SimpMessagingTemplate messageSender;

    public GameMessage scoreCategory(String playerName, String categoryName) {
        int score = game.scoreACategory(playerName, categoryName);
        Sheet sheet = new Sheet(game.getCurrentPlayer().getScored());

        System.out.println("Sending game to topic/game with messageSender:" + messageSender);
        messageSender.convertAndSend("/topic/game",
                new GameMessage(playerName, sheet, game.getDice(), game.getCurrentPlayer().getRoll(), categoryName, score));

        String currentPlayer = game.getNextPlayer();
        if (currentPlayer == null ) {
            game.startRound();
            currentPlayer = game.getNextPlayer();
        }

        game.rollKeeping(currentPlayer);
        int[] dice = game.getDice();
        sheet = new Sheet(game.getCurrentPlayer().getScored());
        return new GameMessage(currentPlayer, sheet, dice, 1, "");
    }

}
