package net.njay.serverinterconnect.mesh.auth;

import event.Event;
import net.njay.serverinterconnect.api.packet.AuthenticationPacket;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpReadThread;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.packet.auth.AccessRequestPacket;
import net.njay.serverinterconnect.packet.reject.RejectionPacket;
import net.njay.serverinterconnect.packet.reject.RejectionReason;

public class AuthenticatedMeshReadThread extends TcpReadThread {

    private AuthenticatedMesh authenticatedMesh;

    public AuthenticatedMeshReadThread(TcpConnection conn, AuthenticatedMesh authenticatedMesh) {
        super(conn);
        this.authenticatedMesh = authenticatedMesh;
    }

    @Override
    public void handlePacket(Packet packet){
        if (authenticatedMesh.getServerManager().isAuthenticated(conn))
            Event.callEvent(new PacketRecievedEvent(conn, packet));
        else {
            if (packet instanceof AccessRequestPacket || packet instanceof AuthenticationPacket)
                Event.callEvent(new PacketRecievedEvent(conn, packet));
            else
                conn.sendPacket(new RejectionPacket(RejectionReason.CONNECTION_NOT_AUTHORIZED).setResponse(packet));
        }
    }

}
