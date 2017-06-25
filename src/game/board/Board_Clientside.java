package game.board;

import apps.NetClient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.Group;
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

public class Board_Clientside extends Board implements IMessageHandler, IMessageSender {

    public final NetClient client;
    public final Group boardView;
    public final Board_Clientside_Cell[][] boardCells;
    public BoardCoords from;
    public BoardCoords to;

    public Board_Clientside(int boardShape, int boardId, String[] usernames, NetClient client) {
        super(boardShape, boardId, usernames);
        this.client = client;
        this.boardView = new Group();
        this.boardCells = new Board_Clientside_Cell[this.boardSizeRows][this.boardSizeCols];
        for (int i = 0; i < this.boardSizeRows; i++) {
            for (int j = 0; j < this.boardSizeCols; j++) {
                this.boardCells[i][j] = new Board_Clientside_Cell(i, j, this);
            }
        }
        this.from = null;
        this.to = null;
        // поставяне на клетките от дъската по местата им:
        switch (boardShape) {
            case 3: {
                for (int i = 0; i < this.boardSizeRows; i++) {
                    for (int j = 0; j < this.boardSizeCols; j++) {
                        // ако полето е от дъската:
                        if (j <= 2 * i) {
                            this.boardView.getChildren().add(this.boardCells[i][j]);
                            this.boardCells[i][j].setRotate((j % 2) * 180.);
                            this.boardCells[i][j].setTranslateX((j - i) * Board_Clientside_Cell.SIDE_3 * 0.500);
                            this.boardCells[i][j].setTranslateY(i * Board_Clientside_Cell.SIDE_3 * 0.866);
                        }
                    }
                }
            }
            break;
            case 4: {
                for (int i = 0; i < this.boardSizeRows; i++) {
                    for (int j = 0; j < this.boardSizeCols; j++) {
                        this.boardView.getChildren().add(this.boardCells[i][j]);
                        //this.boardCells[i][j].setRotate(0.);
                        this.boardCells[i][j].setTranslateX(j * Board_Clientside_Cell.SIDE_4);
                        this.boardCells[i][j].setTranslateY(i * Board_Clientside_Cell.SIDE_4);
                    }
                }
            }
            break;
            case 6: {
                int rowLength = (this.boardSizeCols + 1) / 2;
                int dx = 0;
                int midRowId = (this.boardSizeRows - 1) / 2;
                // горна половина:
                for (int i = 0; i < midRowId + 1; i++, rowLength++, dx--) {
                    for (int j = 0; j < rowLength; j++) {
                        this.boardView.getChildren().add(this.boardCells[i][j]);
                        //this.boardCells[i][j].setRotate(0.);
                        this.boardCells[i][j].setTranslateX((2 * j + dx) * Board_Clientside_Cell.SIDE_6 * 0.866);
                        this.boardCells[i][j].setTranslateY(i * Board_Clientside_Cell.SIDE_6 * 1.500);
                    }
                }
                rowLength = rowLength - 2;
                dx = dx + 2;
                // долна половина:
                {
                    for (int i = midRowId + 1; i < this.boardSizeCols; i++, rowLength--, dx++) {
                        for (int j = 0; j < rowLength; j++) {
                            this.boardView.getChildren().add(this.boardCells[i][j]);
                            //this.boardCells[i][j].setRotate(0.);
                            this.boardCells[i][j].setTranslateX((2 * j + dx) * Board_Clientside_Cell.SIDE_6 * 0.866);
                            this.boardCells[i][j].setTranslateY(i * Board_Clientside_Cell.SIDE_6 * 1.500);
                        }
                    }
                }
            }
            break;
            default: {
                throw new IllegalArgumentException();
            }
        }
        // отразяване на позициите на фигурите по дъската:
        for (Map.Entry<String, HashSet<Figure>> entry : this.playerFigures.entrySet()) {
            String playerName = entry.getKey();
            int playerId = 0;
            while (!(this.usernames[playerId].equals(playerName))) {
                playerId++;
            }
            HashSet<Figure> figures = entry.getValue();
            if (figures != null) {
                Iterator<Figure> it = figures.iterator();
                while (it.hasNext()) {
                    Figure f = it.next();
                    this.boardCells[f.boardCoords.row][f.boardCoords.col].setFill(Board_Clientside_Cell.COLOR_PLAYERS[playerId]);
                }
            }
        }
    }

    public Group getBoardView() {
        return this.boardView;
    }

    @Override
    public synchronized void handleGameStarted(Message_Board_GameStarted message) {
        // boardView is generated in constructor !!!
    }

    @Override
    public synchronized void handleGameSync(Message_Board_GameSync message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void handleMoveFigures(Message_Board_MoveFigures message) {
        if (message != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ArrayList<BoardCoords> from = message.from;
                    ArrayList<BoardCoords> to = message.to;
                    if ((from != null) && (to != null)) {
                        Iterator<BoardCoords> itFrom = from.iterator();
                        Iterator<BoardCoords> itTo = to.iterator();
                        while (itFrom.hasNext() && itTo.hasNext()) {
                            BoardCoords c1 = itFrom.next();
                            BoardCoords c2 = itTo.next();
                            String playerMoves = boardFigures[c1.row][c1.col].username;
                            int playerId = 0;
                            while (!(usernames[playerId].equals(playerMoves))) {
                                playerId++;
                            }
                            boardCells[c1.row][c1.col].setFill(Board_Clientside_Cell.COLOR_BOARD);
                            boardCells[c2.row][c2.col].setFill(Board_Clientside_Cell.COLOR_PLAYERS[playerId]);
                            boardFigures[c2.row][c2.col] = boardFigures[c1.row][c1.col];
                            boardFigures[c1.row][c1.col] = null;
                        }
                    }
                }
            });
        }
    }

    @Override
    public synchronized void handleRemoveFigures(Message_Board_RemoveFigures message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void handleEndTurn(Message_Board_EndTurn message) {
        // TODO
        System.out.println("[Client] Received Message_Board_EndTurn !!! IMPLEMENT THE METHOD !!!");
        System.out.flush();
        // =======================================================================================
        // =======================================================================================
    }

    @Override
    public synchronized void handleEndGame(Message_Board_EndGame message) {
        // TODO
        System.out.println("[Client] Received Message_Board_EndGame !!! IMPLEMENT THE METHOD !!!");
        System.out.flush();
        // =======================================================================================
        // =======================================================================================
    }

    @Override
    public synchronized void handleSurrender(Message_Board_Surrender message) {
        // TODO
        System.out.println("[Client] Received Message_Board_Surrender !!! IMPLEMENT THE METHOD !!!");
        System.out.flush();
        // =======================================================================================
        // =======================================================================================
    }

    @Override
    public synchronized void sendMessage(Message message) {
        this.client.sendMessage(message);
    }
}
