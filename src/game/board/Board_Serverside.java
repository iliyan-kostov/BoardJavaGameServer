package game.board;

import apps.GameManager;
import protocol.Message;

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
    public synchronized void handleMessage(Message message) {
        switch (message.messageType) {
            case BOARD_ENDTURN: {
                // TODO
            }
            break;
            case BOARD_MOVEFIGURES: {
                // TODO
            }
            break;
            case BOARD_REMOVEFIGURES: {
                // TODO
            }
            break;
            case BOARD_SURRENDER: {
                // TODO
            }
            break;
            default: {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }
    }

    @Override
    public synchronized void userLogout(String username) {
        // TODO
    }
}
