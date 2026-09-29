package brittaju.domain.model;

public record BoardPvP(int[][] board) {

    public BoardPvP() {
        this(new int[3][3]);
    }

    public BoardPvP(int[][] board) {
        this.board = deepCopy(board);
    }

    public int getCell(int row, int col) {
        return board[row][col];
    }

    public BoardPvP currentCopy(int player, int row, int col) {
        int[][] currentCopy = deepCopy(this.board);
        currentCopy[row][col] = player;
        return new BoardPvP(currentCopy);
    }

    private static int[][] deepCopy(int[][] original) {
        int[][] copy = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 3);
        }
        return copy;
    }

}