package apps;

import game.board.Board;
import game.board.BoardCoords;
import game.lobby.PlayerStat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    public final static String CONNECTIONSTRING = "jdbc:ucanaccess:////D://Coding//Java//BoardJavaGameServer//database//BoardJavaGameServer.accdb";

    private final String connectionString;

    public Database(String connectionSting) {
        if (connectionSting != null) {
            this.connectionString = connectionSting;
        } else {
            this.connectionString = Database.CONNECTIONSTRING;
        }
        try (Connection conn = DriverManager.getConnection(this.connectionString)) {
            conn.setAutoCommit(false);
            {
                // Table "Users":
                try {
                    // Create "Users" table if not exists:
                    String string1
                            = "CREATE TABLE Users ("
                            + " Username CHAR(30) NOT NULL,"
                            + " Password CHAR(30) NOT NULL,"
                            + " PRIMARY KEY (Username));";
                    PreparedStatement statement1 = conn.prepareStatement(string1);
                    statement1.execute();
                    String string2
                            = "CREATE UNIQUE INDEX UsersIndex"
                            + " ON Users (Username);";
                    PreparedStatement statement2 = conn.prepareStatement(string2);
                    statement2.execute();
                } catch (SQLException ex) {
                    if (ex.getMessage().indexOf("already exists:") > 0) {
                        // Table "Users" already exists!
                    } else {
                        throw ex;
                    }
                }
                conn.commit();
            }
            {
                // Table "Games":
                try {
                    // Create "Games" table if not exists:
                    String string1
                            = "CREATE TABLE Games ("
                            + " BoardId INTEGER NOT NULL,"
                            + " BoardShape INTEGER NOT NULL,"
                            + " Winner CHAR(30) NOT NULL,"
                            + " PRIMARY KEY (BoardId),"
                            + " FOREIGN KEY (Winner) REFERENCES Users(Username));";
                    PreparedStatement statement1 = conn.prepareStatement(string1);
                    statement1.execute();
                    String string2
                            = "CREATE UNIQUE INDEX GamesIndex"
                            + " ON Games (BoardId);";
                    PreparedStatement statement2 = conn.prepareStatement(string2);
                    statement2.execute();
                } catch (SQLException ex) {
                    if (ex.getMessage().indexOf("already exists:") > 0) {
                        // Table "Games" already exists!
                    } else {
                        throw ex;
                    }
                }
                conn.commit();
            }
            {
                // Table "GamesPlayers":
                try {
                    // Create "GamesPlayers" table if not exists:
                    String string1
                            = "CREATE TABLE GamesPlayers ("
                            + " BoardId INTEGER NOT NULL,"
                            + " Position INTEGER NOT NULL,"
                            + " Username CHAR(30) NOT NULL,"
                            + " PRIMARY KEY (BoardId, Position),"
                            + " FOREIGN KEY (BoardId) REFERENCES Games(BoardId),"
                            + " FOREIGN KEY (Username) REFERENCES Users(Username));";
                    PreparedStatement statement1 = conn.prepareStatement(string1);
                    statement1.execute();
                    String string2
                            = "CREATE UNIQUE INDEX GamesPlayers"
                            + " ON GamesPlayers (BoardId, Position);";
                    PreparedStatement statement2 = conn.prepareStatement(string2);
                    statement2.execute();
                } catch (SQLException ex) {
                    if (ex.getMessage().indexOf("already exists:") > 0) {
                        // Table "GamesPlayers" already exists!
                    } else {
                        throw ex;
                    }
                }
                conn.commit();
            }
            {
                // Table "GamesMoves":
                try {
                    // Create "GamesMoves" table if not exists:
                    String string1 = "CREATE TABLE GamesMoves ("
                            + " BoardId INTEGER NOT NULL,"
                            + " MoveId INTEGER NOT NULL,"
                            + " FromRow INTEGER NOT NULL,"
                            + " FromCol INTEGER NOT NULL,"
                            + " ToRow INTEGER NOT NULL,"
                            + " ToCol INTEGER NOT NULL,"
                            + " PRIMARY KEY (BoardId, MoveId),"
                            + " FOREIGN KEY (BoardId) REFERENCES Games(BoardId))";
                    PreparedStatement statement1 = conn.prepareStatement(string1);
                    statement1.execute();
                    String string2
                            = "CREATE UNIQUE INDEX GamesMoves"
                            + " ON GamesMoves (BoardId, MoveId);";
                    PreparedStatement statement2 = conn.prepareStatement(string2);
                    statement2.execute();
                } catch (SQLException ex) {
                    if (ex.getMessage().indexOf("already exists:") > 0) {
                        // Table "GamesMoves" already exists!
                    } else {
                        throw ex;
                    }
                }
                conn.commit();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }

    /**
     * Checks in the database if the username-password pair is valid, or if it
     * can be used to register a new account.
     *
     * @param username the account username (login)
     *
     * @param password the account password
     *
     * @return if the login (or new registration) is successful
     */
    public synchronized boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(this.connectionString)) {
            conn.setAutoCommit(false);
            String string1
                    = "SELECT Username, Password"
                    + " FROM Users"
                    + " WHERE Username = ?;";
            PreparedStatement statement1 = conn.prepareStatement(string1);
            statement1.setString(1, username);
            ResultSet result = statement1.executeQuery();
            if (result.next()) {
                // username already registered, check password:
                String pw = result.getString("Password");
                return pw.equals(password);
            } else {
                // register the new username and password:
                String string2
                        = "INSERT INTO Users (Username, Password)"
                        + " VALUES (?, ?)";
                PreparedStatement statement2 = conn.prepareStatement(string2);
                statement2.setString(1, username);
                statement2.setString(2, password);
                statement2.execute();
                conn.commit();
                // check account - recursion:
                return authenticateUser(username, password);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
        return false;
    }

    public synchronized void recordGame(Board board) {
        try (Connection conn = DriverManager.getConnection(this.connectionString)) {
            conn.setAutoCommit(false);
            {
                // Record game:
                String winner = null;
                for (int i = 0; (i < board.usernames.length) && (winner == null); i++) {
                    if (board.activePlayers[i]) {
                        winner = board.usernames[i];
                    }
                }
                String string1
                        = "INSERT INTO Games (BoardId, BoardShape, Winner)"
                        + " VALUES (?, ?, ?)";
                PreparedStatement statement1 = conn.prepareStatement(string1);
                statement1.setInt(1, board.boardId);
                statement1.setInt(2, board.boardShape);
                statement1.setString(3, winner);
                statement1.execute();
            }
            {
                // Record players:
                String string1
                        = "INSERT INTO GamesPlayers (BoardId, Position, Username)"
                        + " VALUES (?, ?, ?)";
                PreparedStatement statement1 = conn.prepareStatement(string1);
                statement1.setInt(1, board.boardId);
                for (int i = 0; i < board.boardShape; i++) {
                    statement1.setInt(2, i);
                    statement1.setString(3, board.usernames[i]);
                    statement1.execute();
                }
            }
            {
                // Record moves:
                String string1
                        = "INSERT INTO GamesMoves (BoardId, MoveId, FromRow, FromCol, ToRow, ToCol)"
                        + " VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement1 = conn.prepareStatement(string1);
                statement1.setInt(1, board.boardId);
                BoardCoords from, to;
                for (int i = 0; i < board.movesFrom.size(); i++) {
                    statement1.setInt(2, i);
                    from = board.movesFrom.get(i);
                    to = board.movesTo.get(i);
                    statement1.setInt(3, from.row);
                    statement1.setInt(4, from.col);
                    statement1.setInt(5, to.row);
                    statement1.setInt(6, to.col);
                    statement1.execute();
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }

    public synchronized PlayerStat[] getPlayerStats(String[] usernames, int boardShape) {
        PlayerStat[] result = new PlayerStat[usernames.length];
        try (Connection conn = DriverManager.getConnection(this.connectionString)) {
            conn.setAutoCommit(false);
            for (int i = 0; i < usernames.length; i++) {
                String playerName = usernames[i];
                int gamesPlayed = 0;
                {
                    String string1
                            = "SELECT COUNT (BoardId, Position) AS Played"
                            + " FROM GamesPlayers"
                            + " WHERE Username = ?;";
                    PreparedStatement statement1 = conn.prepareStatement(string1);
                    statement1.setString(1, playerName);
                    ResultSet resultSet = statement1.executeQuery();
                    if (resultSet.next()) {
                        gamesPlayed = resultSet.getInt("Played");
                    }
                }
                int gamesWon = 0;
                {
                    String string1
                            = "SELECT COUNT (BoardId) AS Won"
                            + " FROM Games"
                            + " WHERE Winner = ?;";
                    PreparedStatement statement1 = conn.prepareStatement(string1);
                    statement1.setString(1, playerName);
                    ResultSet resultSet = statement1.executeQuery();
                    if (resultSet.next()) {
                        gamesWon = resultSet.getInt("Won");
                    }
                }
                int gamesLost = gamesPlayed - gamesWon;
                int rating = -1;    // TODO
                int position = -1;  // TODO
                result[i] = new PlayerStat(playerName, boardShape, gamesWon, gamesLost, rating, position);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
        return result;
    }

    /**
     * Returns the maximum existing board id in the database.
     *
     * @return the maximum existing board id in the database
     */
    public synchronized int getMaxBoardId() {
        try (Connection conn = DriverManager.getConnection(this.connectionString)) {
            conn.setAutoCommit(false);
            String string1
                    = "SELECT MAX(BoardId) AS Max"
                    + " FROM Games;";
            PreparedStatement statement1 = conn.prepareStatement(string1);
            ResultSet result = statement1.executeQuery();
            if (result.next()) {
                // returns the current max:
                return result.getInt("Max");
            } else {
                // no games regiistered yet:
                return 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
        return 1;
    }

    /*
    // TEST:
    public static void main(String[] args) {
        Database db = new Database(null);
        String[] usernames = new String[]{"user1", "user2", "user3"};
        for (String username : usernames) {
            db.authenticateUser(username, username);
        }

        {
            Board_Serverside board = new Board_Serverside(3, 25, usernames, null, null);
            board.movesFrom.add(new BoardCoords(4, 5));
            board.movesTo.add(new BoardCoords(4, 6));
            board.movesFrom.add(new BoardCoords(4, 6));
            board.movesTo.add(new BoardCoords(4, 7));
            board.movesFrom.add(new BoardCoords(4, 7));
            board.movesTo.add(new BoardCoords(3, 7));
            board.movesFrom.add(new BoardCoords(1, 2));
            board.movesTo.add(new BoardCoords(2, 2));
            db.recordGame(board);
        }

        {
            PlayerStat[] stats = new Database(null).getPlayerStats(usernames, 3);
            for (int i = 0; i < stats.length; i++) {
                PlayerStat current = stats[i];
                System.out.println(
                        current.playerName + " // "
                        + current.boardShape + " // "
                        + current.gamesWon + " // "
                        + current.gamesLost + " // "
                        + current.rating + " // "
                        + current.position);
            }
        }
    }
    /**/
}
