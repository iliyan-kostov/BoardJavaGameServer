package game.board;

import java.util.HashMap;
import java.util.HashSet;
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

    public static final int SIZE_3 = 5; // брой редове на триъгълната дъска
    public static final int SIZE_4 = 5; // брой редове на квадратната дъска
    public static final int SIZE_6 = 7; // брой редове на шестоъгълната дъска

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

    /**
     * кои играчи са активни (не са победени, напуснали или невключили се в
     * играта)
     */
    public final boolean[] activePlayers;

    /**
     * множества от фигурите ан съответните играчи
     */
    public final HashMap<String, HashSet<Figure>> playerFigures;

    /**
     * фигури, разположени върху дъската - двумерен масив (2 координати x,y)
     */
    public final Figure[][] boardFigures;

    /**
     * брой редове на дъската (масива)
     */
    public final int boardSizeRows;

    /**
     * брой колони на дъската - не всички са запълнени !!!
     */
    public final int boardSizeCols;

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
            this.playerFigures = new HashMap<>();
            for (int i = 0; i < boardShape; i++) {
                this.playerFigures.put(usernames[i], new HashSet<>());
            }
            switch (boardShape) {
                case 3: {
                    this.boardSizeRows = Board.SIZE_3;
                    this.boardSizeCols = Board.SIZE_3 * 2 - 1;
                }
                break;
                case 4: {
                    this.boardSizeRows = Board.SIZE_4;
                    this.boardSizeCols = Board.SIZE_4;
                }
                break;
                case 6: {
                    this.boardSizeRows = Board.SIZE_6;
                    this.boardSizeCols = Board.SIZE_6;
                }
                break;
                default: {
                    throw new IllegalArgumentException();
                }
            }
            this.boardFigures = new Figure[this.boardSizeRows][];
            for (int i = 0; i < this.boardSizeRows; i++) {
                this.boardFigures[i] = new Figure[this.boardSizeCols];
                for (int j = 0; j < this.boardSizeCols; j++) {
                    this.boardFigures[i][j] = null;
                }
            }
            // поставяне на начални фигури на дъската:
            this.initFigures();
            // задаване на начално състояние на играта:
            this.currentPlayer = 0;
            this.movesFrom = new LinkedList<>();
            this.movesTo = new LinkedList<>();
        }
    }

    /**
     * Поставя начални фигури на дъската.
     */
    private void initFigures() {
        switch (this.boardShape) {
            case 3: {
                // player 0:
                {
                    int playerId = 0;
                    String playerName = this.usernames[playerId];
                    for (int i = 1; i < this.boardSizeRows - 1; i++) {
                        int row = i;
                        int col = 0;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
                // player 1:
                {
                    int playerId = 1;
                    String playerName = this.usernames[playerId];
                    for (int j = 1; j < this.boardSizeRows - 1; j++) {
                        int row = this.boardSizeRows - 1;
                        int col = 2 * j;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
                // player 2:
                {
                    int playerId = 2;
                    String playerName = this.usernames[playerId];
                    for (int i = 1; i < this.boardSizeRows - 1; i++) {
                        int row = i;
                        int col = 2 * i;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
            }
            break;
            case 4: {
                // player 0:
                {
                    int playerId = 0;
                    String playerName = this.usernames[playerId];
                    for (int i = 1; i < this.boardSizeRows - 1; i++) {
                        int row = i;
                        int col = 0;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
                // player 1:
                {
                    int playerId = 1;
                    String playerName = this.usernames[playerId];
                    for (int j = 1; j < this.boardSizeCols - 1; j++) {
                        int row = this.boardSizeRows - 1;
                        int col = j;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
                // player 2:
                {
                    int playerId = 2;
                    String playerName = this.usernames[playerId];
                    for (int i = 1; i < this.boardSizeRows - 1; i++) {
                        int row = i;
                        int col = this.boardSizeCols - 1;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
                // player 3:
                {
                    int playerId = 3;
                    String playerName = this.usernames[playerId];
                    for (int j = 1; j < this.boardSizeCols - 1; j++) {
                        int row = 0;
                        int col = j;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
            }
            break;
            case 6: {
                // player 0:
                {
                    int playerId = 0;
                    int sideLength = (this.boardSizeRows + 1) / 2;
                    String playerName = this.usernames[playerId];
                    for (int i = 1; i < sideLength - 1; i++) {
                        int row = i;
                        int col = 0;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
                // player 1:
                {
                    int playerId = 1;
                    int sideLength = (this.boardSizeRows + 1) / 2;
                    String playerName = this.usernames[playerId];
                    for (int i = sideLength; i < this.boardSizeRows - 1; i++) {
                        int row = i;
                        int col = 0;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
                // player 2:
                {
                    int playerId = 2;
                    int sideLength = (this.boardSizeRows + 1) / 2;
                    String playerName = this.usernames[playerId];
                    for (int j = 1; j < sideLength - 1; j++) {
                        int row = this.boardSizeRows - 1;
                        int col = j;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
                // player 3:
                {
                    int playerId = 3;
                    int sideLength = (this.boardSizeRows + 1) / 2;
                    int coef = (sideLength - 1) * 3;
                    String playerName = this.usernames[playerId];
                    for (int i = sideLength; i < this.boardSizeRows - 1; i++) {
                        int row = i;
                        int col = coef - i;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
                // player 4:
                {
                    int playerId = 4;
                    int sideLength = (this.boardSizeRows + 1) / 2;
                    String playerName = this.usernames[playerId];
                    for (int i = 1; i < sideLength - 1; i++) {
                        int row = i;
                        int col = i + sideLength - 1;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
                // player 5:
                {
                    int playerId = 5;
                    int sideLength = (this.boardSizeRows + 1) / 2;
                    String playerName = this.usernames[playerId];
                    for (int j = 1; j < sideLength - 1; j++) {
                        int row = 0;
                        int col = j;
                        this.boardFigures[row][col] = new Figure(new BoardCoords(row, col), playerName);
                        this.playerFigures.get(playerName).add(this.boardFigures[row][col]);
                    }
                }
            }
            break;
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public synchronized final void handleMessage(Message message) {
        switch (message.messageType) {
            case BOARD_GAMESTARTED: {
                try {
                    Message_Board_GameStarted msg = (Message_Board_GameStarted) message;
                    this.handleGameStarted(msg);
                } catch (ClassCastException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case BOARD_GAMESYNC: {
                try {
                    Message_Board_GameSync msg = (Message_Board_GameSync) message;
                    this.handleGameSync(msg);
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
