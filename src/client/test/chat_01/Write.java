package client.test.chat_01;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import server.communication.ChatMessage;

public class Write implements Runnable {

	private ObjectOutputStream out;
	private boolean isRunning;
	private String f;
	private String u;
	private Scanner sc;
	
	
	
	public Write(ObjectOutputStream out, String f,String u) {
		super();
		this.out = out;
		this.f = f;
		this.u = u;
		this.sc = new Scanner(System.in);
		isRunning = false;
	}

	@Override
	public void run() {
		isRunning = true;
		while(isRunning){
			String m = sc.nextLine();
			ChatMessage msg = new ChatMessage(u, f, m);
			
			try {
				out.writeObject(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
