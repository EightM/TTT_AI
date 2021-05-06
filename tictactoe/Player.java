package tictactoe;

public class Player {
    private final boolean isHuman;

    public boolean isHuman() {
        return isHuman;
    }

    public Player(String playerType) {
        this.isHuman = playerType.equals("user");
    }
}
