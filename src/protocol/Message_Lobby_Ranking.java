package protocol;

import game.lobby.PlayerStat;

/**
 * <p>
 * Съобщение с данни за класация за определен режим на игра и горна/долна
 * граница.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Lobby_Ranking extends Message_Lobby {

    public final int boardShape;
    public final int minRanking;
    public final int maxRanking;
    public PlayerStat[] playerStats;    // попълва се от сървъра

    public Message_Lobby_Ranking(String username, int boardShape, int minRanking, int maxRanking, int lobbyId) {
        super(username, Message.MESSAGETYPE.LOBBY_RANKING, lobbyId);
        this.boardShape = boardShape;
        this.minRanking = minRanking;
        this.maxRanking = maxRanking;
        this.playerStats = null;
    }
}