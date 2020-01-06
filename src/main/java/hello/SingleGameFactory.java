package hello;

import com.asdt.yahtzee.game.Game;

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
        String currentPlayer = game.getNextPlayer();
        game.rollKeeping(currentPlayer);
        int[] dice = game.getDice();
        return new GameMessage(currentPlayer, dice);
	}

	public GameMessage rollKeeping(String currentPlayer, int[] keep) {
        game.rollKeeping(currentPlayer, keep);
        int[] dice = game.getDice();
        return new GameMessage(currentPlayer, dice);
	}

}
