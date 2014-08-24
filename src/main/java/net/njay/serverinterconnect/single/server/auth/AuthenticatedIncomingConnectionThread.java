package net.njay.serverinterconnect.single.server.auth;

import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpWriteThread;
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
    protected void handleConnection(Socket incomingConn) throws IOException {
        TcpConnection tcpConn = new TcpConnection((SSLSocket) incomingConn, false);
        tcpConn.startThreads(new TcpWriteThread(tcpConn), new AuthenticatedTcpReadThread((AuthenticatedTcpServerManager) manager, tcpConn));
        ((AuthenticatedTcpServerManager) manager).submitUnauthenticatedConnection(tcpConn);
    }

}
