package protocol;

/**
 * <p>
 * Заявка за нова игра от определен режим.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Lobby_NewGameRequest extends Message_Lobby {

    public final int boardShape;

    public Message_Lobby_NewGameRequest(String username, int boardShape, int lobbyId) {
        super(username, Message.MESSAGETYPE.LOBBY_NEWGAMEREQUEST, lobbyId);
        this.boardShape = boardShape;
    }
}