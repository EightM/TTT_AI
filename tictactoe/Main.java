package tictactoe;

public class Main {
    public static void main(String[] args) {
        var game = new Game();
        game.fillBoardByString();
        game.nextTurn();
    }
}