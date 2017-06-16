package protocol;

/**
 * <p>
 * Съобщение за синхронизиране на лоби, в което потребителят участва.
 * <p>
 * Класът ще включва данни за синхронизиране на лобито и др.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Lobby_Sync extends Message_Lobby {

    public Message_Lobby_Sync(String username, int lobbyId) {
        super(username, Message.MESSAGETYPE.LOBBY_SYNC, lobbyId);
    }
}
