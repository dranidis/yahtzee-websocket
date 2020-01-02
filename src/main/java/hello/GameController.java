package hello;

import java.util.ArrayList;
import java.util.Arrays;
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
		if (game == null) {
			return null;
		}
		System.out.println("New game started with " + game.getPlayers());
		System.out.println("Round starts");
		game.startRound();
	
		// return new Sheet(game.getScored());
		return new GameString(game.toString());
	}

	// public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {

	@RequestMapping("/keep")
	public GameString keep(@RequestParam(value="name") String name, @RequestParam(value="keep") int[] keep) {
		System.out.println(name + " rolls keeping " + new ArrayList<>(Arrays.asList(keep)));
		game.rollKeeping(name, keep);
		return new GameString(game.toString());
	}

	@RequestMapping("/score")
	public Score score(@RequestParam(value="name") String playerName, @RequestParam(value="cat") String categoryName) {
		int score = game.scoreACategory(playerName, categoryName);
		System.out.println(playerName + " scores " + categoryName);
		System.out.println(playerName + " gets " + score + " points");
		game.startRound();
		return new Score(score);
	}


}
