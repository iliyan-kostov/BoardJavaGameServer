package game.protocol;

/**
 * <p>
 * Съобщение, известяващо, че даден играч се предава.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Board_Surrender extends Message_Board {

    public final String playerSurrenders;

    public Message_Board_Surrender(String username, int boardId, String playerSurrenders) {
        super(username, Message.MESSAGETYPE.BOARD_SURRENDER, boardId);
        this.playerSurrenders = playerSurrenders;
    }
}
