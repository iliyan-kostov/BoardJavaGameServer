package game.board;

import apps.GameManager;

public class GameLogic_3 extends GameLogic {

    public GameLogic_3(Board_Serverside board, GameManager gameManager) {
        super(board, gameManager);
    }

    @Override
    public synchronized boolean isNextTo(BoardCoords from, BoardCoords to) {
        int row1 = from.row;
        int col1 = from.col;
        int row2 = to.row;
        int col2 = to.col;
        if (row1 % 2 == 0) {
            if (((row2 == row1) && (col2 == col1 - 1)) || ((row2 == row1) && (col2 == col1 + 1)) || ((row2 == col1 + 1) && (col2 == col1 + 1))) {
                return true;
            }
        } else {
            // row1 % 2 == 1
            if (((row2 == row1 - 1) && (col2 == col1 - 1) || ((row2 == row1) && (col2 == col1 - 1))) || ((row2 == row1) && (col2 == col1 + 1))) {
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
            if ((0 <= col) && (col <= 2 * row)) {
                return true;
            }
        }
        return false;
    }
}
