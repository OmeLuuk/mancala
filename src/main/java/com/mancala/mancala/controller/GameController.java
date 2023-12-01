package com.mancala.mancala.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    private final int[] pits = new int[] { 6, 6, 6, 6, 6, 6, 0 ,6, 6, 6, 6, 6, 6, 0}; // 12 small pits + 2 larger pits

    @GetMapping("/")
    public String gameState() {
        StringBuilder html = new StringBuilder("<html><head><title>Mancala Game</title></head><body>");
        html.append("<h1>Current Game State</h1><table><tr>");

        // Display player 1's larger pit
        html.append("<td style='width: 50px; text-align: center;'>").append(pits[13]).append("</td>");

        // Display player 2's pits
        for (int i = 12; i > 6; i--) {
            html.append("<td style='width: 50px; text-align: center;'>").append(pits[i]).append("</td>");
        }

        // Display player 1's pits
        html.append("</tr><tr><td></td>"); // Skip the first cell for alignment
        for (int i = 0; i < 6; i++) {
            html.append("<td style='width: 50px; text-align: center;'>").append(pits[i]).append("</td>");
        }

        // Display player 2's larger pit
        html.append("<td style='width: 50px; text-align: center;'>").append(pits[6]).append("</td>");

        html.append("</tr></table><br>");

        for (int i = 0; i < pits.length; i++) {
            html.append("<a href='/move/").append(i).append("'>Move from Pit ").append(i).append("</a><br>");
        }

        return html.append("</body></html>").toString();
    }

    @GetMapping("/start")
    public String startGame() {
        for (int i = 0; i < pits.length; i++) {
            pits[i] = 6;
        }
        return "Game started. <a href='/'>View Game</a>";
    }

    @GetMapping("/move/{pitIndex}")
    public String makeMove(@PathVariable int pitIndex) {
        int stones = pits[pitIndex];
        pits[pitIndex] = 0;

        for (int i = 1; i <= stones; i++) {
            pits[(pitIndex + i) % pits.length]++;
        }

        return "Moved stones from pit " + pitIndex + ". <a href='/'>View Game</a>";
    }
}
