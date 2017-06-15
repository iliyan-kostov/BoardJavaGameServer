package game.protocol;

/**
 * <p>
 * Съобщение, известяващо край на хода на текущия играч и име на следващия
 * играч, който е на ход.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Board_EndTurn extends Message_Board {

    public final String playerTurnEnds;
    public final String playerTurnStarts;

    public Message_Board_EndTurn(String username, int boardId, String playerTurnEnds, String playerTurnStarts) {
        super(username, Message.MESSAGETYPE.BOARD_ENDTURN, boardId);
        this.playerTurnEnds = playerTurnEnds;
        this.playerTurnStarts = playerTurnStarts;
    }
}