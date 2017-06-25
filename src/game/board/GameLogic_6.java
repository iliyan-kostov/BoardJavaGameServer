package game.board;

import apps.GameManager;

public class GameLogic_6 extends GameLogic {

    public GameLogic_6(Board_Serverside board, GameManager gameManager) {
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
        } else if (2 * row1 + 1 == this.board.boardSizeRows) {
            if (((row1 == row2 + 1) && (col1 == col2 + 1)) || ((row1 == row2 + 1) && (col1 == col2)) || ((row1 == row2 - 1) && (col1 == col2 + 1)) || ((row1 == row2 - 1) && (col1 == col2))) {
                return true;
            }
        } else if (2 * row1 < this.board.boardSizeRows) {
            if (((row1 == row2 + 1) && (col1 == col2 + 1)) || ((row1 == row2 + 1) && (col1 == col2)) || ((row1 == row2 - 1) && (col1 == col2)) || ((row1 == row2 - 1) && (col1 == col2 - 1))) {
                return true;
            }
        } else if (2 * row1 > this.board.boardSizeRows) {
            if (((row1 == row2 + 1) && (col2 == col1)) || ((row1 == row2 + 1) && (col2 == col1 + 1)) || ((row1 == row2 - 1) && (col2 == col1 - 1)) || ((row1 == row2 - 1) && (col2 == col1))) {
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
            int mid = (this.board.boardSizeCols + 1) / 2;
            if (0 <= col) {
                if (row == mid) {
                    if (col < this.board.boardSizeRows) {
                        return true;
                    }
                } else if (row < mid) {
                    if (col < this.board.boardSizeRows - (mid - row)) {
                        return true;
                    }
                } else if (row > mid) {
                    if (col < this.board.boardSizeRows + (mid - row)) {
                        return true;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
