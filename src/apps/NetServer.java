package apps;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocol.Message;
import protocol.Message_Auth_Login;
import protocol.interfaces.IMessageHandler;
import protocol.interfaces.IMessageSender;

public class NetServer implements IMessageSender, IMessageHandler {

    public final static String EVENT_IS_SERVER_RUNNING = "isServerRinning";
    public final static String EVENT_CONNECTION_STARTED = "connectionStarted";
    public final static String EVENT_CONNECTION_STOPPED = "connectionStopped";
    public final static String EVENT_CONNECTION_AUTHENTICATED = "connectionAuthenticated";
    public final static String EVENT_USER_LOGOUT = "userLogout";

    private final PropertyChangeSupport pcs;
    private final Database database;
    private final GameManager gameManager;

    private int port;
    protected ServerSocket serverSocket;
    private NetServerAcceptingThread acceptingThread;
    private boolean isServerRunning;
    private int nextConnectionId;

    protected final HashMap<Integer, NetServersideConnection> connectionsById;
    protected final HashMap<String, NetServersideConnection> connectionsByUsername;

    public NetServer() {
        this.pcs = new PropertyChangeSupport(this);
        this.database = new Database(null); // set string of leave null for default !!!
        this.gameManager = new GameManager(this);
        this.port = -1;
        this.serverSocket = null;
        this.acceptingThread = null;
        this.isServerRunning = false;
        this.nextConnectionId = 1;
        this.connectionsById = new HashMap<>();
        this.connectionsByUsername = new HashMap<>();
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.pcs.addPropertyChangeListener(propertyChangeListener);

    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.pcs.removePropertyChangeListener(propertyChangeListener);
    }

    protected synchronized int getNextConnectionId() {
        return this.nextConnectionId++;
    }

    private synchronized void setServerRunning(boolean isServerRunning) {
        boolean oldValue = this.isServerRunning;
        this.isServerRunning = isServerRunning;
        this.pcs.firePropertyChange(NetServer.EVENT_IS_SERVER_RUNNING, oldValue, this.isServerRunning);
    }

    public synchronized boolean isServerRunning() {
        return this.isServerRunning;
    }

    public synchronized int getPort() {
        return this.port;
    }

    /**
     * Starts the server using the specified local port.
     *
     * @param port the local port to start the server on
     */
    public synchronized void start(int port) {
        if ((this.serverSocket == null) && (this.acceptingThread == null) && !(this.isServerRunning)) {
            this.connectionsById.clear();
            this.connectionsByUsername.clear();
            this.nextConnectionId = 1;
            this.port = port;
            try {
                this.serverSocket = new ServerSocket(this.port);
                {
                    this.acceptingThread = new NetServerAcceptingThread(this, this.serverSocket);
                    this.acceptingThread.start();
                    this.setServerRunning(true);

                }
            } catch (IOException ex) {
                Logger.getLogger(NetServer.class.getName()).log(Level.SEVERE, null, ex);
                // stop the server:
                this.stop();
            }
        } else {
            throw new IllegalArgumentException("Server is already running!");
        }
    }

    /**
     * Stops the server (even if it's not running).
     */
    public synchronized void stop() {
        // close the server socket:
        while ((this.serverSocket != null) && (!(this.serverSocket.isClosed()))) {
            try {
                this.serverSocket.close();

            } catch (IOException ex1) {
                Logger.getLogger(NetServer.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        this.serverSocket = null;
        // close the accepting thread:
        while ((this.acceptingThread != null) && (this.acceptingThread.isAlive())) {
            this.acceptingThread.interrupt();
            try {
                this.acceptingThread.join();

            } catch (InterruptedException ex1) {
                Logger.getLogger(NetServer.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        this.acceptingThread = null;
        // close all active connections to clients:
        for (Map.Entry<Integer, NetServersideConnection> entry : connectionsById.entrySet()) {
            if ((entry != null) && (entry.getValue() != null)) {
                NetServersideConnection connection = entry.getValue();
                this.stopConnection(connection);
            }
        }
        for (Map.Entry<String, NetServersideConnection> entry : connectionsByUsername.entrySet()) {
            if ((entry != null) && (entry.getValue() != null)) {
                NetServersideConnection connection = entry.getValue();
                this.stopConnection(connection);
            }
        }
        this.connectionsById.clear();
        this.connectionsByUsername.clear();
        // set port to -1:
        this.port = -1;
        // set isRunning to false:
        this.setServerRunning(false);
    }

    public synchronized void stopConnection(NetServersideConnection connection) {
        if ((connection != null) && (connection.socket != null) && (!(connection.socket.isClosed()))) {
            while (!(connection.socket.isClosed())) {
                try {
                    connection.socket.close();

                } catch (IOException ex) {
                    Logger.getLogger(NetServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.connectionsById.remove(connection.id);
            if (connection.username != null) {
                this.connectionsByUsername.remove(connection.username);
                this.userLogout(connection.username);
            }
            this.pcs.firePropertyChange(NetServer.EVENT_CONNECTION_STOPPED, null, connection);
        }
    }

    public synchronized void startConnection(NetServersideConnection connection) {
        if (connection != null) {
            this.pcs.firePropertyChange(NetServer.EVENT_CONNECTION_STARTED, null, connection);
            connection.start();
        }
    }

    public synchronized void authenticateConnection(NetServersideConnection connection, Message_Auth_Login loginMessage) {
        if (loginMessage.username == null) {
            this.stopConnection(connection);
        } else {
            String login = loginMessage.username;
            String password = loginMessage.password;
            // check database:
            boolean loginMatchesPassword = this.database.authenticateUser(login, password);
            if (loginMatchesPassword) {
                NetServersideConnection existing = this.connectionsByUsername.get(login);
                if (existing != null) {
                    // do not allow multiple logins with the same username:
                    this.stopConnection(connection);
                } else {
                    connection.username = login;
                    this.connectionsByUsername.put(login, connection);
                    this.pcs.firePropertyChange(NetServer.EVENT_CONNECTION_AUTHENTICATED, null, connection);
                }
            } else {
                this.stopConnection(connection);
            }
        }
    }

    @Override
    public synchronized void sendMessage(Message message) {
        NetServersideConnection connection = this.connectionsByUsername.get(message.username);
        if (connection != null) {
            connection.sendMessage(message);
        }
    }

    @Override
    public synchronized void handleMessage(Message message) {
        switch (message.messageType) {
            case AUTH_LOGIN: {
                // handled by connection thread
            }
            break;
            case AUTH_LOGOUT: {
                this.userLogout(message.username);
            }
            break;
            case BOARD_ENDTURN: {
                this.gameManager.handleMessage(message);
            }
            break;
            case BOARD_MOVEFIGURES: {
                this.gameManager.handleMessage(message);
            }
            break;
            case BOARD_REMOVEFIGURES: {
                this.gameManager.handleMessage(message);
            }
            break;
            case BOARD_SURRENDER: {
                this.gameManager.handleMessage(message);
            }
            break;
            case LOBBY_NEWGAMEREQUEST: {
                this.gameManager.handleMessage(message);
            }
            break;
            case LOBBY_PLAYERSTATS: {
                // TODO
            }
            break;
            case LOBBY_RANKING: {
                // TODO
            }
            break;
            default: {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }
    }

    public synchronized void userLogout(String username) {
        if (username != null) {
            NetServersideConnection connection = this.connectionsByUsername.get(username);
            if (connection != null) {
                this.stopConnection(connection);
            }
            // Notify the client's active games and/or game queues, etc.:
            this.pcs.firePropertyChange(NetServer.EVENT_USER_LOGOUT, null, username);
        }
    }
}
