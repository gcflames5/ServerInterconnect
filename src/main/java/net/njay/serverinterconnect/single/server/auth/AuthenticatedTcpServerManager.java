package net.njay.serverinterconnect.single.server.auth;

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
import net.njay.serverinterconnect.single.server.TcpServerManager;
import net.njay.serverinterconnect.utils.response.ResponseUtil;

import javax.net.ssl.SSLServerSocket;
import java.util.ArrayList;
import java.util.List;

public class AuthenticatedTcpServerManager extends TcpServerManager {

    private Authenticator authenticator;
    private List<TcpConnection> unAuthenticatedConnections;

    public AuthenticatedTcpServerManager(SSLServerSocket serversocket, Authenticator authenticator, Class<? extends AuthenticationPacket> authPacketClass, boolean start) {
        super(serversocket, start);
        this.authenticator = authenticator;
        this.unAuthenticatedConnections = new ArrayList<TcpConnection>();

        registerPacketResponses(authPacketClass);
    }

    public Authenticator getAuthenticator() {
        return this.authenticator;
    }

    public void submitUnauthenticatedConnection(TcpConnection connection) {
        this.unAuthenticatedConnections.add(connection);
    }

    private void registerPacketResponses(Class<? extends AuthenticationPacket> authPacketClass) {
        //Waits for the client to send a AccessRequestPacket, and when it does, send an
        //AuthenticationRequestPacket with the same secret as the AccessRequestPacket
        ResponseUtil.getDefaultResponder().respondTo(AccessRequestPacket.class, new Response() {
            @Override
            public void run() {
                if (!unAuthenticatedConnections.contains(event.getConnection())) return;
                AccessRequestPacket accessRequestPacket = (AccessRequestPacket) packet;
                event.getConnection().sendPacket(new AuthenticationRequestPacket(accessRequestPacket.getSecret()).setResponse(packet));
            }
        });

        //Waits for the client to return with an AuthenticationPacket and when it does,
        //check the validity of its credentials and admit it to the server
        ResponseUtil.getDefaultResponder().respondTo(authPacketClass, new Response() {
            @Override
            public void run() {
                if (!unAuthenticatedConnections.contains(event.getConnection())) return;
                AuthenticationPacket authenticationPacket = (AuthenticationPacket) packet;
                if (authenticator.authenticate(authenticationPacket)) {
                    submitConnection(event.getConnection());
                    unAuthenticatedConnections.remove(event.getConnection());
                    event.getConnection().sendPacket(new SuccessPacket().setResponse(packet));
                    Event.callEvent(new AuthenticationSuccessEvent(event.getConnection()));
                }else{
                    event.getConnection().sendPacket(new RejectionPacket(RejectionReason.INVALID_CREDENTIALS).setResponse(packet));
                    Event.callEvent(new AuthenticationFailureEvent(event.getConnection()));
                }
            }
        });
    }

    @Override
    public void startConnManager() {
        connManager = new AuthenticatedIncomingConnectionThread(this);
        connManager.start();
    }

    public boolean isAuthenticated(TcpConnection conn) {
        return activeConnections.contains(conn);
    }

}
