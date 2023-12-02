package com.mancala.mancala.model;

public enum Player {
    blue,
    red;

    public Player next() {
        // Assuming two players for now; can be updated for more players
        return this == blue ? red : blue;
    }
}
