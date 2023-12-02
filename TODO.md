# To-Do List for Mancala Game Project

## Essential Features and Improvements
- **Quick Code ToDos:
  - Currently the colors for player board drawing are hardcoded in the strings
- **Basic Game Rules**: 
  - Implement stone distribution logic.
  - Add rules for capturing stones.
  - Handle the scenario where the last stone lands in an empty pit.
- **Unit Testing**:
  - Write tests for initial game setup.
  - Test stone distribution and capturing rules.
- **Track and Display Turns**: 
  - Implement a turn system in the game logic.
  - Update the UI to indicate the current player's turn.
- **Dynamic Board Updates**:
  - Implement better solution for making a move than clicking links to intermediate pages
  - Update the board dynamically.
- **README File**:
  - Document setup instructions.
  - Provide a brief overview of game rules and architecture.
- **In-Memory Game State**:
  - Store the current state of the game in memory.
  - Retrieve and update this state with each move.

## Enhancements for Scalability and Robustness
- **Concurrent Games**:
  - Modify the application to handle multiple game instances.
  - Use unique identifiers for each game session.
- **Session Management**:
  - Implement session cookies to track players and games.
- **Integration Testing**:
  - Test the integration of the frontend and backend.
  - Ensure game state consistency throughout the game lifecycle.
- **Deploy Application**:
  - Research hosting options (e.g., Heroku, AWS).
  - Deploy the application to something other than localhost:8080.

## Advanced Features (Nice to Have)
- **Load Balancer**:
  - Investigate load balancing solutions.
  - Plan the deployment architecture for scalability.
- **Persistent Storage**:
  - Evaluate database options.
  - Implement database integration for game states.
- **User Authentication**:
  - Design a simple login system.
  - Integrate user authentication with the game sessions.

## Ongoing Tasks
- **Refine UI**:
  - Regularly enhance the user interface for better usability.
- **Regular Refactoring**:
  - Continuously refactor code for clarity and maintainability.
- **Continuous Unit Testing**:
  - Regularly write and update unit tests as new features are added.
- **Documentation Updates**:
  - Keep the README and other documentation in sync with development changes.
- **Feedback Incorporation**:
  - Periodically review user and peer feedback.
  - Adjust development priorities based on feedback.
