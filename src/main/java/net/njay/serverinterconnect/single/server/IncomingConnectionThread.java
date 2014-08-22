package net.njay.serverinterconnect.single.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import net.njay.serverinterconnect.connection.TcpConnection;

/**
 * Takes incoming connections, checks them for SSL viability, and then passes them on to a TcpServerManager
 */
public class IncomingConnectionThread extends Thread{

	protected TcpServerManager manager;
    protected SSLServerSocket serversocket;

    /**
     * Constructor.
     *
     * @param manager server manager that new connections will be passed on to
     */
	public IncomingConnectionThread(TcpServerManager manager){
		this.manager = manager;
		this.serversocket = manager.getServerSocket();
	}
	
	@Override
	public void run(){
		while (!manager.isTerminated()) {
            if (!waitForConnection()) break;
        }
	}

    public boolean waitForConnection(){
        try {
            Socket incomingConn = serversocket.accept();
            if (!(incomingConn instanceof SSLSocket))
                throw new RuntimeException("Non-SSL Connection detected! Rejecting " + incomingConn.getInetAddress());
            TcpConnection tcpConn = new TcpConnection((SSLSocket)incomingConn);
            manager.submitConnection(tcpConn);
        } catch(SocketException e){
            System.err.println("Socket closed... terminating listening thread.");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}