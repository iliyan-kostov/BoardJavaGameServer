package apps;

import game.board.Board;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocol.Message;
import protocol.Message_Lobby_NewGameRequest;
import protocol.interfaces.IMessageHandler;

public class GameManager implements PropertyChangeListener, IMessageHandler {

    protected final NetServer server;
    protected final HashMap<Integer, Board> boards;                     // boards - by id (id -> board)
    protected final HashMap<Integer, LinkedList<String>> queueByMode;   // queues - by board shape (shape -> queue)
    protected final HashMap<String, Integer> queueByUser;               // queues - by username (username -> board shape)

    public GameManager(NetServer server) {
        this.server = server;
        this.server.addPropertyChangeListener(this);
        this.boards = new HashMap<>();
        this.queueByMode = new HashMap<>();
        this.queueByMode.put(3, new LinkedList<>());
        this.queueByMode.put(4, new LinkedList<>());
        this.queueByMode.put(6, new LinkedList<>());
        this.queueByUser = new HashMap<>();
    }

    public synchronized void queueRemoveUser(String username) {
        Integer mode = this.queueByUser.get(username);
        if (mode != null) {
            this.queueByUser.remove(username);
            this.queueByMode.get(mode).remove(username);

        }
    }

    public synchronized void queueAddUser(String username, int boardShape) {
        if ((boardShape == 3) || (boardShape == 4) || (boardShape == 6)) {
            // remove user from current queue:
            this.queueRemoveUser(username);
            // queue user for the new mode:
            this.queueByUser.put(username, boardShape);
            this.queueByMode.get(boardShape).add(username);
            // check if a game has to be created:
            if (this.queueByMode.get(boardShape).size() >= boardShape) {
                // start a new game:
                String[] usernames = new String[boardShape];
                for (int i = 0; i < boardShape; i++) {
                    String un = this.queueByMode.get(boardShape).remove();
                    this.queueRemoveUser(un);
                    usernames[i] = un;
                }
                this.startGame(usernames, boardShape);
            }
        }
    }

    public synchronized void startGame(String[] usernames, int boardShape) {
        // TODO
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.messageType) {
            case LOBBY_NEWGAMEREQUEST: {
                try {
                    Message_Lobby_NewGameRequest newGameRequest = (Message_Lobby_NewGameRequest) message;
                    this.queueAddUser(newGameRequest.username, newGameRequest.boardShape);
                } catch (ClassCastException ex) {
                    Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            default: {
            }
            break;
        }
    }
}
