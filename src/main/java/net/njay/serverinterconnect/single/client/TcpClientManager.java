package net.njay.serverinterconnect.single.client;

import java.io.IOException;

import javax.net.ssl.SSLSocket;

import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpSocketFactory;

/**
 * Handles the connection between a client and an outside source
 *
 * Manages a SSLSocket, TcpConnection, and host details
 */
public class TcpClientManager {

	private SSLSocket socket;
	private TcpConnection activeConnection;
	private String address;
	private int port;

    /**
     * Constructor.
     *
     * @param address host name to connect to
     * @param port port to connect to
     */
	public TcpClientManager(String address, int port){
		this.address = address;
		this.port = port;
	}

    /**
     * Create a SSLSocket and connect
     *
     * @throws IOException Connection failed
     */
	public void initialize() throws IOException{
		socket = TcpSocketFactory.generateSocket(address, port, false);
		activeConnection = new TcpConnection(socket);
	}

    /**
     * @return current TcpConnection
     */
	public TcpConnection getConnection(){ return this.activeConnection; }
	
}
