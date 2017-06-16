package protocol;

import game.lobby.PlayerStat;

/**
 * <p>
 * Клас за съобщения, съдържащи игрални статистики - име на играч и
 * съответстващите му статистики за различните режими на игра.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Lobby_PlayerStats extends Message_Lobby {

    public final String playerName;
    public PlayerStat[] playerStats;    // попълва се от сървъра

    public Message_Lobby_PlayerStats(String username, String playerName, int lobbyId) {
        super(username, Message.MESSAGETYPE.LOBBY_PLAYERSTATS, lobbyId);
        this.playerName = playerName;
        this.playerStats = null;
    }
}