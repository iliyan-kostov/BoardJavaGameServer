package apps;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.Socket;
import protocol.Message;
import protocol.interfaces.IMessageHandler;
import protocol.interfaces.IMessageSender;

public class NetClient implements IMessageSender, IMessageHandler, PropertyChangeListener {

    private final PropertyChangeSupport pcs;

    private final Socket socket;
    private final String username;
    private final String password;
    private NetClientsideConnection connection;

    public NetClient(Socket socket, String username, String password) {
        this.pcs = new PropertyChangeSupport(this);
        this.socket = socket;
        this.username = username;
        this.password = password;
        this.connection = null;
    }

    public synchronized void startConnection() {
        if (this.connection == null) {
            this.connection = new NetClientsideConnection(this, socket, username, password);
            this.connection.addPropertyChangeListener(this);
            this.connection.start();
        }
    }

    public synchronized void stopConnection() {
        if (this.connection != null) {
            this.connection.stopConnection();
            this.connection = null;
        }
    }

    public synchronized boolean isRunning() {
        if ((this.connection != null) && (this.connection.isAlive())) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.pcs.addPropertyChangeListener(propertyChangeListener);

    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.pcs.removePropertyChangeListener(propertyChangeListener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.pcs.firePropertyChange(evt);
    }

    @Override
    public synchronized void sendMessage(Message message) {
        this.connection.sendMessage(message);
    }

    @Override
    public synchronized void handleMessage(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
