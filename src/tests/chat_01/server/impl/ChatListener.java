package tests.chat_01.server.impl;

import server.AbstrctClientExecutor;
import server.communication.Message;
import tests.chat_01.server.handlers.LoginHandler;
import tests.chat_01.server.handlers.MessageHandler;

public class ChatListener extends AbstrctClientExecutor {

	private MessageHandler hand;
	
	@Override
	protected Message decode(Object obj) {
		return (Message)obj;
	}

	@Override
	public void setHandlers() {
		hand = new MessageHandler();
		this.handlers.add(hand);
		
	}

	@Override
	public void setLogin() {
		this.login = new LoginHandler();
	}

}
