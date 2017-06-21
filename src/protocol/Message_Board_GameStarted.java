package protocol;

/**
 * <p>
 * Съобщение за започнала нова игра, в която потребителят участва.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Board_GameStarted extends Message_Board {

    public final int boardShape;
    public final String[] playerNames;

    public Message_Board_GameStarted(String username, int boardId, int boardShape, String[] playerNames) {
        super(username, Message.MESSAGETYPE.BOARD_GAMESTARTED, boardId);
        this.boardShape = boardShape;
        this.playerNames = new String[playerNames.length];
        System.arraycopy(playerNames, 0, this.playerNames, 0, playerNames.length);
    }
}