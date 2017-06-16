package protocol;

import game.board.BoardCoords;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>
 * Съобщение, включващо данни за премахване на фигури от дадена дъска.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Board_RemoveFigures extends Message_Board {

    public final ArrayList<BoardCoords> from;

    public Message_Board_RemoveFigures(String username, int boardId, ArrayList<BoardCoords> from) {
        super(username, Message.MESSAGETYPE.BOARD_MOVEFIGURES, boardId);
        this.from = new ArrayList<>();
        {
            Iterator<BoardCoords> it = from.iterator();
            while (it.hasNext()) {
                this.from.add(it.next());
            }
        }
    }
}
