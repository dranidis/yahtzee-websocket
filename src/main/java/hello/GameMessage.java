package hello;

import java.util.Arrays;

public class GameMessage {
    private String currentPlayer;
    int[] dice;

    public GameMessage(String currentPlayer, int[] dice) {
        this.currentPlayer = currentPlayer;
        this.dice = new int[dice.length];
        for (int i = 0; i < dice.length; i++) {
            this.dice[i] = dice[i];
        }
    }

    public GameMessage() {

    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String toString() {
        return "GameMessage [currentPlayer=" + currentPlayer + ", dice=" + Arrays.toString(dice) + "]";
    }

    public int[] getDice() {
        return dice;
    }

    public void setDice(int[] dice) {
        this.dice = dice;
    }

}
