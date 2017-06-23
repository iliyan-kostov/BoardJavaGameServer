package game.board;

/**
 * Клас за фигурите в играта.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public class Figure {

    /**
     * координати на фигурата в рамките на дъската
     */
    public BoardCoords boardCoords;

    /**
     * собственик на фигурата
     */
    public final String username;

    public Figure(BoardCoords boardCoords, String username) {
        this.boardCoords = new BoardCoords(boardCoords.row, boardCoords.col);
        this.username = username;
    }
}
