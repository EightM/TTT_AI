package tictactoe;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Board {
    private final String[][] gameBoard = new String[3][3];

    public void fillBoardByString(String initialData) {
        if (initialData.length() != 9) {
            throw new IllegalArgumentException();
        }
        var shift = 0;
        for (var i = 0; i < gameBoard.length; i++) {
            for (var j = 0; j < gameBoard.length; j++) {
                var mark = getNextMarkFromData(initialData, shift);
                gameBoard[i][j] = mark;
                shift++;
            }
        }
    }

    public void printBoard() {
        System.out.println("---------");
        for (String[] strings : gameBoard) {
            String row = "| " + String.join(" ", strings) + " |";
            System.out.println(row);
        }
        System.out.println("---------");
    }

    private String getNextMarkFromData(String initialData, int shift) {
        var symbol = String.valueOf(initialData.charAt(shift));
        if (symbol.equals("_")) {
            return " ";
        }

        return symbol;
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

    public boolean putMarkOnTheBoard(int x, int y) {
        if (x < 1 || x > 3 || y < 1 || y > 3) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        }

        // Массив индексируется с 0 до 2
        // Формат ввода человеком является обратным тому, как данные хранятся в массиве
        var realX = x - 1;
        var realY = y - 1;

        if (!gameBoard[realX][realY].isBlank()) {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }

        gameBoard[realX][realY] = currentPlayerMark();

        return true;
    }

    public boolean isFinished(String lastMark) {
        // check rows
        if (checkBoardRowsForWin(lastMark)) {
            return true;
        }

        // check columns
        if (checkColumnsForWin(lastMark)) {
            return true;
        }

        return checkDiagonalsForWin(lastMark);
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

    public boolean isBoardFull() {
        return Arrays.stream(gameBoard)
                .flatMap(Arrays::stream)
                .filter(String::isBlank)
                .findAny().isEmpty();
    }
}
