package game.board;

import game.IMessageHandler;
import game.IMessageSender;
import game.protocol.Message_Board;

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

    public Board(int boardShape, int boardId) {
        if ((boardShape != 3) && (boardShape != 4) && (boardShape != 6)) {
            throw new IllegalArgumentException();
        } else {
            this.boardShape = boardShape;
            this.boardId = boardId;
        }
    }
}
