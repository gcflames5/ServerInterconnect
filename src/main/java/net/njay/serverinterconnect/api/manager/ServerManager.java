package net.njay.serverinterconnect.api.manager;

import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.packet.Packet;

import javax.net.ssl.SSLServerSocket;
import java.util.List;

public interface ServerManager {

    /**
     * Add an active connection
     *
     * @param connection newly created connectiom
     */
    public void submitConnection(TcpConnection connection);

    /**
     * Terminate a connection and remove it from the activeConnections list
     *
     * @param connection connection to be terminated
     */
    public void terminateConnection(TcpConnection connection);

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

    /** @return active socket */
    public SSLServerSocket getServerSocket();

    /** @return whether or not server manager is still running */
    public boolean isTerminated();

    /** @return all active connections */
    public List<TcpConnection> getConnections();

}
