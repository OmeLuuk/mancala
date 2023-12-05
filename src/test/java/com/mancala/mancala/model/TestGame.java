package com.mancala.mancala.model;

public class TestGame extends Game
{
    public Player assignPlayer() {
        if (!isBluePlayerAssigned) {
            isBluePlayerAssigned = true;
            return Player.blue;
        }
        else if (!isRedPlayerAssigned) {
            isRedPlayerAssigned = true;
            return Player.red;
        }
        // Additional logic if needed for more players or handling errors
        return null;
    }

    public void setBoard(Board board)
    {
        this.board = board;
    }

    public Player[] getWinners()
    {
        return winners;
    }
}
