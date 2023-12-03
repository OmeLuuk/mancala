package com.mancala.mancala.model;

public interface IGame {
    public String getHTML(Player player);
    public Player assignPlayer();
    public void determineWinners();
    public boolean isPlayerTurn(Player player);
    public void makeMove(int pitIndex, Player player);
    public boolean isGameOver();
}
