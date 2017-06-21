package protocol;

/**
 * <p>
 * Съобщение за синхронизиране на игра, в която потребителят участва.
 * <p>
 * Класът ще включва данни за синхронизиране на дъската и др.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Board_GameSync extends Message_Board {

    public Message_Board_GameSync(String username, int boardId) {
        super(username, Message.MESSAGETYPE.BOARD_GAMESYNC, boardId);
    }
}