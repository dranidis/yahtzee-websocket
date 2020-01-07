package com.asdt.yahtzee.websocket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.asdt.yahtzee.game.Game;
import com.asdt.yahtzee.websocket.messages.GameResponse;
import com.asdt.yahtzee.websocket.messages.KeepMessage;
import com.asdt.yahtzee.websocket.messages.SheetSubResponse;
import com.asdt.yahtzee.websocket.messages.StringMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SingleGame {
    private Game game;
    int msDelay = 500; // TODO get from UI

    Set<String> players = new HashSet<>();

    public List<String> addPlayer(String name) {
        players.add(name);
        return new ArrayList<>(players);
    }

    private void rolling(String currentPlayer, int[] keep, int msDelay) {
        messageSender.convertAndSend("/topic/rolling", new KeepMessage(currentPlayer, keep, msDelay));
        try {
            TimeUnit.MILLISECONDS.sleep(msDelay); // for the animation in the UI
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        game.rollKeeping(currentPlayer, keep);
    }

    public GameResponse start() {
        game = new Game();
        for (String player : players)
            game.addPlayer(player);

        game.startRound();
        System.out.println("Game started with " + game.getPlayers().keySet());

        String currentPlayer = game.getNextPlayer();
        SheetSubResponse sheet = new SheetSubResponse(game.getCurrentPlayer().getFullScoreSheet());

        messageSender.convertAndSend("/topic/game", new GameResponse(game.getRound(), currentPlayer, sheet,
                game.getDice(), game.getCurrentPlayer().getRoll(), "", 0));

        rolling(currentPlayer, new int[] {}, msDelay);

        int[] dice = game.getDice();
        sheet = new SheetSubResponse(game.getCurrentPlayer().getFullScoreSheet());
        return new GameResponse(game.getRound(), currentPlayer, sheet, dice, 1, "", 0);
    }

    public GameResponse rollKeeping(KeepMessage keepMsg) {
        msDelay = keepMsg.getMsDelay();
        int roll = game.getCurrentPlayer().getRoll();
        if (roll <= 3) {

            rolling(keepMsg.getName(), keepMsg.getKeep(), msDelay);

            int[] dice = game.getDice();
            SheetSubResponse sheet = new SheetSubResponse(game.getCurrentPlayer().getFullScoreSheet());
            return new GameResponse(game.getRound(), keepMsg.getName(), sheet, dice, roll, "", 0);
        }
        return null;
    }

    @Autowired
    private SimpMessagingTemplate messageSender;

    public GameResponse scoreCategory(String playerName, String categoryName) {
        int score = game.scoreACategory(playerName, categoryName);
        SheetSubResponse sheet = new SheetSubResponse(game.getCurrentPlayer().getFullScoreSheet());

        GameResponse msg = new GameResponse(game.getRound(), playerName, sheet, game.getDice(),
                game.getCurrentPlayer().getRoll(), categoryName, score);

        messageSender.convertAndSend("/topic/game", msg);

        String currentPlayer = game.getNextPlayer();
        if (currentPlayer == null) { // round has ended
            if (game.getRound() < 13) {
                game.startRound();
                currentPlayer = game.getNextPlayer();
            } else { // game has ended
                messageSender.convertAndSend("/topic/end", new StringMessage("END"));
                return null; // will be ignored
            }
        }

        rolling(currentPlayer, new int[] {}, msDelay);

        int[] dice = game.getDice();
        sheet = new SheetSubResponse(game.getCurrentPlayer().getFullScoreSheet());
        return new GameResponse(game.getRound(), currentPlayer, sheet, dice, 1, "", 0);
    }

}
