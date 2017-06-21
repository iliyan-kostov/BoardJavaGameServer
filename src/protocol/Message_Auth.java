package protocol;

/**
 * <p>
 * Съобщение, носещо данни за идентификация между клиент и сървър.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public abstract class Message_Auth extends Message {

    public Message_Auth(String username, Message.MESSAGETYPE messageType) {
        super(username, messageType);
    }
}
