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
        assertArrayEquals(new Player[]{Player.red}, game.getWinners(), "Red should be the winner after the game over");
    }

    @Test
    void testManyConsecutiveMoves() {
        testBoard.setPits(new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0});

        game.makeMove(7, Player.blue);
        assertArrayEquals(new int[]{6, 6, 6, 6, 6, 6, 0, 0, 7, 7, 7, 7, 7, 1}, testBoard.getPits());

        game.makeMove(8, Player.blue);
        assertArrayEquals(new int[]{7, 7, 6, 6, 6, 6, 0, 0, 0, 8, 8, 8, 8, 2}, testBoard.getPits());

        game.makeMove(0, Player.red);
        assertArrayEquals(new int[]{0, 8, 7, 7, 7, 7, 1, 1, 0, 8, 8, 8, 8, 2}, testBoard.getPits());

        game.makeMove(7, Player.blue);
        assertArrayEquals(new int[]{0, 8, 7, 7, 0, 7, 1, 0, 0, 8, 8, 8, 8, 10}, testBoard.getPits());

        game.makeMove(3, Player.red);
        assertArrayEquals(new int[]{0, 8, 7, 0, 1, 8, 2, 1, 1, 9, 9, 8, 8, 10}, testBoard.getPits());

        game.makeMove(11, Player.blue);
        assertArrayEquals(new int[]{1, 9, 8, 1, 2, 9, 2, 1, 1, 9, 9, 0, 9, 11}, testBoard.getPits());

        game.makeMove(4, Player.red);
        assertArrayEquals(new int[]{1, 9, 8, 1, 0, 10, 3, 1, 1, 9, 9, 0, 9, 11}, testBoard.getPits());

        game.makeMove(3, Player.red);
        assertArrayEquals(new int[]{1, 9, 8, 0, 0, 10, 5, 1, 0, 9, 9, 0, 9, 11}, testBoard.getPits());

        game.makeMove(7, Player.blue);
        assertArrayEquals(new int[]{1, 9, 8, 0, 0, 10, 5, 0, 0, 9, 9, 0, 9, 12}, testBoard.getPits());

        game.makeMove(2, Player.red);
        assertArrayEquals(new int[]{1, 9, 0, 1, 1, 11, 6, 1, 1, 10, 10, 0, 9, 12}, testBoard.getPits());

        game.makeMove(9, Player.blue);
        assertArrayEquals(new int[]{2, 10, 1, 2, 2, 12, 6, 1, 1, 0, 11, 1, 10, 13}, testBoard.getPits());

        game.makeMove(4, Player.red);
        assertArrayEquals(new int[]{2, 10, 1, 2, 0, 13, 7, 1, 1, 0, 11, 1, 10, 13}, testBoard.getPits());

        game.makeMove(1, Player.red);
        assertArrayEquals(new int[]{2, 0, 2, 3, 1, 14, 8, 2, 2, 1, 12, 2, 10, 13}, testBoard.getPits());

        game.makeMove(7, Player.blue);
        assertArrayEquals(new int[]{2, 0, 2, 3, 1, 14, 8, 0, 3, 2, 12, 2, 10, 13}, testBoard.getPits());
    }
}