package net.njay.serverinterconnect.mesh;

import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.mesh.incoming.MeshEntryPacketListener;
import net.njay.serverinterconnect.single.server.TcpServerManager;

import javax.net.ssl.SSLServerSocket;
import java.io.IOException;

public class MeshServerManager extends TcpServerManager {

    private Mesh mesh;

    /**
     * Constructor.
     *
     * @param serversocket socket to manage
     */
    public MeshServerManager(SSLServerSocket serversocket, Mesh mesh) {
        super(serversocket, false);
        this.mesh = mesh;
        startConnManager();
    }

    @Override
    public void startConnManager(){
        this.connManager = new MeshEntryPacketListener(this, mesh);
        this.connManager.start();
    }

    @Override
    public void terminateConnections() {
        for (TcpConnection connection : activeConnections)
            connection.terminate();
        activeConnections.clear();
        try{
            serversocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
