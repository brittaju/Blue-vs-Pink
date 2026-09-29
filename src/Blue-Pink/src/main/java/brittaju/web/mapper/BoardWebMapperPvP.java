package brittaju.web.mapper;

import brittaju.domain.model.BoardPvP;
import brittaju.web.model.BoardWeb;

import java.util.ArrayList;
import java.util.List;

public final class BoardWebMapperPvP {

    private BoardWebMapperPvP() {}

    public static BoardPvP toDomain(BoardWeb board) {
        int[][] newBoard = new int[3][3];
        for (int i = 0; i < 3; i++) {
            List<Character> row = board.board().get(i);
            for (int j = 0; j < 3; j++) {
                switch (row.get(j)) {
                    case 'X':
                        newBoard[i][j] = 1;
                        break;
                    case 'O':
                        newBoard[i][j] = -1;
                        break;
                    default:
                        newBoard[i][j] = 0;
                }
            }
        }
        return new BoardPvP(newBoard);
    }

    public static BoardWeb fromDomain(BoardPvP board) {
        List<List<Character>> newBoardWeb = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Character> row = new ArrayList<>(3);
            for (int j = 0; j < 3; j++) {
                switch (board.getCell(i, j)) {
                    case 1:
                        row.add('X');
                        break;
                    case -1:
                        row.add('O');
                        break;
                    default:
                        row.add(' ');
                }
            }
            newBoardWeb.add(row);
        }
        return new BoardWeb(newBoardWeb);
    }

}
