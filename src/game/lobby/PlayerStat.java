package game.lobby;

import java.io.Serializable;

/**
 * <p>
 * Игрални статистики за потребител за определен режим на игра:
 * <ul>
 * <li>име на играч
 * <li>тип дъска (брой страни на дъската)
 * <li>брой победи
 * <li>брой загуби
 * <li>рейтинг
 * <li>класиране
 * </ul>
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public class PlayerStat implements Serializable {

    public final String playerName;
    public final int boardShape;
    public final int gamesWon;
    public final int gamesLost;
    public final int rating;
    public final int position;

    public PlayerStat(String playerName, int boardshape, int gamesWon, int gamesLost, int rating, int position) {
        this.playerName = playerName;
        this.boardShape = boardshape;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.rating = rating;
        this.position = position;
    }
}
