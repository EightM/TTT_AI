package tictactoe.player;

import tictactoe.Board;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public interface Player {
    void makeAMove(Board board);

    default List<Integer> readAndCheckInput(Scanner scanner) {

        var digits = "0123456789";
        var input = Arrays.stream(scanner.nextLine().split("\\s+"))
                .filter(digits::contains)
                .collect(Collectors.toList());

        if (input.size() < 2) {
            return Collections.emptyList();
        }

        return input.stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
