package net.njay.serverinterconnect.mesh;

import net.njay.serverinterconnect.api.manager.ServerManager;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpSocketFactory;
import net.njay.serverinterconnect.connection.TcpWriteThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Mesh {

    protected List<UUID> recentPacketIds;
    protected ServerManager serverManager;

    protected String[] ipsToConnect;
    protected int port;

    public Mesh(int listenPort, String... ipsToConnect) {
        recentPacketIds = Collections.synchronizedList(new ArrayList<UUID>());
        this.ipsToConnect = ipsToConnect;
        this.port = listenPort;
    }

    public void initialize() {
        serverManager = new MeshServerManager(TcpSocketFactory.generateServerSocket(port), this);
        createConnections();
    }

    public void addRecentPacket(Packet packet) {
        recentPacketIds.add(packet.getPacketUUID());
        if (recentPacketIds.size() > 50)
            recentPacketIds.remove(50);
    }

    public boolean recieved(Packet packet) {
        return recentPacketIds.contains(packet.getPacketUUID());
    }

    public void terminate() {
        serverManager.terminateConnections();
    }

    protected void createConnections() {
        for (String ip : ipsToConnect) {
            int port;
            if (ip.contains(":"))
                port = Integer.valueOf(ip.split(":")[1]);
            else
                port = this.port;
            try {
                TcpConnection tcpConnection = new TcpConnection(TcpSocketFactory.generateSocket(ip.split(":")[0], port, false), false);
                tcpConnection.startThreads(new TcpWriteThread(tcpConnection), new MeshReadThread(tcpConnection, this));
                serverManager.submitConnection(tcpConnection);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public ServerManager getManager() {
        return this.serverManager;
    }

}
