package net.njay.serverinterconnect.connection;

import java.io.IOException;
import java.net.InetAddress;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class TcpSocketFactory {

    /**
     * Generate a SSLServerSocket from a port (for servers)
     *
     * @param listenport port to listen on
     * @return active socket
     */
	public static SSLServerSocket generateServerSocket(int listenport){
		SSLServerSocket serverSocket = null;
		try {
        	SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(listenport);
            final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
            serverSocket.setEnabledCipherSuites(enabledCipherSuites);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + listenport);
        }
		return serverSocket;
	}

    /**
     * Generate a SSLSocket that connects to an address (for clients)
     *
     * @param serverAddress hostname to connect to
     * @param port port that the hostname is listening on
     * @param handshake whether to ssl handshake immediately (if false this will be performed upon first attempt at sending a packet)
     * @return connected socket
     * @throws IOException failed to connect to host
     */
	public static SSLSocket generateSocket(String serverAddress, int port, boolean handshake) throws IOException{
		SSLSocket socket = null;
		SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		socket = (SSLSocket) sslSocketFactory.createSocket(InetAddress.getByName(serverAddress), port);
		final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
		socket.setEnabledCipherSuites(enabledCipherSuites);
		if (handshake) socket.startHandshake();
		return socket;
	}

}
