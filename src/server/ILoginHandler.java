package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface ILoginHandler extends IHandler {
	
	public void handel(ObjectInputStream in,ObjectOutputStream out);
}
