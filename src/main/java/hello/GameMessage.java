package hello;

import java.util.Arrays;

public class GameMessage {
    private String currentPlayer;
    private int roll;
    int[] dice;
    private String categoryName;
    private int score;
    private Sheet sheet;

    public GameMessage(String currentPlayer, Sheet sheet, int[] dice, int roll, String categoryName) {
        this.currentPlayer = currentPlayer;
        this.dice = new int[dice.length];
        for (int i = 0; i < dice.length; i++) {
            this.dice[i] = dice[i];
        }
        this.roll = roll;
        this.categoryName = categoryName;
        this.sheet = sheet;
    }

    public GameMessage() {

    }

    public GameMessage(String playerName, Sheet sheet, int[] dice2, int roll2, String categoryName2, int score) {
        this(playerName, sheet, dice2, roll2, categoryName2);
        this.score = score;
        this.sheet = sheet;
	}

	public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String toString() {
        return "GameMessage [categoryName=" + categoryName + ", currentPlayer=" + currentPlayer + ", dice="
                + Arrays.toString(dice) + ", roll=" + roll + ", score=" + score + "]";
    }

    public int[] getDice() {
        return dice;
    }

    public void setDice(int[] dice) {
        this.dice = dice;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

}
