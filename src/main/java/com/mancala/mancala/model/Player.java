package com.mancala.mancala.model;

public enum Player {
    red,
    blue;

    public Player next() {
        // Assuming two players for now; can be updated for more players
        return this == blue ? red : blue;
    }
}
