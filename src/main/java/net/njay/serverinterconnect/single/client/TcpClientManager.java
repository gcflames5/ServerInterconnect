package net.njay.serverinterconnect.single.client;

import net.njay.serverinterconnect.api.connection.Connection;
import net.njay.serverinterconnect.api.manager.ClientManager;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpSocketFactory;

import javax.net.ssl.SSLSocket;
import java.io.IOException;

/**
 * Handles the connection between a client and an outside source
 * <p/>
 * Manages a SSLSocket, TcpConnection, and host details
 */
public class TcpClientManager implements ClientManager {

    protected SSLSocket socket;
    protected Connection activeConnection;
    protected String address;
    protected int port;

    /**
     * Constructor.
     *
     * @param address host name to connect to
     * @param port    port to connect to
     */
    public TcpClientManager(String address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * Create a SSLSocket and connect
     *
     * @throws IOException Connection failed
     */
    public void initialize() throws IOException {
        socket = TcpSocketFactory.generateSocket(address, port, false);
        activeConnection = new TcpConnection(socket);
    }

    /**
     * @return current TcpConnection
     */
    public Connection getConnection() {
        return this.activeConnection;
    }

}
