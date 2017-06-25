package game.board;

/**
 * <p>
 * Координати на поле (или фигура) в рамките на дъската.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class BoardCoords {

    public final int row;
    public final int col;

    public BoardCoords(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
