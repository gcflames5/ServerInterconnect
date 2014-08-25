package net.njay.serverinterconnect.mesh.auth;

import event.Event;
import net.njay.serverinterconnect.api.auth.Authenticator;
import net.njay.serverinterconnect.api.packet.AuthenticationPacket;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.event.connection.auth.AuthenticationFailureEvent;
import net.njay.serverinterconnect.event.connection.auth.AuthenticationSuccessEvent;
import net.njay.serverinterconnect.packet.auth.AccessRequestPacket;
import net.njay.serverinterconnect.packet.auth.AuthenticationRequestPacket;
import net.njay.serverinterconnect.packet.reject.RejectionPacket;
import net.njay.serverinterconnect.packet.reject.RejectionReason;
import net.njay.serverinterconnect.packet.reponse.Response;
import net.njay.serverinterconnect.packet.success.SuccessPacket;
import net.njay.serverinterconnect.single.server.auth.AuthenticatedIncomingConnectionThread;
import net.njay.serverinterconnect.single.server.auth.AuthenticatedTcpServerManager;
import net.njay.serverinterconnect.utils.response.ResponseUtil;

import javax.net.ssl.SSLServerSocket;

public class AuthenticatedMeshServerManager extends AuthenticatedTcpServerManager {

    private AuthenticatedMesh authenticatedMesh;

    public AuthenticatedMeshServerManager(AuthenticatedMesh authenticatedMesh, SSLServerSocket serversocket, Authenticator authenticator, Class<? extends AuthenticationPacket> authPacketClass) {
        super(serversocket, authenticator, authPacketClass, false);
        this.authenticatedMesh = authenticatedMesh;
    }

    @Override
    public void startConnManager() {
        connManager = new AuthenticatedMeshEntryPacketListener(authenticatedMesh);
        connManager.start();
    }

    public boolean isAuthenticated(TcpConnection conn) {
        return activeConnections.contains(conn);
    }

}

