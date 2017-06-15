package game.lobby;

import game.IMessageHandler;
import game.IMessageSender;
import game.protocol.Message_Lobby;

/**
 * <p>
 * Базов абстрактен клас за игрално лоби - група от играчи, които все още не са
 * в игра ("чат-канал").
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public abstract class Lobby implements IMessageSender, IMessageHandler {

    /**
     * <p>
     * Идентификатор на игралното лоби в рамките на системата.
     */
    public final int lobbyId;

    public Lobby(int lobbyId) {
        this.lobbyId = lobbyId;
    }
}
