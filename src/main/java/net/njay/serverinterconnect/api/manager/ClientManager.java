package net.njay.serverinterconnect.api.manager;

import net.njay.serverinterconnect.connection.TcpConnection;

import java.io.IOException;

public interface ClientManager {

    /**
     * Create a SSLSocket and connect
     *
     * @throws java.io.IOException Connection failed
     */
    public void initialize() throws IOException;

    /**
     * @return current TcpConnection
     */
    public TcpConnection getConnection();

}
