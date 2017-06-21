package protocol;

/**
 * <p>
 * Съобщение за вход в системата - като съществуващ или като нов потребител.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public final class Message_Auth_Login extends Message_Auth {

    public final String password;

    public Message_Auth_Login(String username, String password) {
        super(username, Message.MESSAGETYPE.AUTH_LOGIN);
        this.password = password;
    }
}