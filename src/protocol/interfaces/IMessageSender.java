package protocol.interfaces;

import protocol.Message;

/**
 * Описва възможността на клас да изпраща съобщения от типа
 * {@link protocol.Message}.
 *
 * @author iliyan-kostov <https://github.com/iliyan-kostov/>
 */
public interface IMessageSender {

    /**
     * Изпраща изходящо съобщение.
     *
     * @param message изходящо съобщение
     */
    public void sendMessage(Message message);
}
