package server;

import server.communication.Message;

public interface IHandler {
	
	public void handel(Message msg,Boolean authorize);
	
}
