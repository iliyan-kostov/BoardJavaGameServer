package apps;

import game.board.Board_Serverside;
import game.lobby.PlayerStat;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocol.Message;
import protocol.Message_Board;
import protocol.Message_Board_EndGame;
import protocol.Message_Board_GameStarted;
import protocol.Message_Board_Surrender;
import protocol.Message_Lobby_NewGameRequest;
import protocol.interfaces.IMessageHandler;
import protocol.interfaces.IMessageSender;

public class GameManager implements PropertyChangeListener, IMessageSender, IMessageHandler {

    protected final NetServer server;
    protected final HashMap<Integer, Board_Serverside> boardsById;          // boards - by id (id -> board)
    protected final HashMap<String, Board_Serverside> boardsByUsername;     // boards - by id (id -> board)
    protected final HashMap<Integer, LinkedList<String>> queueByMode;       // queues - by board shape (shape -> queue)
    protected final HashMap<String, Integer> queueByUser;                   // queues - by username (username -> board shape)
    protected int nextBoardId;

    public GameManager(NetServer server) {
        this.server = server;
        this.server.addPropertyChangeListener(this);
        this.boardsById = new HashMap<>();
        this.boardsByUsername = new HashMap<>();
        this.queueByMode = new HashMap<>();
        this.queueByMode.put(3, new LinkedList<>());
        this.queueByMode.put(4, new LinkedList<>());
        this.queueByMode.put(6, new LinkedList<>());
        this.queueByUser = new HashMap<>();
        // get next board id from database:
        this.nextBoardId = this.server.database.getMaxBoardId() + 1;
    }

    public synchronized void queueRemoveUser(String username) {
        Integer mode = this.queueByUser.get(username);
        if (mode != null) {
            this.queueByUser.remove(username);
            this.queueByMode.get(mode).remove(username);

        }
    }

    public synchronized void queueAddUser(String username, int boardShape) {
        if (this.boardsByUsername.get(username) == null) {
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
    }

    public synchronized void startGame(String[] usernames, int boardShape) {
        // TODO
        Board_Serverside board = new Board_Serverside(boardShape, this.nextBoardId, usernames, this, this.server);
        this.boardsById.put(board.boardId, board);
        for (int i = 0; i < usernames.length; i++) {
            this.boardsByUsername.put(usernames[i], board);
        }
        this.nextBoardId++;
        // send messages to the players in the game:
        Message_Board_GameStarted msg = new Message_Board_GameStarted(null, board.boardId, boardShape, usernames);
        for (int i = 0; i < boardShape; i++) {
            msg.username = usernames[i];
            this.sendMessage(msg);
        }
    }

    /**
     * Ends a game and records it in the database if it's finished according to
     * the game logic.
     *
     * @param board
     */
    public synchronized void endGame(Board_Serverside board) {
        // ако играта все още не е официално прекратена:
        if (this.boardsById.get(board.boardId) != null) {
            // вземане на данни за статистиките на играчите - преди края на играта:
            PlayerStat[] playerStatsOld = board.server.database.getPlayerStats(board.usernames, board.boardShape);
            // приемане на статистики след играта - еднакви с началните:
            PlayerStat[] playerStatsNew = playerStatsOld;
            // ако играта е приключила успешно (според логиката):
            if (board.gameLogic.isGameFinished()) {
                // регистриране на играта в базата данни:
                this.server.database.recordGame(board);
                // вземане на актуалните данни за статистиките на играчите - след края на играта:
                playerStatsNew = board.server.database.getPlayerStats(board.usernames, board.boardShape);
            }
            // разпращане на съобщението за завършилата игра към всички играчи:
            for (int i = 0; i < board.usernames.length; i++) {
                board.sendMessage(new Message_Board_EndGame(board.usernames[i], board.boardId, playerStatsOld, playerStatsNew));
            }
            // премахване на играта от списъка:
            this.boardsById.remove(board.boardId);
            // премахване на асоциацията на играчите с играта:
            for (int i = 0; i < board.usernames.length; i++) {
                this.boardsByUsername.remove(board.usernames[i]);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case NetServer.EVENT_USER_LOGOUT: {
                String playerSurrenders = (String) evt.getNewValue();
                Board_Serverside board = this.boardsByUsername.get(playerSurrenders);
                if (board != null) {
                    board.handleSurrender(new Message_Board_Surrender(playerSurrenders, board.boardId, playerSurrenders));
                }
            }
            break;
            default: {
            }
            break;
        }
    }

    @Override
    public synchronized void handleMessage(Message message) {
        switch (message.messageType) {
            case BOARD_ENDTURN: {
                try {
                    Message_Board msg = (Message_Board) message;
                    int boardId = msg.boardId;
                    this.boardsById.get(boardId).handleMessage(message);
                } catch (ClassCastException ex) {
                    Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case BOARD_MOVEFIGURES: {
                try {
                    Message_Board msg = (Message_Board) message;
                    int boardId = msg.boardId;
                    this.boardsById.get(boardId).handleMessage(message);
                } catch (ClassCastException ex) {
                    Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case BOARD_REMOVEFIGURES: {
                try {
                    Message_Board msg = (Message_Board) message;
                    int boardId = msg.boardId;
                    this.boardsById.get(boardId).handleMessage(message);
                } catch (ClassCastException ex) {
                    Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case BOARD_SURRENDER: {
                try {
                    Message_Board msg = (Message_Board) message;
                    int boardId = msg.boardId;
                    this.boardsById.get(boardId).handleMessage(message);
                } catch (ClassCastException ex) {
                    Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
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
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }
    }

    @Override
    public synchronized void sendMessage(Message message) {
        this.server.sendMessage(message);
    }
}
