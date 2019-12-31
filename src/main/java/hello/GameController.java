package hello;

import java.util.concurrent.atomic.AtomicLong;

import com.asdt.yahtzee.game.Game;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	Game game;

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
		return new Greeting(counter.incrementAndGet(),
							String.format(template, name));
	}

	@RequestMapping("/game")
	public void game() {
		System.out.println("New game created");
		game = new Game();
	}

	@RequestMapping("/addplayer")
	public void addPlayer(@RequestParam(value="name") String name) {
		System.out.println("New player added " + name);
		game.addPlayer(name);;
	}

	@RequestMapping("/start")
	public GameString start() {
		System.out.println("New game created");
		System.out.println("Round starts");
		game.startRound();
		String playerName = game.getCurrentPlayersName();
		System.out.println("1st player rolls");
		game.rollKeeping(playerName);
		System.out.println("Round starts");
		return new GameString(game.toString());
	}

	@RequestMapping("/keep")
	public GameString keep(@RequestParam(value="name") String name, @RequestParam(value="keep") int[] keep) {
		game.rollKeeping(name, keep);
		return new GameString(game.toString());
	}

	@RequestMapping("/score")
	public GameString score(@RequestParam(value="name") String playerName, @RequestParam(value="cat") String categoryName) {
		game.scoreACategory(playerName, categoryName);
		return new GameString(game.toString());
	}


}
