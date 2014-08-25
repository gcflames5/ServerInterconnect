package net.njay.serverinterconnect.mesh;

import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpWriteThread;
import net.njay.serverinterconnect.single.server.IncomingConnectionThread;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.Socket;

public class MeshEntryPacketListener extends IncomingConnectionThread {

    private Mesh mesh;

    public MeshEntryPacketListener(MeshServerManager meshServerManager, Mesh mesh) {
        super(meshServerManager);
        this.mesh = mesh;
    }

    @Override
    protected void handleConnection(Socket incomingConn) throws IOException {
        TcpConnection tcpConnection = new TcpConnection((SSLSocket) incomingConn, false);
        tcpConnection.startThreads(new TcpWriteThread(tcpConnection), new MeshReadThread(tcpConnection, mesh));
        manager.submitConnection(tcpConnection);

    }

    public void terminate() {
        manager.terminateConnections();
    }

}
