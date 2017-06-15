package server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import server.AbstrctClientExecutor;
import server.IListener;
import server.utils.BoardJavaGameServerConfig;
import server.utils.Logger;

public class DefaultListener implements IListener {
	
	protected ServerSocket serverSocket;
	protected ThreadPoolExecutor executor;
	
	protected boolean isRunning;
	
	protected String clientExecutorName = "";
	
	public DefaultListener(){
		executor = new ThreadPoolExecutor(
				BoardJavaGameServerConfig.CORE_POOL_SIZE,
				BoardJavaGameServerConfig.MAXIMUM_POOL_SIZE, 
				BoardJavaGameServerConfig.KEEP_ALIVE_TIME,
				BoardJavaGameServerConfig.BASE_TIME_UNIT, 
				new LinkedBlockingQueue<Runnable>()
		);
		isRunning = false;
	}

	public String getClientExecutorName() {
		return clientExecutorName;
	}

	public void setClientExecutorName(String clientExecutorName) {
		this.clientExecutorName = clientExecutorName;
	}

	@Override
	public void run() {
		isRunning = true;
		while(isRunning){
			 Socket socket = null;
             try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				Logger.error(e.getMessage());
			}
             AbstrctClientExecutor conn = null;
			try {
				if(socket != null){
					conn = (AbstrctClientExecutor)Class.forName(clientExecutorName).newInstance();
					conn.setSocket(socket);
				}
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			if(conn != null){
				executor.execute(conn);
			}
			
			if(serverSocket.isClosed()){
				isRunning = false;
			}
		}
	}
	
	public void setServerSocket(ServerSocket serverSocket){
		this.serverSocket = serverSocket;
	}

	public void stop(){
		executor.shutdown();
	}
}
