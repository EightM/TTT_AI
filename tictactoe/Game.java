package tictactoe;

import tictactoe.player.AiPlayer;
import tictactoe.player.HumanPlayer;
import tictactoe.player.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Game {
    private final Board board = new Board();

    public void startGame() {
        var gameSettings = getGameSettings();

        if (gameSettings.isEmpty()) {
            return;
        }

        var firstPlayer = createPlayer(gameSettings.get().getKey());
        var secondPlayer = createPlayer(gameSettings.get().getValue());
        board.printBoard();
        var isFirstPlayerTurn = true;

        var gameResult = "";
        while (gameResult.isEmpty()) {
            if (isFirstPlayerTurn) {
                firstPlayer.makeAMove(board);
                isFirstPlayerTurn = false;
                gameResult = isGameFinished();
                continue;
            }

            secondPlayer.makeAMove(board);
            gameResult = isGameFinished();
            isFirstPlayerTurn = true;
        }
        printGameStatus(gameResult);
    }

    public String isGameFinished() {
        var lastMark = board.lastPlayerMark();
        return board.isFinished(lastMark);
    }

    private Player createPlayer(String userType) {
        var aiTypes = Set.of("easy", "medium", "hard");
        if (aiTypes.contains(userType)) {
            return new AiPlayer(userType);
        }

        return new HumanPlayer();
    }

    private Optional<Map.Entry<String, String>> getGameSettings() {
        var scanner = new Scanner(System.in);
        System.out.println("Input command:");
        var input = scanner.nextLine();

        while (true) {
            if (input.equals("exit")) {
                return Optional.empty();
            }

            if (input.startsWith("start")) {
                var userArgs = handleStartArgument(input);
                if (userArgs.isPresent()) {
                    return userArgs;
                }
            }

            System.out.println("Bad parameters!");
            input = scanner.nextLine();
        }
    }

    private Optional<Map.Entry<String, String>> handleStartArgument(String input) {
        var args = Arrays.stream(input.split("\\s+"))
                .filter(Predicate.isEqual("user").or(Predicate.isEqual("easy").or(Predicate.isEqual("medium"))))
                .collect(Collectors.toList());

        if (args.size() != 2) {
            return Optional.empty();
        }

        return Optional.of(Map.entry(args.get(0), args.get(1)));
    }

    private void printGameStatus(String gameResult) {
        System.out.println(gameResult);
    }

}
