package com.mancala.mancala.model;

import org.springframework.web.bind.annotation.PathVariable;

public class Board {
    private final int[] pits = new int[14]; // 12 small pits + 2 larger pits

    public Board() {
        // Initialize the board with 6 stones in each small pit
        for (int i = 0; i < 12; i++) {
            pits[i] = 6;
        }
        // Larger pits (stores) start empty
        pits[12] = 0; // Player 1's store
        pits[13] = 0; // Player 2's store
    }

    public String toHTML() {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Mancala Game</title></head><body>");
        html.append("<h1>Current Game State</h1><table><tr>");

        // Display player 1's larger pit
        html.append("<td style='width: 50px; text-align: center;'>").append(pits[13]).append("</td>");

        // Display player 2's pits
        for (int i = 12; i > 6; i--) {
            html.append("<td style='width: 50px; text-align: center;'><a href='/move/").append(i).append("'>").append(pits[i]).append("</a></td>");
        }

        // Display player 1's pits
        html.append("</tr><tr><td></td>"); // Skip the first cell for alignment
        for (int i = 0; i < 6; i++) {
            html.append("<td style='width: 50px; text-align: center;'><a href='/move/").append(i).append("'>").append(pits[i]).append("</a></td>");
        }

        // Display player 2's larger pit
        html.append("<td style='width: 50px; text-align: center;'>").append(pits[6]).append("</td>");

        html.append("</tr></table></body></html>");
        return html.toString();
    }

    public void makeMove(@PathVariable int pitIndex) {
        int stones = pits[pitIndex];
        pits[pitIndex] = 0;

        for (int i = 1; i <= stones; i++) {
            pits[(pitIndex + i) % pits.length]++;
        }
    }
}
