package net.njay.serverinterconnect.single.server;

import net.njay.serverinterconnect.api.connection.Connection;
import net.njay.serverinterconnect.api.manager.ServerManager;
import net.njay.serverinterconnect.api.packet.Packet;

import javax.net.ssl.SSLServerSocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TcpServerManager implements ServerManager{

	protected SSLServerSocket serversocket;
    protected List<Connection> activeConnections;
    protected IncomingConnectionThread connManager;
	
	private boolean terminated = false;

    /**
     * Constructor.
     *
     * @param serversocket socket to manage
     */
	public TcpServerManager(SSLServerSocket serversocket){
		this.serversocket = serversocket;
        this.activeConnections = new ArrayList<Connection>();
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
	public void submitConnection(Connection connection){
		this.activeConnections.add(connection);
	}

    /**
     * Terminate a connection and remove it from the activeConnections list
     *
     * @param connection connection to be terminated
     */
    public void terminateConnection(Connection connection){
        connection.terminate();
        activeConnections.remove(connection);
    }

    public void broadcast(Packet p){
        for (Connection conn : getConnections())
            conn.sendPacket(p);
    }

    /**
     * Begin process to handle new connections
     */
    public void startConnManager(){
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
		for (Connection connection : activeConnections)
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
	public List<Connection> getConnections(){ return this.activeConnections; }
}
