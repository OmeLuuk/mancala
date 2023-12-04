package com.mancala.mancala.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private TestBoard board;

    @BeforeEach
    void setUp() {
        board = new TestBoard();
    }

    @Test
    void testInitialBoardSetup() {
        assertArrayEquals(board.getPits(), new int[] { 6, 6, 6, 6, 6, 6,   0,   6, 6, 6, 6, 6, 6,   0 });
    }

    @Test
    void testIllegalMoveByBluePlayer() {
        // Assuming blue player's pits are 7 to 13
        boolean anotherTurn = board.makeMove(0, Player.blue); // This should be an illegal move
        assertTrue(anotherTurn, "Blue player should get another turn as nothing happened after an illegal move");
        assertArrayEquals(board.getPits(), new int[] { 6, 6, 6, 6, 6, 6,   0,   6, 6, 6, 6, 6, 6,   0 },
                "The board should remain unchanged upon making an illegal move");
    }

    @Test
    void testMakeMove() {
        // Test a valid move
        boolean anotherTurn = board.makeMove(7, Player.blue);
        assertTrue(anotherTurn, "Player should not get another turn on this move");
        assertArrayEquals(board.getPits(), new int[] { 6, 6, 6, 6, 6, 6,   0,   0, 7, 7, 7, 7, 7,   1},
                "The 6 stones in pit 7 should be spread over the 6 pits after it");
    }

    @Test
    void testMoveResultingInCapture() {
        // Manually setting up a board state for the capture scenario
        int[] customPits = {0, 6, 6, 6, 6, 6,   0,   1, 0, 6, 6, 6, 6,   0};
        board.setPits(customPits); // This method will need to be implemented in your Board class

        // Blue player makes a move from pit 7 which should capture stones from pit 0
        board.makeMove(7, Player.blue);

        // Expected state after capture
        int[] expectedPits = {0, 6, 6, 6, 0, 6,   0,   0, 0, 6, 6, 6, 6,   7};
        assertArrayEquals(board.getPits(), expectedPits,
                "We should capture the single stone and the opponent's stones because the next pit is empty");
    }

    @Test
    void testGameOverCondition() {
        // Set up a board state where all of Red player's pits except one are empty
        int[] customPits = {0, 0, 1, 0, 0, 0, 10, 1, 0, 0, 0, 0, 0, 15};
        board.setPits(customPits);

        // Red player makes a move from the last non-empty pit (pit 7)
        board.makeMove(7, Player.blue); // This move should empty Red player's side

        // Assert that the game is over
        assertTrue(board.isGameOver(), "Game should be over when one side is empty");

        // Expected state after the game-over move
        int[] expectedPits = {0, 0, 1, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 16}; // Assuming stones are collected in the store
        assertArrayEquals(board.getPits(), expectedPits,
                "Board state should match expected state at game over");
    }

    @Test
    void testMultipleTurns() {
        // Setup a board state for testing multiple turns
        int[] customPits = {6, 6, 6, 6, 6, 1, 15, 6, 6, 6, 6, 6, 1, 20};
        board.setPits(customPits);

        // Blue player makes a move from pit 12, ending in their own big pit
        boolean anotherTurn = board.makeMove(12, Player.blue);

        // Assert that the player gets another turn
        assertTrue(anotherTurn, "Player should get another turn if the last stone lands in their big pit");

        // Expected state after the move
        int[] expectedPits = {6, 6, 6, 6, 6, 1, 15, 6, 6, 6, 6, 6, 0, 21}; // Adjust based on your game rules
        assertArrayEquals(board.getPits(), expectedPits,
                "Board state should match expected state after multiple turns move");
    }

    @Test
    void testTryMoveOnEmptyPit() {
        int[] customPits = {0, 0, 1, 0, 0, 0, 10, 1, 0, 0, 0, 0, 0, 15};
        board.setPits(customPits);

        // This move should do nothing since pit 9 is empty
        boolean anotherTurn = board.makeMove(9, Player.blue);

        // Assert that the player gets another turn
        assertTrue(anotherTurn, "Player should get another turn if the selected pit was empty");

        int[] expectedPits = {0, 0, 1, 0, 0, 0, 10, 1, 0, 0, 0, 0, 0, 15};
        assertArrayEquals(board.pits, expectedPits, "The pits should remain unchanged after an empty move");
    }

    @Test
    void landSingleStoneInOtherPlayerEmptyPit()
    {
        int[] customPits = { 7, 7, 6, 6, 6, 6,   0,   0, 0, 8, 8, 8, 8,   2};
        board.setPits(customPits);

        boolean anotherTurn = board.makeMove(2, Player.red);
        assertFalse(anotherTurn, "Player should not get another turn if last stone lands in opponent's pit");

        int[] expectedPits = {7, 7, 0, 7, 7, 7,   1,   1, 1, 8, 8, 8, 8,   2};
        assertArrayEquals(board.pits, expectedPits, "Nothing special should happen in this situation");
    }
}
