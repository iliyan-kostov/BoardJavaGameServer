package protocol;

/**
 * <p>
 * Съобщение, носещо данни за развитие на определена игра.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public abstract class Message_Board extends Message {

    public final int boardId;

    public Message_Board(String username, MESSAGETYPE messageType, int boardId) {
        super(username, messageType);
        this.boardId = boardId;
    }
}