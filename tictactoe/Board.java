package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Board {
    private final String[][] gameBoard = new String[3][3];

    public Board() {
        for (var i = 0; i < gameBoard.length; i++) {
            for (var j = 0; j < gameBoard.length; j++) {
                gameBoard[i][j] = " ";
            }
        }
    }

    public Board(String[][] data) {
        for (var i = 0; i < gameBoard.length; i++) {
            System.arraycopy(data[i], 0, gameBoard[i], 0, gameBoard.length);
        }
    }

    public Board getBoardCopy() {
        var copyBoard = new String[gameBoard.length][gameBoard.length];
        for (var i = 0; i < copyBoard.length; i++) {
            copyBoard[i] = Arrays.copyOf(gameBoard[i], gameBoard.length);
        }

        return new Board(copyBoard);
    }

    public void printBoard() {
        System.out.println("---------");
        for (String[] strings : gameBoard) {
            String row = "| " + String.join(" ", strings) + " |";
            System.out.println(row);
        }
        System.out.println("---------");
    }

    public String currentPlayerMark() {
        var markCounts = Arrays.stream(gameBoard)
                .flatMap(Arrays::stream).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        var xCount = markCounts.getOrDefault("X", 0L);
        var oCount = markCounts.getOrDefault("O", 0L);

        if (xCount.equals(oCount)) {
            return "X";
        }

        return "O";
    }

    public boolean putMarkOnTheBoard(String mark, int x, int y) {

        var realX = x - 1;
        var realY = y - 1;

        if (realX < 0 || realX > 2 || realY < 0 || realY > 2) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        }

        if (!gameBoard[realX][realY].isBlank()) {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }

        gameBoard[realX][realY] = mark;

        return true;
    }

    public String isFinished(String lastMark) {
        // check rows
        if (checkBoardRowsForWin(lastMark) || checkColumnsForWin(lastMark) || checkDiagonalsForWin(lastMark)) {
            return lastMark + " wins";
        }

        if (isBoardFull()) {
            return "Draw";
        }

        return "";
    }

    public boolean isBoardFull() {
        return Arrays.stream(gameBoard)
                .flatMap(Arrays::stream)
                .filter(String::isBlank)
                .findAny().isEmpty();
    }

    public String lastPlayerMark() {
        return currentPlayerMark().equals("O") ? "X" : "O";
    }

    public List<Map.Entry<Integer, Integer>> getFreeCells() {
        List<Map.Entry<Integer, Integer>> freeCells = new ArrayList<>();
        for (var i = 0; i < gameBoard.length; i++) {
            for (var j = 0; j < gameBoard.length; j++) {
                if (gameBoard[i][j].isBlank()) {
                    freeCells.add(Map.entry(i, j));
                }
            }
        }

        return freeCells;
    }

    private boolean checkDiagonalsForWin(String lastMark) {
        var mainDiagonalScore = 0;
        var subDiagonalScore = 0;
        for (var i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i][i].equals(lastMark)) {
                mainDiagonalScore++;
            }
        }

        for (var i = 0; i < gameBoard.length; i++) {
            if (gameBoard[gameBoard.length - i - 1][i].equals(lastMark)) {
                subDiagonalScore++;
            }
        }
        return mainDiagonalScore == 3 || subDiagonalScore == 3;
    }

    private boolean checkColumnsForWin(String lastMark) {
        for (var i = 0; i < gameBoard.length; i++) {
            var countMark = 0;
            for (String[] strings : gameBoard) {
                if (strings[i].equals(lastMark)) {
                    countMark++;
                }

                if (countMark == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkBoardRowsForWin(String lastMark) {
        for (String[] strings : gameBoard) {
            var isFinished = Arrays.stream(strings)
                    .filter(currentMark -> currentMark.equals(lastMark))
                    .count() == 3L;

            if (isFinished) {
                return true;
            }
        }
        return false;
    }

    public Optional<Map.Entry<Integer, Integer>> getRandomFreeCell() {
        var freeCells = getFreeCells();
        Collections.shuffle(freeCells);
        var iterator = freeCells.iterator();

        if (iterator.hasNext()) {
            return Optional.of(iterator.next());
        }

        return Optional.empty();
    }
}
