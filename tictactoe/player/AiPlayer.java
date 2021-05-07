package tictactoe.player;

import tictactoe.Board;

import java.util.Arrays;

public class AiPlayer implements Player{

    private final BotDifficulty difficulty;

    public AiPlayer(String difficulty) {
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

    private void makeHardMove(Board board) {
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
