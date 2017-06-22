package game.board;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocol.Message;
import protocol.Message_Board_EndGame;
import protocol.Message_Board_EndTurn;
import protocol.Message_Board_GameStarted;
import protocol.Message_Board_GameSync;
import protocol.Message_Board_MoveFigures;
import protocol.Message_Board_RemoveFigures;
import protocol.Message_Board_Surrender;
import protocol.interfaces.IMessageHandler;
import protocol.interfaces.IMessageSender;

/**
 * <p>
 * Базов абстрактен клас за дъската.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public abstract class Board implements IMessageSender, IMessageHandler {

    /**
     * <p>
     * Брой страни на дъската.
     */
    public final int boardShape;

    /**
     * <p>
     * Идентификатор на дъската (играта) в рамките на системата.
     */
    public final int boardId;

    /**
     * <p>
     * Имена на играчите (login) - по ред на ходовете.
     */
    public final String[] usernames;
    public final boolean[] activePlayers;

    protected int currentPlayer;
    public LinkedList<BoardCoords> movesFrom;
    public LinkedList<BoardCoords> movesTo;

    public Board(int boardShape, int boardId, String[] usernames) {
        if ((boardShape != 3) && (boardShape != 4) && (boardShape != 6)) {
            throw new IllegalArgumentException();
        } else {
            this.boardShape = boardShape;
            this.boardId = boardId;
            this.usernames = new String[boardShape];
            for (int i = 0; i < boardShape; i++) {
                this.usernames[i] = usernames[i];
            }
            this.activePlayers = new boolean[boardShape];
            for (int i = 0; i < boardShape; i++) {
                this.activePlayers[i] = true;
            }
            this.currentPlayer = 0;
            this.movesFrom = new LinkedList<>();
            this.movesTo = new LinkedList<>();
        }
    }

    @Override
    public synchronized final void handleMessage(Message message) {
        switch (message.messageType) {
            case BOARD_GAMESYNC: {
                try {
                    Message_Board_GameSync msg = (Message_Board_GameSync) message;
                    this.handleGameSync(msg);
                } catch (ClassCastException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case BOARD_GAMESTARTED: {
                try {
                    Message_Board_GameStarted msg = (Message_Board_GameStarted) message;
                    this.handleGameStarted(msg);
                } catch (ClassCastException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case BOARD_MOVEFIGURES: {
                try {
                    Message_Board_MoveFigures msg = (Message_Board_MoveFigures) message;
                    this.handleMoveFigures(msg);
                } catch (ClassCastException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case BOARD_REMOVEFIGURES: {
                try {
                    Message_Board_RemoveFigures msg = (Message_Board_RemoveFigures) message;
                    this.handleRemoveFigures(msg);
                } catch (ClassCastException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case BOARD_ENDTURN: {
                try {
                    Message_Board_EndTurn msg = (Message_Board_EndTurn) message;
                    this.handleEndTurn(msg);
                } catch (ClassCastException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case BOARD_ENDGAME: {
                try {
                    Message_Board_EndGame msg = (Message_Board_EndGame) message;
                    this.handleEndGame(msg);
                } catch (ClassCastException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case BOARD_SURRENDER: {
                try {
                    Message_Board_Surrender msg = (Message_Board_Surrender) message;
                    this.handleSurrender(msg);
                } catch (ClassCastException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            default: {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }
    }

    public abstract void handleGameStarted(Message_Board_GameStarted message);

    public abstract void handleGameSync(Message_Board_GameSync message);

    public abstract void handleMoveFigures(Message_Board_MoveFigures message);

    public abstract void handleRemoveFigures(Message_Board_RemoveFigures message);

    public abstract void handleEndTurn(Message_Board_EndTurn message);

    public abstract void handleEndGame(Message_Board_EndGame message);

    public abstract void handleSurrender(Message_Board_Surrender message);
}
