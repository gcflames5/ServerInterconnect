package net.njay.serverinterconnect.mesh;

import event.Event;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpReadThread;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.mesh.Mesh;
import net.njay.serverinterconnect.utils.packet.PacketUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class MeshReadThread extends TcpReadThread {

    private Mesh mesh;

    public MeshReadThread(TcpConnection conn, Mesh mesh) {
        super(conn);
        this.mesh = mesh;
    }

    @Override
    public void handlePacket(Packet p){
        if (!mesh.recieved(p)) {
            mesh.addRecentPacket(p);
            Event.callEvent(new PacketRecievedEvent(conn, p));
        }
    }
}