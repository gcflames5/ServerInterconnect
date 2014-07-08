package net.njay.serverinterconnect.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLServerSocket;

import net.njay.serverinterconnect.connection.TcpConnection;

public class TcpServerManager {

	private SSLServerSocket serversocket;
	private List<TcpConnection> activeConnections = new ArrayList<TcpConnection>();
	private IncomingConnectionThread connManager;
	
	private boolean terminated = false;

    /**
     * Constructor.
     *
     * @param serversocket socket to manage
     */
	public TcpServerManager(SSLServerSocket serversocket){
		this.serversocket = serversocket;
		startConnManager();
	}

    /**
     * Add an active connection
     *
     * @param connection newly created connectio
     */
	public void submitConnection(TcpConnection connection){
		this.activeConnections.add(connection);
	}

    /**
     * Begin process to handle new connections
     */
	private void startConnManager(){
		this.connManager = new IncomingConnectionThread(this);
		this.connManager.start();
	}

    /**
     * End all active connections
     *
     * @throws IOException
     */
	public void terminateConnections() throws IOException{
		terminated = true;
		for (TcpConnection connection : activeConnections)
			connection.terminate();
		serversocket.close();
	}

    /** @return active socket */
	public SSLServerSocket getServerSocket(){ return this.serversocket; }

    /** @return whether or not server manager is still running */
	public boolean isTerminated(){ return this.terminated; }

    /** @return all active connections */
	public List<TcpConnection> getConnections(){ return this.activeConnections; }
}
