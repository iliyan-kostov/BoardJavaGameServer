package tests.chat_01.server;

import server.impl.DefaultListener;
import server.impl.Server;


public class AppMain {
	public static final String PATH_EXECUTOR = "server.test.chat_01.impl.ChatListener";
	
	public static void main(String[] args) {
		Server server = new Server(9000);
		DefaultListener listener = new DefaultListener();
		listener.setClientExecutorName(PATH_EXECUTOR);
		server.setListener(listener);
		System.out.println("Starting th sever.....");
		server.start();
		//server.stop();
	}
}
