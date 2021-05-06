package tictactoe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
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
        board.printBoard();
        var isHumanTurn = true;

        while (!isGameFinished()) {
            if (isHumanTurn) {
                nextHumanTurn();
                isHumanTurn = false;
                continue;
            }

            nextAiTurn();
            isHumanTurn = true;
        }
        printGameStatus();
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
