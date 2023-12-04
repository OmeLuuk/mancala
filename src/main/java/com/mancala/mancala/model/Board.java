package com.mancala.mancala.model;

import org.springframework.web.bind.annotation.PathVariable;

public class Board implements IBoard {
    private final int boardSize = 14; // 6 pits per player and one score pit
    private final int startStones = 6;
    protected int[] pits;

    public Board() {
        pits = new int[boardSize];

        for (int i = 0; i < boardSize; i++) {
            pits[i] = startStones;
        }
        // Larger pits (stores) start empty
        pits[boardSize / 2 - 1] = 0; // Player 1's store
        pits[boardSize - 1] = 0; // Player 2's store
    }

    public String toHTML(boolean isCurrentPlayer, Player player) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Mancala Game</title></head><body>");
        html.append("<h1>Current Game State</h1><table><tr>");

        // Display player 1's larger pit
        html.append("<td style='width: 50px; text-align: center;'><span style='color: blue;'>")
                .append(pits[boardSize - 1]).append("</span></td>");

        // Display player 1's pits
        for (int i = boardSize - 2; i >= boardSize / 2; i--) {
            html.append("<td style='width: 50px; text-align: center;'>");
            if (isCurrentPlayer && player == Player.blue) {
                html.append("<a href='/move/").append(i).append("' style='color: blue;'>");
            }
            html.append("<button style='background:#3dd5e9;color:black;'>").append(pits[i]).append("</button>");
            if (isCurrentPlayer && player == Player.blue) {
                html.append("</a>");
            }
            html.append("</td>");
        }

        // Display player 2's pits
        html.append("</tr><tr><td></td>"); // Skip the first cell for alignment
        for (int i = 0; i < boardSize / 2 - 1; i++) {
            html.append("<td style='width: 50px; text-align: center;'>");
            if (isCurrentPlayer && player == Player.red) {
                html.append("<a href='/move/").append(i).append("' style='color: red;'>");
            }
            html.append("<button style='background:#f1807e;color:black;'>").append(pits[i]).append("</button>");
            if (isCurrentPlayer && player == Player.red) {
                html.append("</a>");
            }
            html.append("</td>");
        }

        // Display player 2's larger pit
        html.append("<td style='width: 50px; text-align: center;'><span style='color: red;'>")
                .append(pits[boardSize / 2 - 1]).append("</span></td>");

        html.append("</tr></table></body></html>");
        return html.toString();
    }

    // This method is designed for only two players currently.
    public boolean makeMove(int pitIndex, Player player) {
        boolean blueTurn = player == Player.blue;

        boolean triedInvalidMove =
                blueTurn && pitIndex < boardSize / 2 ||
                !blueTurn && pitIndex >= boardSize / 2 - 1 ||
                pits[pitIndex] == 0;
        if (triedInvalidMove)
        {
            return true; // Same player's turn again since the given move was not made
        }

        int stones = pits[pitIndex];
        pits[pitIndex] = 0;
        int lastPitIndex = 0;

        for (int i = 1; i <= stones; i++)
        {
            lastPitIndex = (pitIndex + i) % pits.length;
            // Skip opponent's big pit
            if ((blueTurn && lastPitIndex == boardSize / 2 - 1) ||
                    (!blueTurn && lastPitIndex == boardSize - 1)) {
                stones++; // Compensate for the skipped pit
                continue;
            }
            pits[lastPitIndex]++;
        }

        // Determine the index for the player's big pit
        int bigPitIndex = blueTurn ? boardSize - 1 : boardSize / 2 - 1;

        // Capturing logic
        if (lastPitIndex != bigPitIndex && pits[lastPitIndex] == 1)
        {
            // Check if the last pit is on the player's side and was empty before the move
            if ((blueTurn && lastPitIndex >= boardSize / 2) || (!blueTurn && lastPitIndex < boardSize / 2 - 1))
            {
                int oppositePitIndex = boardSize - 2 - lastPitIndex;
                int capturedStones = pits[oppositePitIndex] + 1;  // Capture opposite stones plus the one just placed
                pits[oppositePitIndex] = 0;  // Empty the opposite pit
                pits[lastPitIndex] = 0;  // Empty the last pit
                pits[bigPitIndex] += capturedStones;  // Add to the player's store
            }
        }

        // Player gets another turn if the last stone lands in their big pit
        return lastPitIndex == bigPitIndex;
    }

    public boolean isGameOver()
    {
        // Here we assume that we always play with the maximum number of players. If we want to allow games with fewer
        // players we will need to store the number of players and loop until that number instead.
        if (boardSize / 2 * Player.values().length != pits.length) {
            throw new IllegalArgumentException("The number of players does not match the board's capacity!");
        }
        for (int player = 0; player < Player.values().length; player++)
        {
            boolean pitsEmpty = true;
            for (int pit = boardSize / 2 * player; pit < boardSize / 2 * (player + 1) - 1; pit++)
            {
                if (pits[pit] != 0)
                {
                    pitsEmpty = false;
                    break;
                }
            }
            if (pitsEmpty)
            {
                return true;
            }
        }

        return false;
    }

    public int getStoreCount(Player player) {
        // Calculate the index of the store pit based on the player's ordinal
        // This assumes a specific ordering and spacing in the pits array
        int storeIndex = boardSize / 2 - 1 + (boardSize / 2 * player.ordinal());
        if (storeIndex < pits.length)
        {
            return pits[storeIndex];
        }
        else
        {
            throw new IllegalArgumentException("Invalid player: " + player);
        }
    }
}
