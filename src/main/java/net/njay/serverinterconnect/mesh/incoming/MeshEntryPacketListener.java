package net.njay.serverinterconnect.mesh.incoming;

import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.mesh.Mesh;
import net.njay.serverinterconnect.mesh.MeshConnection;
import net.njay.serverinterconnect.mesh.MeshServerManager;
import net.njay.serverinterconnect.single.server.IncomingConnectionThread;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class MeshEntryPacketListener extends IncomingConnectionThread {

    private Mesh mesh;

    public MeshEntryPacketListener(MeshServerManager meshServerManager, Mesh mesh) {
        super(meshServerManager);
        this.mesh = mesh;
    }

    @Override
    public boolean waitForConnection(){
        try {
            Socket incomingConn = serversocket.accept();
            if (!(incomingConn instanceof SSLSocket))
                throw new RuntimeException("Non-SSL Connection detected! Rejecting " + incomingConn.getInetAddress());
            TcpConnection tcpConn = new MeshConnection(mesh, (SSLSocket)incomingConn);
            manager.submitConnection(tcpConn);
        } catch(SocketException e){
            System.err.println("Socket closed... terminating listening thread.");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void terminate(){
        manager.terminateConnections();
    }

}
