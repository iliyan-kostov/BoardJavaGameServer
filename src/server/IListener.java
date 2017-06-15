package server;

import java.net.ServerSocket;

public interface IListener extends Runnable {
	
	public void setServerSocket(ServerSocket serverSocket);
	public void stop();
}
