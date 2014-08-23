package net.njay.serverinterconnect.api.connection;

import net.njay.serverinterconnect.api.packet.Packet;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Queue;

public abstract class Connection extends Thread {

    /**
     * Adds a transferable to the sending queue, queue is sent if connection is ready
     *
     * @param packet
     */
    public abstract void sendPacket(Packet packet);

    /**
     * Start the write and read threads
     */
    public abstract void startThreads();

    /**
     * Safely terminate read and write threads, close socket
     */
    public abstract void terminate();

    /**
     * @return whether the TcpConnection is terminated (socket disconnected, monitor threads not running)
     */
    public abstract boolean isTerminated();

    /**
     * @return inputstream of socket
     */
    public abstract DataInputStream inputStream();

    /**
     * @return outputstream of socket
     */
    public abstract DataOutputStream outputStream();

    /**
     * @return queue of messages to be sent
     */
    public abstract Queue<Packet> getQueue();

    /**
     * @return Thread that writes packets to the outputstream
     */
    public abstract Thread getWriteThread();

    /**
     * @return Thread that reads packets from the inputstream
     */
    public abstract Thread getReadThread();

    /**
     * @return current socket
     */
    public abstract SSLSocket getSocket();

}