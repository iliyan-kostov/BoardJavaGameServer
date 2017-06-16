package apps.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocol.Message_Auth_Login;

public class NetServer {

    private final PropertyChangeSupport pcs;

    private int port;
    protected ServerSocket serverSocket;
    private NetServerAcceptingThread acceptingThread;
    private boolean isServerRunning;
    private int nextConnectionId;

    private final HashMap<Integer, NetServersideConnection> connectionsById;
    private final HashMap<String, NetServersideConnection> connectionsByUsername;

    public NetServer() {
        this.pcs = new PropertyChangeSupport(this);
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
        this.pcs.firePropertyChange("isServerRunning", oldValue, this.isServerRunning);
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
        try {
            throw new UnsupportedOperationException("Closing active connections from server - not yet implemented !!!");
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(NetServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.connectionsById.clear();
        this.connectionsByUsername.clear();
        // set port to -1:
        this.port = -1;
        // set isRunning to false:
        this.setServerRunning(false);
    }

    public synchronized void stopConnection(NetServersideConnection connection) {
        while ((connection != null) && (connection.socket != null) && (!(connection.socket.isClosed()))) {
            try {
                connection.socket.close();
            } catch (IOException ex) {
                Logger.getLogger(NetServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            if (connection.username != null) {
                this.connectionsByUsername.remove(connection.username);
            }
            this.connectionsById.remove(connection.id);
            this.pcs.firePropertyChange("connectionStopped", null, connection);
        }
    }

    public synchronized void startConnection(NetServersideConnection connection) {
        if (connection != null) {
            this.pcs.firePropertyChange("connectionStarted", null, connection);
            connection.start();
        }
    }

    public synchronized void authenticateConnection(NetServersideConnection connection, Message_Auth_Login loginMessage) {
        if (loginMessage.username == null) {
            this.stopConnection(connection);
        } else {
            String login = loginMessage.username;
            String password = loginMessage.password;
            // check database !!!
            boolean loginMatchesPassword = true;
            if (loginMatchesPassword) {
                connection.username = login;
                NetServersideConnection existing = this.connectionsByUsername.get(login);
                this.stopConnection(existing);
                this.connectionsByUsername.remove(login);
                this.connectionsByUsername.put(login, connection);
                this.pcs.firePropertyChange("connectionAuthenticated", null, connection);
            } else {
                stopConnection(connection);
            }
        }
    }
}
