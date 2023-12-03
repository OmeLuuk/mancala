package com.mancala.mancala.model;

import org.springframework.web.bind.annotation.PathVariable;

public class Board {
    private int[] pits = new int[14]; // 12 small pits + 2 larger pits

    public Board() {
        // Initialize the board with 6 stones in each small pit
        for (int i = 0; i < 14; i++) {
            pits[i] = 6;
        }
        // Larger pits (stores) start empty
        pits[6] = 0; // Player 1's store
        pits[13] = 0; // Player 2's store
    }

    public String toHTML(boolean isCurrentPlayer, Player player) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Mancala Game</title></head><body>");
        html.append("<h1>Current Game State</h1><table><tr>");

        // Display player 1's larger pit
        html.append("<td style='width: 50px; text-align: center;'><span style='color: blue;'>").append(pits[13]).append("</span></td>");

        // Display player 1's pits
        for (int i = 12; i > 6; i--) {
            html.append("<td style='width: 50px; text-align: center;'>");
            if (isCurrentPlayer && player == Player.blue) {
                html.append("<a href='/move/").append(i).append("' style='color: blue;'>");
            }
            html.append(pits[i]);
            if (isCurrentPlayer && player == Player.blue) {
                html.append("</a>");
            }
            html.append("</td>");
        }

        // Display player 2's pits
        html.append("</tr><tr><td></td>"); // Skip the first cell for alignment
        for (int i = 0; i < 6; i++) {
            html.append("<td style='width: 50px; text-align: center;'>");
            if (isCurrentPlayer && player == Player.red) {
                html.append("<a href='/move/").append(i).append("' style='color: red;'>");
            }
            html.append(pits[i]);
            if (isCurrentPlayer && player == Player.red) {
                html.append("</a>");
            }
            html.append("</td>");
        }

        // Display player 2's larger pit
        html.append("<td style='width: 50px; text-align: center;'><span style='color: red;'>").append(pits[6]).append("</span></td>");

        html.append("</tr></table></body></html>");
        return html.toString();
    }

    // This method is designed for only two players currently.
    public boolean makeMove(int pitIndex, Player player) {
        if (player == Player.blue && pitIndex < 7 ||
                player == Player.red && pitIndex > 6)
        {
            return false;
        }

        int stones = pits[pitIndex];
        pits[pitIndex] = 0;
        int lastPitIndex = 0;

        for (int i = 1; i <= stones; i++) {
            lastPitIndex = (pitIndex + i) % pits.length;
            // Skip opponent's big pit
            if ((player == Player.blue && lastPitIndex == 6) || (player == Player.red && lastPitIndex == 13)) {
                stones++; // Compensate for the skipped pit
                continue;
            }
            pits[lastPitIndex]++;
        }

        // Capturing logic
        if (lastPitIndex != 6 && lastPitIndex != 13 && pits[lastPitIndex] == 1) {
            // Check if the last pit is on the player's side and was empty before the move
            if ((player == Player.blue && lastPitIndex > 6) || (player == Player.red && lastPitIndex < 6)) {
                int oppositePitIndex = 12 - lastPitIndex;
                int capturedStones = pits[oppositePitIndex] + 1;  // Capture opposite stones plus the one just placed
                pits[oppositePitIndex] = 0;  // Empty the opposite pit
                pits[lastPitIndex] = 0;  // Empty the last pit
                pits[player == Player.blue ? 13 : 6] += capturedStones;  // Add to the player's store
            }
        }

        // Determine the index for the player's big pit
        int bigPitIndex = player == Player.blue ? 13 : 6;

        // Player gets another turn if the last stone lands in their big pit
        return lastPitIndex == bigPitIndex;
    }

    public boolean isGameOver()
    {
        // Here we assume that we always play with the maximum number of players. If we want to allow games with fewer
        // players we will need to store the number of players and loop until that number instead.
        if (7 * Player.values().length != pits.length) {
            throw new IllegalArgumentException("The number of players does not match the board's capacity!");
        }
        for (int player = 0; player < Player.values().length; player++)
        {
            boolean pitsEmpty = true;
            for (int pit = 7 * player; pit < 7 * (player + 1) - 1; pit++)
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
        int storeIndex = 6 + 7 * player.ordinal();
        if (storeIndex < pits.length)
        {
            return pits[storeIndex];
        }
        else
        {
            throw new IllegalArgumentException("Invalid player: " + player);
        }
    }

    // These are currently only used for testing. A cleaner approach would be to make pits protected
    // and inherit the class into TestBoard and expose pits from there. For simplicity I chose to
    // make them public in the main class for now, given that the game is a relatively small app
    // and we want to quickly get to a testable MVP.
    public int[] getPits()
    {
        return pits;
    }

    public void setPits(int[] pits)
    {
        this.pits = pits;
    }
}
