package tictactoe.player;

import tictactoe.Board;

import java.util.List;
import java.util.Scanner;

public class HumanPlayer implements Player {

    @Override
    public void makeAMove(Board board) {
        var scanner = new Scanner(System.in);

        var success = false;
        while (!success) {
            System.out.println("Enter the coordinates:");
            List<Integer> input = readAndCheckInput(scanner);
            if (input.isEmpty()) {
                System.out.println("You should enter numbers!");
                continue;
            }
            var currentPlayerMark = board.currentPlayerMark();
            success = board.putMarkOnTheBoard(currentPlayerMark, input.get(0), input.get(1));
        }
        board.printBoard();
    }

}
