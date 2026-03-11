package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Game;
import com.example.demo.repository.GameRepository;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // Create a new game
    @PostMapping
    public Game createGame() {
        Game game = new Game();
        game.setBoard("---------");
        game.setCurrentPlayer("X");
        game.setStatus("IN_PROGRESS");
        return gameRepository.save(game);
    }

    // Get game by ID
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        return gameRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Make a move: position is 0-8
    @PostMapping("/{id}/move/{position}")
    public ResponseEntity<?> makeMove(@PathVariable Long id, @PathVariable int position) {
        Game game = gameRepository.findById(id).orElse(null);

        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        if (!"IN_PROGRESS".equals(game.getStatus())) {
            return ResponseEntity.badRequest().body("Game is already over.");
        }
        if (position < 0 || position > 8) {
            return ResponseEntity.badRequest().body("Position must be 0-8.");
        }

        char[] board = game.getBoard().toCharArray();

        if (board[position] != '-') {
            return ResponseEntity.badRequest().body("Position already taken.");
        }

        // Place the move
        board[position] = game.getCurrentPlayer().charAt(0);
        game.setBoard(new String(board));

        // Check for winner
        String winner = checkWinner(board);
        if (winner != null) {
            game.setStatus(winner.equals("X") ? "X_WON" : "O_WON");
        } else if (isBoardFull(board)) {
            game.setStatus("DRAW");
        } else {
            // Switch player
            game.setCurrentPlayer(game.getCurrentPlayer().equals("X") ? "O" : "X");
        }

        return ResponseEntity.ok(gameRepository.save(game));
    }

    // -------------------------------------------------------
    // Helpers
    // -------------------------------------------------------

    private String checkWinner(char[] b) {
        int[][] lines = {
            {0,1,2}, {3,4,5}, {6,7,8}, // rows
            {0,3,6}, {1,4,7}, {2,5,8}, // cols
            {0,4,8}, {2,4,6}            // diagonals
        };
        for (int[] line : lines) {
            if (b[line[0]] != '-' &&
                b[line[0]] == b[line[1]] &&
                b[line[1]] == b[line[2]]) {
                return String.valueOf(b[line[0]]);
            }
        }
        return null;
    }

    private boolean isBoardFull(char[] b) {
        for (char c : b) {
            if (c == '-') return false;
        }
        return true;
    }
}
