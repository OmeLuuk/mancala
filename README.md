# Mancala Game Web Application

## Small comments
- A project like this one is new for me, so Java-specific features, frameworks and the API interactions I learned on the go.

## Overview
This application is a web-based implementation of the Mancala game, allowing two players to play the game in a simple web interface. It's built using Spring Boot and Java.

## Features
- Two-player Mancala game.
- Web-based interface for game interaction.
- Session-based game state management.

## How to Run
1. **Prerequisites**: Ensure you have Java and Spring Boot installed.
2. **Running the Application**: Execute `./mvnw spring-boot:run` in the root directory of the project.
3. **Accessing the Game**: Open a web browser and navigate to `http://localhost:8080`.

## How to Play
Board Setup
Each of the two players has his six pits in front of him. To the right of the six pits,
each player has a larger pit. At the start of the game, there are six stones in each
of the six round pits .
Rules

Game Play
The player who begins with the first move picks up all the stones in any of his own
six pits, and sows the stones on to the right, one in each of the following pits,
including his own big pit. No stones are put in the opponents' big pit. If the player's
last stone lands in his own big pit, he gets another turn. This can be repeated
several times before it's the other player's turn.

- Capturing Stones
During the game the pits are emptied on both sides. Always when the last stone
lands in an own empty pit, the player captures his own stone and all stones in the
opposite pit (the other player�s pit) and puts them in his own (big or little?) pit.

The Game Ends
The game is over as soon as one of the sides runs out of stones. The player who
still has stones in his pits keeps them and puts them in his big pit. The winner of
the game is the player who has the most stones in his big pit. 

## API Endpoints
- `GET /start`: Starts a new game.
- `GET /move/{pitIndex}`: Makes a move for the current player.
- `GET /game`: Returns the current game state as HTML.

## Future Enhancements
- Improve real-time updates with SSE/WebSockets.
- Support for multiple concurrent games.
- Enhanced UI with better interactivity.

# Mancala Game Domain Model

## Classes

### Game
- **Attributes**:
  - `currentPlayer`: Player
  - `gameOver`: boolean
  - `winners`: Player[]
  - `board`: Board
- **Methods**:
  - `makeMove(pitIndex: int, player: Player)`: void
  - `determineWinners()`: void
  - `isPlayerTurn(player: Player)`: boolean
  - `getHTML(player: Player)`: String

### Board
- **Attributes**:
  - `pits`: int[] (Array representing the board state)
- **Methods**:
  - `makeMove(pitIndex: int, player: Player)`: boolean
  - `isGameOver()`: boolean
  - `getStoreCount(player: Player)`: int
  - `toHTML(isCurrentPlayer: boolean, player: Player)`: String

### Player (Enum)
- **Values**:
  - `blue`
  - `red`

## Relationships
- The `Game` class contains a `Board` object.
- The `Game` class manages instances of `Player`.
- The `Board` class is used within the `Game` class to represent the state of the game board and to execute game logic.
