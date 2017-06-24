package game.board;

import apps.GameManager;
import java.util.ArrayList;
import java.util.Iterator;
import protocol.Message_Board_EndTurn;
import protocol.Message_Board_MoveFigures;
import protocol.Message_Board_Surrender;

public abstract class GameLogic {

    protected final Board_Serverside board;
    protected final GameManager gameManager;

    public GameLogic(Board_Serverside board, GameManager gameManager) {
        this.board = board;
        this.gameManager = gameManager;
    }

    /**
     * Дали сочените координати са в рамките на дъската.
     *
     * @param boardCoords координати на поле
     *
     * @return дали сочените координати са в рамките на дъската
     */
    public abstract boolean isInsideBoard(BoardCoords boardCoords);

    /**
     * Проверява дали полетата със зададените координати са съседни по ръб
     * (страна). НЕ ПРОВЕРЯВА ДАЛИ ПОЛЕТАТА СА В РАМКИТЕ НА ДЪСКАТА !!!
     *
     * @param from координати на началното поле
     *
     * @param to координати на крайното поле
     *
     * @return дали полетата със зададените координати са съседни по ръб
     * (страна)
     */
    public abstract boolean isNextTo(BoardCoords from, BoardCoords to);

    /**
     * Извършва местене на фигурите според зададеното съобщение.
     *
     * @param message съобщение с инструкции за местене
     */
    public final synchronized void moveFigures(Message_Board_MoveFigures message) {
        if (!(this.isGameFinished())) {
            if (message != null) {
                ArrayList<BoardCoords> acceptedFrom = new ArrayList<>();
                ArrayList<BoardCoords> acceptedTo = new ArrayList<>();
                Iterator<BoardCoords> itFrom = message.from.iterator();
                Iterator<BoardCoords> itTo = message.to.iterator();
                // докато има още заявки за местене в съобщението и играта не е завършила:
                while (itFrom.hasNext() && itTo.hasNext() && !(this.isGameFinished())) {
                    // взема се следващата двойка координати (от, до):
                    BoardCoords from = itFrom.next();
                    BoardCoords to = itTo.next();
                    // ако координатите сочат полета в рамките на дъската, които са съседни:
                    if (this.isInsideBoard(from) && this.isInsideBoard(to) && this.isNextTo(from, to)) {
                        Figure figureFrom = this.board.boardFigures[from.row][from.col];
                        Figure figureTo = this.board.boardFigures[to.row][to.col];
                        // ако на съответните полета има фигури:
                        if (figureFrom != null && figureTo != null) {
                            // ако фигурата на началното поле принадлежи на изпратилия съобщението играч и той е на ход:
                            String usnMessage = message.username;
                            String usnFigureFrom = figureFrom.username;
                            String usnCurrent = this.board.usernames[this.board.currentPlayer];
                            if ((usnMessage.equals(usnCurrent)) && (usnMessage.equals(usnFigureFrom))) {
                                // ако фигурите са на различни играчи:
                                if (!((figureFrom.username).equals(figureTo.username))) {
                                    // регистрира се преместването в историята на играта (дъската):
                                    this.board.movesFrom.add(from);
                                    this.board.movesTo.add(to);
                                    // преместването се регистрира в съобщението за отговор към играчите:
                                    acceptedFrom.add(from);
                                    acceptedTo.add(to);
                                    // премахва се от игра фигурата на крайното поле:
                                    this.board.playerFigures.get(figureTo.username).remove(figureTo);
                                    // проверява се дали играчът, загубил фигура, има останали фигури:
                                    if (this.board.playerFigures.get(figureTo.username).isEmpty()) {
                                        int id = 0;
                                        while (!(this.board.usernames[id].equals(figureTo.username))) {
                                            id++;
                                        }
                                        this.board.activePlayers[id] = false;
                                    }
                                    // фигурата от началното поле се премества в крайното:
                                    this.board.boardFigures[to.row][to.col] = figureFrom;
                                    figureFrom.boardCoords = to;
                                    this.board.boardFigures[from.row][from.col] = null;
                                }
                            }
                        }
                    }
                }
                // разпращане на съобщението с изпълнените ходове към всички играчи:
                for (int i = 0; i < this.board.usernames.length; i++) {
                    this.board.sendMessage(new Message_Board_MoveFigures(this.board.usernames[i], this.board.boardId, acceptedFrom, acceptedTo));
                }
                // ако играта е приключила:
                if (this.isGameFinished()) {
                    // прекратяване на играта и регистриране на играта в базата данни:
                    this.gameManager.endGame(board);
                }
            }
        }
    }

    public final synchronized void endTurn(Message_Board_EndTurn message) {
        if (!(this.isGameFinished())) {
            if (message != null) {
                String usnMessage = message.username;
                String usnCurrent = this.board.usernames[this.board.currentPlayer];
                // ако играчът, изпратил съобщението, е на ход:
                if (usnMessage.equals(usnCurrent)) {
                    // предаване на хода на следващия активен играч:
                    do {
                        this.board.currentPlayer = (this.board.currentPlayer + 1) % (this.board.usernames.length);
                    } while (this.board.activePlayers[this.board.currentPlayer] == false);
                    // изпращане на съобщението за край на ред до всички играчи в играта:
                    for (int i = 0; i < this.board.usernames.length; i++) {
                        this.board.sendMessage(new Message_Board_EndTurn(this.board.usernames[i], this.board.boardId, usnMessage, this.board.usernames[this.board.currentPlayer]));
                    }
                }
            }
        }
    }

    public synchronized void surrender(Message_Board_Surrender message) {
        if (!(this.isGameFinished())) {
            if (message != null) {
                String usnMessage = message.username;
                message.playerSurrenders = usnMessage;
                for (int i = 0; i < this.board.usernames.length; i++) {
                    this.gameManager.sendMessage(new Message_Board_Surrender(this.board.usernames[i], this.board.boardId, usnMessage));
                }
                int userIndex = 0;
                while (!(this.board.usernames[userIndex].equals(usnMessage))) {
                    userIndex++;
                }
                // ако играчът е активен:
                if (this.board.activePlayers[userIndex] == true) {
                    this.board.activePlayers[userIndex] = false;
                    // ако играта е свършила:
                    if (this.board.gameLogic.isGameFinished()) {
                        // прекратяване на играта и регистриране на играта в базата данни:
                        this.gameManager.endGame(board);
                    } else {
                        // ако играчът, който се предава, е бил на ход:
                        if (this.board.currentPlayer == userIndex) {
                            // прекратяване на текущия ход:
                            this.board.gameLogic.endTurn(new Message_Board_EndTurn(usnMessage, this.board.boardId, usnMessage, null));
                        }
                    }
                }
            }
        }
        // TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public final synchronized boolean isGameFinished() {
        int activePlayersRemaining = 0;
        for (int i = 0; i < this.board.usernames.length; i++) {
            if (this.board.activePlayers[i]) {
                activePlayersRemaining++;
            }
        }
        if (activePlayersRemaining <= 1) {
            return true;
        } else {
            return false;
        }
    }
}
