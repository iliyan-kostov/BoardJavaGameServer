package protocol;

/**
 * <p>
 * Съобщение за изход от системата.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Auth_Logout extends Message_Auth {

    public Message_Auth_Logout(String username) {
        super(username, Message.MESSAGETYPE.AUTH_LOGOUT);
    }
}
