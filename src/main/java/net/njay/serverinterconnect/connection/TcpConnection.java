package net.njay.serverinterconnect.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.net.ssl.SSLSocket;

import net.njay.serverinterconnect.packet.Packet;

public class TcpConnection extends Thread{

	private SSLSocket socket;
	private Thread writeThread;
	private Thread readThread;
	private DataInputStream in;
	private DataOutputStream out;
	private BlockingQueue<Packet> sendQueue;
	
	private boolean terminated = false;

    /**
     * Create a TcpConnection with default read and write threads
     *
     * @param socket connected socket
     * @throws IOException
     */
	public TcpConnection(SSLSocket socket) throws IOException{
		this.socket = socket;
		this.writeThread = new TcpWriteThread(this);
		this.readThread = new TcpReadThread(this);
		this.in = new DataInputStream(socket.getInputStream());
		this.out = new DataOutputStream(socket.getOutputStream());
		this.sendQueue = new LinkedBlockingDeque<Packet>();
		startThreads();
	}

    /**
     * Create a TcpConnection with custom read and write threads
     *
     * @param socket
     * @param writeThread thread to handle writing packets
     * @param readThread thread to handle reading packets
     * @throws IOException
     */
    public TcpConnection(SSLSocket socket, TcpWriteThread writeThread, TcpReadThread readThread) throws IOException{
        this.socket = socket;
        this.writeThread = writeThread;
        this.readThread = readThread;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.sendQueue = new LinkedBlockingDeque<Packet>();
        startThreads();
    }

    /**
     * Adds a packet to the sending queue, queue is sent if connection is ready
     *
     * @param packet
     */
	public void sendPacket(Packet packet){ sendQueue.add(packet); }

    /**
     * Start the write and read threads
     */
	public void startThreads(){
		if (writeThread != null)
			writeThread.start();
		if (readThread != null)
			readThread.start();
	}

    /**
     * Safely terminate read and write threads, close socket
     */
	public void terminate(){
		try {
			writeThread = null;
			readThread = null;
			terminated = true;
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * @return whether the TcpConnection is terminated (socket disconnected, monitor threads not running)
     */
	public boolean isTerminated(){ return this.terminated; }

    /**
     * @return inputstream of socket
     */
	public DataInputStream inputStream(){ return this.in; }

    /**
     * @return outputstream of socket
     */
	public DataOutputStream outputStream(){ return this.out; }

    /**
     * @return queue of messages to be sent
     */
	public BlockingQueue<Packet> getQueue(){ return this.sendQueue; }

    /**
     * @return Thread that writes packets to the outputstream
     */
	public Thread getWriteThread(){ return this.writeThread; }

    /**
     * @return Thread that reads packets from the inputstream
     */
	public Thread getReadThread(){ return this.readThread; }

    /**
     * @return current socket
     */
	public SSLSocket getSocket(){ return this.socket; }
	
}
