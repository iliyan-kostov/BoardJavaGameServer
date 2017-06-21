package apps;

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
                            + " Username CHAR(30) PRIMARY KEY,"
                            + " Password CHAR(30) NOT NULL);";
                    PreparedStatement statement1 = conn.prepareStatement(string1);
                    statement1.execute();
                } catch (SQLException ex) {
                    if (ex.getMessage().indexOf("already exists:") > 0) {
                        // Table "Users" already exists!
                        //System.out.println("INFO: Table \"Users\" already exists.");
                    } else {
                        throw ex;
                    }
                }
            }
            conn.commit();
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
}
