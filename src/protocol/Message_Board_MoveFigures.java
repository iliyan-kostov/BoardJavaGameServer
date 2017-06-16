package protocol;

import game.board.BoardCoords;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>
 * Съобщение, включващо данни за местене на фигури в дадена дъска.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Board_MoveFigures extends Message_Board {

    public final ArrayList<BoardCoords> from;
    public final ArrayList<BoardCoords> to;

    public Message_Board_MoveFigures(String username, int boardId, ArrayList<BoardCoords> from, ArrayList<BoardCoords> to) {
        super(username, Message.MESSAGETYPE.BOARD_MOVEFIGURES, boardId);
        this.from = new ArrayList<>();
        {
            Iterator<BoardCoords> it = from.iterator();
            while (it.hasNext()) {
                this.from.add(it.next());
            }
        }
        this.to = new ArrayList<>();
        {
            Iterator<BoardCoords> it = to.iterator();
            while (it.hasNext()) {
                this.to.add(it.next());
            }
        }
    }
}
