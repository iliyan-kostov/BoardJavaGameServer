package apps.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocol.Message;
import protocol.Message_Auth_Login;
import protocol.Message_Auth_Logout;
import protocol.interfaces.IMessageHandler;
import protocol.interfaces.IMessageSender;

public class NetClientsideConnection extends Thread implements IMessageSender, IMessageHandler {

    private final PropertyChangeSupport pcs;

    protected final Socket socket;
    protected String username;
    protected String password;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public NetClientsideConnection(Socket socket, String username, String password) {
        super();
        this.pcs = new PropertyChangeSupport(this);
        this.socket = socket;
        this.username = username;
        this.password = password;
        this.outputStream = null;
        this.inputStream = null;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.pcs.addPropertyChangeListener(propertyChangeListener);

    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.pcs.removePropertyChangeListener(propertyChangeListener);
    }

    @Override
    public void run() {
        try {
            this.inputStream = new ObjectInputStream(this.socket.getInputStream());
            this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
            // authenticate:
            this.sendMessage(new Message_Auth_Login(this.username, this.password));
            this.sendMessage(new Message_Auth_Logout(this.username));
            // loop:
            boolean keepRunning = true;
            while (keepRunning && (this.inputStream != null) && (!(this.socket.isClosed()))) {
                try {
                    Message input = (Message) this.inputStream.readObject();
                    this.handleMessage(input);
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(NetClientsideConnection.class.getName()).log(Level.SEVERE, null, ex);
                    keepRunning = false;
                    //this.stopConnection();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(NetClientsideConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        // stop connection:
        this.stopConnection();
    }

    public synchronized void stopConnection() {
        while (!(this.socket.isClosed())) {
            try {
                this.socket.close();
            } catch (IOException ex) {
                Logger.getLogger(NetServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.inputStream = null;
        this.outputStream = null;
        this.pcs.firePropertyChange("isClientRunning", true, false);
    }

    @Override
    public synchronized void sendMessage(Message message) {
        try {
            this.outputStream.writeObject(message);
            this.outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetClientsideConnection.class.getName()).log(Level.SEVERE, null, ex);
            this.stopConnection();
        }
    }

    @Override
    public synchronized void handleMessage(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
