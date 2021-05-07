package tictactoe.player;

import tictactoe.Board;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AiPlayer implements Player{

    private final BotDifficulty difficulty;
    private final String playerMark;

    public AiPlayer(String difficulty, String mark) {
        switch (difficulty) {
            case "easy":
                this.difficulty = BotDifficulty.EASY;
                break;
            case "hard":
                this.difficulty = BotDifficulty.HARD;
                break;
            default:
                this.difficulty = BotDifficulty.MEDIUM;
                break;
        }

        this.playerMark = mark;
    }

    @Override
    public void makeAMove(Board board) {
        switch (difficulty) {
            case EASY:
                makeEasyMove(board);
                break;
            case HARD:
                makeHardMove(board);
                break;
            default:
                makeMediumMove(board);
                break;
        }
    }

    private void makeMediumMove(Board originalBoard) {
        var freeCells = originalBoard.getFreeCells();
        System.out.println("Making move level \"medium\"");
        var lastMark = originalBoard.lastPlayerMark();
        for (var freeCell : freeCells) {
            var copyBoard = originalBoard.getBoardCopy();
            copyBoard.putMarkOnTheBoard(lastMark, freeCell.getKey() + 1, freeCell.getValue() + 1);
            if (!copyBoard.isFinished(lastMark).isEmpty()) {
                originalBoard.putMarkOnTheBoard(originalBoard.currentPlayerMark(),
                        freeCell.getKey() + 1, freeCell.getValue() + 1);
                originalBoard.printBoard();
                return;
            }
        }

        makeARandomMove(originalBoard);
        originalBoard.printBoard();
    }

    private void makeHardMove(Board originalBoard) {
        Map<Map.Entry<Integer, Integer>, Integer> scores = new HashMap<>();
        var freeCells = originalBoard.getFreeCells();
        for (var freeCell : freeCells) {
            var copyBoard = originalBoard.getBoardCopy();
            copyBoard.putMarkOnTheBoard(playerMark, freeCell.getKey() + 1, freeCell.getValue() + 1);
            var score = minimax(copyBoard);
            scores.put(freeCell, score);
        }

        var cellForMove = scores.entrySet().stream()
                .max(Map.Entry.comparingByValue()).orElseThrow().getKey();

        originalBoard.putMarkOnTheBoard(playerMark, cellForMove.getKey() + 1, cellForMove.getValue() + 1);
        originalBoard.printBoard();
    }

    private int minimax(Board originalBoard) {
        var currentPlayer = originalBoard.currentPlayerMark();
        var lastPlayer = originalBoard.lastPlayerMark();
        var freeCells = originalBoard.getFreeCells();
        var copyBoard = originalBoard.getBoardCopy();

        if (copyBoard.isFinished(lastPlayer).contains("wins") && playerMark.equals(lastPlayer)) {
            return 10;
        } else if (copyBoard.isFinished(lastPlayer).contains("wins") && !playerMark.equals(lastPlayer)) {
            return -10;
        } else if (freeCells.isEmpty()) {
            return 0;
        }

        Set<Integer> moves = new HashSet<>();
        for (var freeCell : freeCells) {
            copyBoard = originalBoard.getBoardCopy();
            copyBoard.putMarkOnTheBoard(currentPlayer, freeCell.getKey() + 1, freeCell.getValue() + 1);
            var score = minimax(copyBoard);
            moves.add(score);
        }

        if (currentPlayer.equals(playerMark)) {
            return moves.stream().mapToInt(Integer::intValue).max().orElseThrow();
        } else {
            return moves.stream().mapToInt(Integer::intValue).min().orElseThrow();
        }
    }

    private void makeEasyMove(Board board) {
        System.out.println("Making move level \"easy\"");
        if (!makeARandomMove(board)) {
            return;
        }
        board.printBoard();
    }

    private boolean makeARandomMove(Board board) {
        var randomFreeCell = board.getRandomFreeCell();
        if (randomFreeCell.isEmpty()) {
            return false;
        }

        var currentPlayerMark = board.currentPlayerMark();
        return board.putMarkOnTheBoard(currentPlayerMark,
                randomFreeCell.get().getKey() + 1, randomFreeCell.get().getValue() + 1);
    }

}
