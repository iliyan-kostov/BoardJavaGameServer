package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import server.communication.Message;
import server.utils.Logger;

public abstract class AbstrctClientExecutor implements Runnable {
	
	protected Socket socket;
	protected ArrayList<IHandler> handlers;
	protected boolean isRunning;
	
	protected ILoginHandler login;
    
	protected ObjectInputStream in;
	protected ObjectOutputStream out;

    public AbstrctClientExecutor() {
    	isRunning = false;
    	this.handlers = new ArrayList<IHandler>();
    	this.setHandlers();
    	this.setLogin();
    }
    
    public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
			this.out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			Logger.error(e.getMessage());
		}
		login.handel(in, out);
	}

	@Override
    public void run() {
		if(socket != null && handlers != null)
			isRunning = true;
        while(isRunning) {
        	Object obj = null;
			try {
				obj = in.readObject();
			} catch (ClassNotFoundException e) {
				Logger.error(e.getMessage());
				isRunning = false;
			} catch (IOException e) {
				Logger.error(e.getMessage());
				isRunning = false;
			}
        	Message msg = decode(obj);
        	if(msg != null){
        		Boolean authorize = new Boolean(true);
	        	for(IHandler h: handlers){
	        		h.handel(msg,authorize);
	        	}
        	}
        }
    }
    
    protected abstract Message decode(Object obj);
    public abstract void setHandlers();
    public abstract void setLogin();
    
	public void stopRunning() {
        isRunning = false;
        try {
            socket.close();
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }
}
