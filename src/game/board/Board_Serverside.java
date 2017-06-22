package game.board;

import apps.GameManager;
import protocol.Message;
import protocol.Message_Board_EndGame;
import protocol.Message_Board_EndTurn;
import protocol.Message_Board_GameStarted;
import protocol.Message_Board_GameSync;
import protocol.Message_Board_MoveFigures;
import protocol.Message_Board_RemoveFigures;
import protocol.Message_Board_Surrender;

/**
 * <p>
 * Клас за дъската - от страна на сървъра.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public class Board_Serverside extends Board {

    private final GameManager gameManager;

    public Board_Serverside(int boardShape, int boardId, String[] usernames, GameManager gameManager) {
        super(boardShape, boardId, usernames);
        this.gameManager = gameManager;
    }

    @Override
    public synchronized void sendMessage(Message message) {
        this.gameManager.sendMessage(message);
    }

    @Override
    public void handleGameStarted(Message_Board_GameStarted message) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleGameSync(Message_Board_GameSync message) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void handleMoveFigures(Message_Board_MoveFigures message) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void handleRemoveFigures(Message_Board_RemoveFigures message) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void handleEndTurn(Message_Board_EndTurn message) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void handleEndGame(Message_Board_EndGame message) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void handleSurrender(Message_Board_Surrender message) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
