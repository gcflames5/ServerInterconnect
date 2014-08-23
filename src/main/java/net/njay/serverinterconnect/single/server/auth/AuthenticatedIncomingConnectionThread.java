package net.njay.serverinterconnect.single.server.auth;

import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.single.server.IncomingConnectionThread;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class AuthenticatedIncomingConnectionThread extends IncomingConnectionThread {

    /**
     * Constructor.
     *
     * @param manager server manager that new connections will be passed on to
     */
    public AuthenticatedIncomingConnectionThread(AuthenticatedTcpServerManager manager) {
        super(manager);
    }

    @Override
    public boolean waitForConnection(){
        try {
            Socket incomingConn = serversocket.accept();
            if (!(incomingConn instanceof SSLSocket))
                throw new RuntimeException("Non-SSL Connection detected! Rejecting " + incomingConn.getInetAddress());
            TcpConnection tcpConn = new TcpConnection((SSLSocket)incomingConn);
            ((AuthenticatedTcpServerManager)manager).submitUnauthenticatedConnection(tcpConn);
        } catch(SocketException e){
            System.err.println("Socket closed... terminating listening thread.");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
