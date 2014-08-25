package net.njay.serverinterconnect.mesh.auth;

import net.njay.serverinterconnect.api.auth.Authenticator;
import net.njay.serverinterconnect.api.packet.AuthenticationPacket;
import net.njay.serverinterconnect.single.server.auth.AuthenticatedTcpServerManager;

import javax.net.ssl.SSLServerSocket;

public class AuthenticatedMeshServerManager extends AuthenticatedTcpServerManager {

    public AuthenticatedMeshServerManager(SSLServerSocket serversocket, Authenticator authenticator, Class<? extends AuthenticationPacket> authPacketClass, boolean start) {
        super(serversocket, authenticator, authPacketClass, start);
    }


}
