package protocol.interfaces;

/**
 * Описва възможността на клас (за връзка) да изпраща и да обработва съобщения
 * от типа {@link protocol.Message}, както и връзката да бъде стартирана и
 * прекъсвана.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public interface IConnection extends IMessageSender, IMessageHandler {

    /**
     * Изпълнява се за стартиране на връзката.
     */
    public void startConnection();

    /**
     * Изпълнява се за прекъсване на връзката.
     */
    public void stopConnection();
}
