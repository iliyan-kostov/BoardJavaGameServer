package game.protocol;

/**
 * <p>
 * Съобщение, носещо данни за определено игрално лоби ("чат-канал" с играчи
 * извън игра).
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public abstract class Message_Lobby extends Message {

    public final int lobbyId;

    public Message_Lobby(String username, Message.MESSAGETYPE messageType, int lobbyId) {
        super(username, messageType);
        this.lobbyId = lobbyId;
    }
}
