package brittaju.web.mapper;

import brittaju.domain.model.BoardPvE;
import brittaju.web.model.BoardWeb;

import java.util.ArrayList;
import java.util.List;

public final class BoardWebMapperPvE {

    private BoardWebMapperPvE() {}

    public static BoardPvE toDomainAI(BoardWeb board) {
        int[][] newBoard = new int[3][3];
        for (int i = 0; i < 3; i++) {
            List<Character> row = board.board().get(i);
            for (int j = 0; j < 3; j++) {
                switch (row.get(j)) {
                    case 'X':
                        newBoard[i][j] = -1;
                        break;
                    case 'O':
                        newBoard[i][j] = 1;
                        break;
                    default:
                        newBoard[i][j] = 0;
                }
            }
        }
        return new BoardPvE(newBoard);
    }

    public static BoardPvE toDomainHuman(BoardWeb board) {
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
        return new BoardPvE(newBoard);
    }

    public static BoardWeb fromDomainAI(BoardPvE board) {
        List<List<Character>> newBoardWeb = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Character> row = new ArrayList<>(3);
            for (int j = 0; j < 3; j++) {
                switch (board.getCell(i, j)) {
                    case -1:
                        row.add('X');
                        break;
                    case 1:
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

    public static BoardWeb fromDomainHuman(BoardPvE board) {
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
                        break;
                }
            }
            newBoardWeb.add(row);
        }
        return new BoardWeb(newBoardWeb);
    }

}
