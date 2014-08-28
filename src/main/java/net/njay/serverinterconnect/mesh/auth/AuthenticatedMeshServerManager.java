package net.njay.serverinterconnect.mesh.auth;

import net.njay.serverinterconnect.api.auth.Authenticator;
import net.njay.serverinterconnect.api.packet.AuthenticationPacket;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.single.server.auth.AuthenticatedTcpServerManager;

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

