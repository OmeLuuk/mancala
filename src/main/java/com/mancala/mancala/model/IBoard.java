package com.mancala.mancala.model;

public interface IBoard {
    public String toHTML(boolean isCurrentPlayer, Player player);
    public boolean makeMove(int pitIndex, Player player);
    public boolean isGameOver();
    public int getStoreCount(Player player);
}
