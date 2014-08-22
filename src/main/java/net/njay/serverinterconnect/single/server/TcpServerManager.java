package net.njay.serverinterconnect.single.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLServerSocket;

import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.packet.Packet;

public class TcpServerManager {

	protected SSLServerSocket serversocket;
    protected List<TcpConnection> activeConnections = new ArrayList<TcpConnection>();
    protected IncomingConnectionThread connManager;
	
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
     * Constructor.
     *
     * @param serversocket socket to manage
     */
    public TcpServerManager(SSLServerSocket serversocket, boolean start){
        this.serversocket = serversocket;
        if (start) startConnManager();
    }

    /**
     * Add an active connection
     *
     * @param connection newly created connectiom
     */
	public void submitConnection(TcpConnection connection){
		this.activeConnections.add(connection);
	}

    /**
     * Terminate a connection and remove it from the activeConnections list
     *
     * @param connection connection to be terminated
     */
    public void terminateConnection(TcpConnection connection){
        connection.terminate();
        activeConnections.remove(connection);
    }

    public void broadcast(Packet p){
        for (TcpConnection conn : getConnections())
            conn.sendPacket(p);
    }

    /**
     * Begin process to handle new connections
     */
    protected void startConnManager(){
		this.connManager = new IncomingConnectionThread(this);
		this.connManager.start();
	}

    /**
     * End all active connections
     *
     * @throws IOException
     */
	public void terminateConnections() {
		terminated = true;
		for (TcpConnection connection : activeConnections)
			connection.terminate();
        activeConnections.clear();
		try{
            serversocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
	}

    /** @return active socket */
	public SSLServerSocket getServerSocket(){ return this.serversocket; }

    /** @return whether or not server manager is still running */
	public boolean isTerminated(){ return this.terminated; }

    /** @return all active connections */
	public List<TcpConnection> getConnections(){ return this.activeConnections; }
}
