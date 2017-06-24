package game.board;

import apps.NetClient;
import javafx.scene.Group;
import javafx.scene.control.Label;
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

    public Board_Clientside(int boardShape, int boardId, String[] usernames, NetClient client) {
        super(boardShape, boardId, usernames);
        this.client = client;
        this.boardView = new Group();
        switch (boardShape) {
            case 3: {
            }
            break;
            case 4: {
            }
            break;
            case 6: {
            }
            break;
        }
    }

    public Group getBoardView() {
        return this.boardView;
    }

    @Override
    public synchronized void handleGameStarted(Message_Board_GameStarted message) {
        // TODO
        this.boardView.getChildren().clear();
        this.boardView.getChildren().add(new Label("INSERT BOARD VIEW HERE !!!"));
        System.out.println("[Client] Received Message_Board_GameStarted !!! IMPLEMENT THE METHOD !!!");
        System.out.flush();
        // =======================================================================================
        // =======================================================================================
    }

    @Override
    public synchronized void handleGameSync(Message_Board_GameSync message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void handleMoveFigures(Message_Board_MoveFigures message) {
        // TODO
        System.out.println("[Client] Received Message_Board_MoveFigures !!! IMPLEMENT THE METHOD !!!");
        System.out.flush();
        // =======================================================================================
        // =======================================================================================
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
