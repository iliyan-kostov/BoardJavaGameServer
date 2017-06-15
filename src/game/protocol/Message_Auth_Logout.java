package game.protocol;

/**
 * <p>
 * Съобщение за изход от системата.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Auth_Logout extends Message_Auth {

    public final String password;

    public Message_Auth_Logout(String username, String password) {
        super(username, Message.MESSAGETYPE.AUTH_LOGOUT);
        this.password = password;
    }
}