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
     * брой разрешени премествания на фигура в рамките на един ход
     */
    public static final int MOVESMAX = 1;

    /**
     * брой извършени премествания в рамките на текущия ход
     */
    public int movesDone;

    /**
     * собственик на фигурата
     */
    public final String username;

    public Figure(BoardCoords boardCoords, String username) {
        this.boardCoords = new BoardCoords(boardCoords.row, boardCoords.col);
        this.username = username;
        this.movesDone = 0;
    }

    public final boolean canMove() {
        return (this.movesDone < Figure.MOVESMAX);
    }

    /**
     * Рестартира брояча на извършени премествания за фигурата.
     */
    public final synchronized void movesReset() {
        this.movesDone = 0;
    }

    /**
     * Увеличава броя извършени премествания в рамките на хода.
     */
    public final synchronized void movesMake() {
        this.movesDone++;
    }
}
