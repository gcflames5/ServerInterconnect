package net.njay.serverinterconnect.mesh;

import net.njay.serverinterconnect.api.manager.ServerManager;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpSocketFactory;
import net.njay.serverinterconnect.packet.JsonPacket;
import net.njay.serverinterconnect.packet.packets.sample.message.JsonMessagePacket;
import net.njay.serverinterconnect.packet.packets.sample.message.MessagePacket;
import net.njay.serverinterconnect.packet.reponse.AutoResponder;
import net.njay.serverinterconnect.packet.reponse.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Mesh {

    private List<UUID> recentPacketIds;
    private ServerManager serverManager;

    private String[] ipsToConnect;
    private int port;

    public Mesh(String password, int listenPort, String... ipsToConnect){
        recentPacketIds = Collections.synchronizedList(new ArrayList<UUID>());
        this.ipsToConnect = ipsToConnect;
        this.port = listenPort;
    }

    public void initialize(){
        serverManager = new MeshServerManager(TcpSocketFactory.generateServerSocket(port), this);
        createConnections();
    }

    public void addRecentPacket(Packet packet){
        recentPacketIds.add(packet.getPacketUUID());
        if (recentPacketIds.size() > 50)
            recentPacketIds.remove(50);
    }

    public boolean recieved(Packet packet){
        return recentPacketIds.contains(packet.getPacketUUID());
    }

    public void terminate(){
        serverManager.terminateConnections();
    }

    protected void createConnections(){
        for (String ip : ipsToConnect){
            int port;
            if (ip.contains(":"))
                port = Integer.valueOf(ip.split(":")[1]);
            else
                port = this.port;
            try {
                TcpConnection tcpConnection = new MeshConnection(this, TcpSocketFactory.generateSocket(ip.split(":")[0], port, false));
                serverManager.submitConnection(tcpConnection);
            } catch (IOException e) { e.printStackTrace(); continue; }
        }
    }

    public ServerManager getManager(){
        return this.serverManager;
    }

}
