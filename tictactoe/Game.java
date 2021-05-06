package tictactoe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Game {
    private final Board board = new Board();

    public void nextHumanTurn() {
        var scanner = new Scanner(System.in);

        var success = false;
        while (!success) {
            System.out.println("Enter the coordinates:");
            List<Integer> input = readAndCheckInput(scanner);
            if (input.isEmpty()) {
                System.out.println("You should enter numbers!");
                continue;
            }
            success = board.putMarkOnTheBoard(input.get(0), input.get(1));
        }
        board.printBoard();
    }

    public void startGame() {
        var gameSettings = getGameSettings();

        if (gameSettings.isEmpty()) {
            return;
        }

        var firstPlayer = new Player(gameSettings.get().getKey());
        var secondPlayer = new Player(gameSettings.get().getValue());
        board.printBoard();
        var isFirstPlayerTurn = true;

        while (!isGameFinished()) {
            if (isFirstPlayerTurn) {
                nextTurn(firstPlayer);
                isFirstPlayerTurn = false;
                continue;
            }

            nextTurn(secondPlayer);
            isFirstPlayerTurn = true;
        }
        printGameStatus();
    }

    private void nextTurn(Player player) {
        if (player.isHuman()) {
            nextHumanTurn();
        } else {
            nextAiTurn();
        }
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
                .filter(Predicate.isEqual("user").or(Predicate.isEqual("easy")))
                .collect(Collectors.toList());

        if (args.size() != 2) {
            return Optional.empty();
        }

        return Optional.of(Map.entry(args.get(0), args.get(1)));
    }

    private void printGameStatus() {
        var lastMark = board.currentPlayerMark().equals("O") ? "X" : "O";
        if (isGameFinished()) {
            System.out.println(lastMark + " wins");
        } else if (isBoardFull()) {
            System.out.println("Draw");
        } else {
            System.out.println("Game not finished");
        }
    }

    private List<Integer> readAndCheckInput(Scanner scanner) {

        var digits = "0123456789";
        var input = Arrays.stream(scanner.nextLine().split("\\s+"))
                .filter(digits::contains)
                .collect(Collectors.toList());

        if (input.size() < 2) {
            return Collections.emptyList();
        }

        return input.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    private boolean isGameFinished() {
        var lastMark = board.currentPlayerMark().equals("O") ? "X" : "O";
        return board.isFinished(lastMark);
    }

    private boolean isBoardFull() {
        return board.isBoardFull();
    }

    private void nextAiTurn() {
        System.out.println("Making move level \"easy\"");
        var randomFreeCell = board.getRandomFreeCell();
        if (randomFreeCell.isEmpty()) {
            return;
        }

        board.putMarkOnTheBoard(randomFreeCell.get().getKey() + 1, randomFreeCell.get().getValue() + 1);
        board.printBoard();
    }
}
