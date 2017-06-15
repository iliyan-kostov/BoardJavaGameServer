package tests.chat_01.server.handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;

import server.ILoginHandler;
import server.communication.Login;
import server.communication.Message;

public class LoginHandler implements ILoginHandler {

	@Override
	public void handel(Message msg, Boolean authorize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handel(ObjectInputStream in, ObjectOutputStream out) {
		Login log = null;
		try {
			log = (Login)in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.impl.Connection.connections.put(log.getName(),out);
	}
}
