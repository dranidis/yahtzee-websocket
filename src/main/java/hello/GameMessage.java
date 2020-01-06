package hello;

import java.util.Arrays;

public class GameMessage {
    private String currentPlayer;
    private int roll;
    int[] dice;
    private String categoryName;
    private int score;

    public GameMessage(String currentPlayer, int[] dice, int roll, String categoryName) {
        this.currentPlayer = currentPlayer;
        this.dice = new int[dice.length];
        for (int i = 0; i < dice.length; i++) {
            this.dice[i] = dice[i];
        }
        this.roll = roll;
        this.categoryName = categoryName;
    }

    public GameMessage() {

    }

    public GameMessage(String playerName, int[] dice2, int roll2, String categoryName2, int score) {
        this(playerName, dice2, roll2, categoryName2);
        this.score = score;
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

}
