package game.board;

import java.io.Serializable;

/**
 * <p>
 * Координати на поле (или фигура) в рамките на дъската.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class BoardCoords implements Serializable {

    public final int row;
    public final int col;

    public BoardCoords(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
