package server;
/**
 * TCP/IP representation of server
 * @author Nikolay Nikolov 
 *
 */
public interface IServer {
	
	public void start();
	
	public void stop();
	
	public void setListener(IListener listener);
}
