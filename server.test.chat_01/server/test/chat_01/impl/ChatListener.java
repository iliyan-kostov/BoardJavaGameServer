package server.test.chat_01.impl;

import server.AbstrctClientExecutor;
import server.communication.Message;
import server.test.chat_01.heandlers.LoginHandler;
import server.test.chat_01.heandlers.MessageHandler;

public class ChatListener extends AbstrctClientExecutor {

	private LoginHandler login;
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
		login = new LoginHandler();
	}

}