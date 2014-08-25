package net.njay.serverinterconnect.mesh;

import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpWriteThread;

import javax.net.ssl.SSLSocket;
import java.io.IOException;

public class MeshConnection extends TcpConnection {

    public MeshConnection(Mesh mesh, SSLSocket socket) throws IOException {
        super(socket, false);
        super.writeThread = new TcpWriteThread(this);
        super.readThread = new MeshReadThread(this, mesh);
        startThreads();
    }
}
