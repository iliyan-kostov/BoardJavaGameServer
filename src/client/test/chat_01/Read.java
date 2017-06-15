package client.test.chat_01;

import java.io.IOException;
import java.io.ObjectInputStream;

import server.communication.ChatMessage;

public class Read implements Runnable {
	
	private ObjectInputStream in;
	private boolean isRunning;
	
	public Read(ObjectInputStream in){
		this.in = in;
		isRunning = false;
	}
	
	
	@Override
	public void run() {
		isRunning = true;
		while(isRunning){
			ChatMessage msg = null;
			try {
				msg = (ChatMessage)in.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("----------------------------");
			System.out.println(msg.getForm()+" : "+msg.getMsg());
		}
		
	}

}
