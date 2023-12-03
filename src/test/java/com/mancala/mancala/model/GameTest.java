package com.mancala.mancala.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private TestGame game;
    private TestBoard testBoard;

    @BeforeEach
    void setUp() {
        game = new TestGame();
        testBoard = new TestBoard();
        game.setBoard(testBoard);
    }

    @Test
    void testGameInitialization() {
        assertEquals(Player.blue, game.getCurrentPlayer(), "Initial player should be Blue");
        assertFalse(game.isGameOver(), "Game should not be over at start");
        assertArrayEquals(new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0}, testBoard.getPits(),
                "Initial board setup should have 6 stones in each pit except stores");
    }

    @Test
    void testPlayerAssignment() {
        Player firstPlayer = game.assignPlayer();
        assertEquals(Player.blue, firstPlayer, "First player should be Blue");

        Player secondPlayer = game.assignPlayer();
        assertEquals(Player.red, secondPlayer, "Second player should be Red");
    }

    @Test
    void testGameFlow() {
        game.makeMove(7, Player.blue);
        assertEquals(Player.blue, game.getCurrentPlayer(),
                "It should be Blue's turn after Blue's final stone lands in his collection pit");

        game.makeMove(8, Player.blue);
        assertEquals(Player.red, game.getCurrentPlayer(), "It should be Red's turn after Blue");
    }

    @Test
    void testGameOverScenario() {
        // Set up a near endgame scenario: Red player's side is almost empty
        testBoard.setPits(new int[]{0, 0, 0, 0, 0, 1, 30, 1, 0, 0, 0, 0, 0, 25});

        // Blue player makes a move from pit 7, which should empty the last pit on Red's side
        game.makeMove(7, Player.blue);
        assertTrue(game.isGameOver(), "Game should be over after the last move");

        // Assuming Blue is the winner
        assertArrayEquals(new Player[]{Player.blue}, game.getWinners(), "Blue should be the winner after the game over");
    }
}
