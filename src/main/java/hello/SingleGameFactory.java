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

}
