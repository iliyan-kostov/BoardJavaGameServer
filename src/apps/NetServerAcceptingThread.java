package apps;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetServerAcceptingThread extends Thread {

    private final NetServer server;
    private final ServerSocket serverSocket;

    public NetServerAcceptingThread(NetServer server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while ((this.serverSocket != null) && (!(this.serverSocket.isClosed()))) {
            try {
                Socket socket = this.serverSocket.accept();
                NetServersideConnection connection = new NetServersideConnection(this.server, socket, null, this.server.getNextConnectionId());
                this.server.connectionsById.put(connection.id, connection);
                this.server.startConnection(connection);
            } catch (IOException ex) {
                Logger.getLogger(NetServerAcceptingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (this.server.serverSocket != null) {
            // stop the server in case of exception in the accepting thread:
            while ((this.serverSocket != null) && (!(this.serverSocket.isClosed()))) {
                try {
                    this.serverSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(NetServerAcceptingThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.server.stop();
        }
    }
}
