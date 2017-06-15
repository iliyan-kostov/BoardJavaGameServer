package client.test.chat_01;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import server.communication.Login;

public class AppTest {
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private String user;
	private String friend;
	
	public AppTest(){
		this.connectServer();
		this.writeNames();
		Login log = new Login();
		log.setName(user);
		try {
			out.writeObject(log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Write w = new Write(out, friend, user);
		Read r = new Read(in);
		
		Thread t1 = new Thread(w);
		Thread t2 = new Thread(r);
		
		t1.start();
		t2.start();
	}
	
	private void writeNames() {
		Scanner sc = new Scanner(System.in);
		System.out.print("User: ");
		user = sc.next();
		System.out.print("Friend: ");
		friend = sc.next();
		
	}

	private void connectServer(){
		String serverAddres = "127.0.0.1";
		try {
			Socket socket = new Socket(serverAddres,9000);
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new AppTest();
	}

}
