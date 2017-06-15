package server.test.chat_01.handlers;

import java.io.IOException;

import server.IHandler;
import server.communication.ChatMessage;
import server.communication.Message;

public class MessageHandler implements IHandler {

	@Override
	public void handel(Message msg, Boolean authorize) {
		authorize = false;
		ChatMessage message = (ChatMessage)msg;
		try {
			server.impl.Connection.connections.get(message.getTo()).writeObject(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
