package hello;

import java.util.concurrent.TimeUnit;

import com.asdt.yahtzee.game.Game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SingleGame {
    private Game game;

    public void addPlayer(String name) {
        if (game == null) {
            game = new Game();
        }
        game.addPlayer(name);
    }

    private void rolling(String currentPlayer, int[] keep) {
        messageSender.convertAndSend("/topic/rolling", new KeepMessage(currentPlayer, keep));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        game.rollKeeping(currentPlayer, keep);

    }

    public GameMessage start() {
        game.startRound();
        System.out.println("Game started with " + game.getPlayers().keySet());

        String currentPlayer = game.getNextPlayer();
        Sheet sheet = new Sheet(game.getCurrentPlayer().getFullScoreSheet());

        messageSender.convertAndSend("/topic/game", new GameMessage(currentPlayer, sheet, game.getDice(),
                game.getCurrentPlayer().getRoll(), ""));

        rolling(currentPlayer, new int[]{});

        int[] dice = game.getDice();
        sheet = new Sheet(game.getCurrentPlayer().getFullScoreSheet());
        return new GameMessage(currentPlayer, sheet, dice, 1, "");
    }

    public GameMessage rollKeeping(String currentPlayer, int[] keep) {
        int roll = game.getCurrentPlayer().getRoll();
        if (roll <= 3) {

            rolling(currentPlayer, keep);

            int[] dice = game.getDice();
            Sheet sheet = new Sheet(game.getCurrentPlayer().getFullScoreSheet());
            return new GameMessage(currentPlayer, sheet, dice, roll, "");
        }
        // TODO: throw exception?
        return null;
    }

    @Autowired
    private SimpMessagingTemplate messageSender;

    public GameMessage scoreCategory(String playerName, String categoryName) {
        int score = game.scoreACategory(playerName, categoryName);
        Sheet sheet = new Sheet(game.getCurrentPlayer().getFullScoreSheet());

        System.out.println("Sending game to topic/game with messageSender:" + messageSender);
        messageSender.convertAndSend("/topic/game", new GameMessage(playerName, sheet, game.getDice(),
                game.getCurrentPlayer().getRoll(), categoryName, score));

        String currentPlayer = game.getNextPlayer();
        if (currentPlayer == null) { // round has ended
            if (game.getRound() < 13) {
                game.startRound();
                currentPlayer = game.getNextPlayer();
            } else { // game has ended
                messageSender.convertAndSend("/topic/end", new StringMessage("END"));
            }
        }

        rolling(currentPlayer, new int[]{});

        int[] dice = game.getDice();
        sheet = new Sheet(game.getCurrentPlayer().getFullScoreSheet());
        return new GameMessage(currentPlayer, sheet, dice, 1, "");
    }

}
