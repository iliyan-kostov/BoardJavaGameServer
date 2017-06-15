package server.impl;

import java.io.IOException;
import java.net.ServerSocket;

import server.IListener;
import server.IServer;
import server.utils.Logger;

public class Server implements IServer {
	
	private ServerSocket serverSocket;
	private IListener listener;
	
	private Thread listenerExecutor;
	
	public Server(int port){
		try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void start() {
		if(listener == null){
			Logger.error("There is no listener!");
		}
		else{
			listenerExecutor.start();
		}	
	}

	@Override
	public void stop() {
		listener.stop();
		try {
			listenerExecutor.join();
		} catch (InterruptedException e) {
			Logger.error(e.getMessage());
		}
	}

	@Override
	public void setListener(IListener listener) {
		this.listener = listener;
		this.listener.setServerSocket(serverSocket);
		listenerExecutor = new Thread(listener);
	}
}
