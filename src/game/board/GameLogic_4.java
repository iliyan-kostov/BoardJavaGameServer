package game.board;

import apps.GameManager;

public class GameLogic_4 extends GameLogic {

    public GameLogic_4(Board_Serverside board, GameManager gameManager) {
        super(board, gameManager);
    }

    @Override
    public synchronized boolean isNextTo(BoardCoords from, BoardCoords to) {
        int row1 = from.row;
        int col1 = from.col;
        int row2 = to.row;
        int col2 = to.col;
        if (row1 == row2) {
            if ((col1 == col2 - 1) || (col1 == col2 + 1)) {
                return true;
            }
        } else if (col1 == col2) {
            if ((row1 == row2 - 1) || (row1 == row2 + 1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean isInsideBoard(BoardCoords boardCoords) {
        int row = boardCoords.row;
        int col = boardCoords.col;
        if ((0 <= row) && (row < this.board.boardSizeRows)) {
            if ((0 <= col) && (col < this.board.boardSizeCols)) {
                return true;
            }
        }
        return false;
    }
}
