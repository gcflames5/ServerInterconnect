package net.njay.serverinterconnect.mesh.auth;

import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpWriteThread;
import net.njay.serverinterconnect.single.server.IncomingConnectionThread;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.Socket;

public class AuthenticatedMeshEntryPacketListener extends IncomingConnectionThread {

    private AuthenticatedMesh authenticatedMesh;

    public AuthenticatedMeshEntryPacketListener(AuthenticatedMesh authenticatedMesh) {
        super(authenticatedMesh.getServerManager());
        this.authenticatedMesh = authenticatedMesh;
    }

    @Override
    protected void handleConnection(Socket incomingConn) throws IOException {
        TcpConnection tcpConn = new TcpConnection((SSLSocket) incomingConn, false);
        tcpConn.startThreads(new TcpWriteThread(tcpConn), new AuthenticatedMeshReadThread(tcpConn, authenticatedMesh));
        ((AuthenticatedMeshServerManager) manager).submitUnauthenticatedConnection(tcpConn);
    }
}
