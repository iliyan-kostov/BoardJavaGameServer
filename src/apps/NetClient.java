package apps;

import game.board.Board_Clientside;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Group;
import protocol.Message;
import protocol.Message_Board_GameStarted;
import protocol.interfaces.IMessageHandler;
import protocol.interfaces.IMessageSender;

public class NetClient implements IMessageSender, IMessageHandler, PropertyChangeListener {

    public static final String EVENT_GAME_STARTED = "gameStarted";

    private final PropertyChangeSupport pcs;

    private final Socket socket;
    private final String username;
    private final String password;
    private NetClientsideConnection connection;
    // visual:
    private Board_Clientside board;

    public Group getBoardView() {
        return this.board.getBoardView();
    }

    public NetClient(Socket socket, String username, String password) {
        this.pcs = new PropertyChangeSupport(this);
        this.socket = socket;
        this.username = username;
        this.password = password;
        this.connection = null;
        this.board = null;
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
        switch (message.messageType) {
            case BOARD_GAMESTARTED: {
                try {
                    Message_Board_GameStarted msg = (Message_Board_GameStarted) message;
                    switch (msg.boardShape) {
                        case 3: {
                            this.board = new Board_Clientside(msg.boardShape, msg.boardId, msg.playerNames, this);
                        }
                        break;
                        case 4: {
                            this.board = new Board_Clientside(msg.boardShape, msg.boardId, msg.playerNames, this);
                        }
                        break;
                        case 6: {
                            this.board = new Board_Clientside(msg.boardShape, msg.boardId, msg.playerNames, this);
                        }
                        break;
                        default: {
                            throw new IllegalArgumentException();
                        }
                    }
                    this.board.handleMessage(message);
                    this.pcs.firePropertyChange(NetClient.EVENT_GAME_STARTED, false, true);
                } catch (ClassCastException ex) {
                    Logger.getLogger(NetClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case BOARD_MOVEFIGURES: {
                this.board.handleMessage(message);
            }
            break;
            case BOARD_ENDTURN: {
                this.board.handleMessage(message);
            }
            break;
            case BOARD_ENDGAME: {
                this.board.handleMessage(message);
            }
            break;
            case BOARD_SURRENDER: {
                this.board.handleMessage(message);
            }
            break;
            default: {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }
    }
}
