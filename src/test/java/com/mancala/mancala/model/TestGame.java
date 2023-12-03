package com.mancala.mancala.model;

public class TestGame extends Game
{
    public Player getCurrentPlayer()
    {
        return currentPlayer;
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
