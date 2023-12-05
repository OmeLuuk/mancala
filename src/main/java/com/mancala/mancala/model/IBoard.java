package com.mancala.mancala.model;

public interface IBoard {
    String toHTML(boolean isCurrentPlayer, Player player);
    boolean makeMove(int pitIndex, Player player);
    boolean isGameOver();
    int getStoreCount(Player player);
}
