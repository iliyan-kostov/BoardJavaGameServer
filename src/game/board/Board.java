package game.board;

import java.util.LinkedList;
import protocol.interfaces.IMessageHandler;
import protocol.interfaces.IMessageSender;

/**
 * <p>
 * Базов абстрактен клас за дъската.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public abstract class Board implements IMessageSender, IMessageHandler {

    /**
     * <p>
     * Брой страни на дъската.
     */
    public final int boardShape;

    /**
     * <p>
     * Идентификатор на дъската (играта) в рамките на системата.
     */
    public final int boardId;

    /**
     * <p>
     * Имена на играчите (login) - по ред на ходовете.
     */
    public final String[] usernames;
    public final boolean[] activePlayers;

    protected int currentPlayer;
    public LinkedList<BoardCoords> movesFrom;
    public LinkedList<BoardCoords> movesTo;

    public Board(int boardShape, int boardId, String[] usernames) {
        if ((boardShape != 3) && (boardShape != 4) && (boardShape != 6)) {
            throw new IllegalArgumentException();
        } else {
            this.boardShape = boardShape;
            this.boardId = boardId;
            this.usernames = new String[boardShape];
            for (int i = 0; i < boardShape; i++) {
                this.usernames[i] = usernames[i];
            }
            this.activePlayers = new boolean[boardShape];
            for (int i = 0; i < boardShape; i++) {
                this.activePlayers[i] = true;
            }
            this.currentPlayer = 0;
            this.movesFrom = new LinkedList<>();
            this.movesTo = new LinkedList<>();
        }
    }

    public abstract void userLogout(String username);
}
