package net.njay.serverinterconnect.api.manager;

import net.njay.serverinterconnect.api.connection.Connection;
import net.njay.serverinterconnect.api.packet.Packet;

import javax.net.ssl.SSLServerSocket;
import java.util.List;

public interface ServerManager {

    /**
     * Add an active connection
     *
     * @param connection newly created connectiom
     */
    public void submitConnection(Connection connection);

    /**
     * Terminate a connection and remove it from the activeConnections list
     *
     * @param connection connection to be terminated
     */
    public void terminateConnection(Connection connection);

    /**
     * Send packet to all connections
     *
     * @param p
     */
    public void broadcast(Packet p);

    /**
     * Begin process to handle new connections
     */
    public void startConnManager();

    /**
     * End all active connections
     *
     * @throws java.io.IOException
     */
    public void terminateConnections();

    /**
     * @return active socket
     */
    public SSLServerSocket getServerSocket();

    /**
     * @return whether or not server manager is still running
     */
    public boolean isTerminated();

    /**
     * @return all active connections
     */
    public List<Connection> getConnections();

}
