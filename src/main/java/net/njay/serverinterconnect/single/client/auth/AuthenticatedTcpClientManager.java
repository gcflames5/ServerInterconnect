package net.njay.serverinterconnect.single.client.auth;

import event.Event;
import net.njay.serverinterconnect.api.packet.AuthenticationPacket;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpSocketFactory;
import net.njay.serverinterconnect.event.connection.auth.AuthenticationFailureEvent;
import net.njay.serverinterconnect.event.connection.auth.AuthenticationSuccessEvent;
import net.njay.serverinterconnect.packet.auth.AccessRequestPacket;
import net.njay.serverinterconnect.packet.auth.AuthenticationRequestPacket;
import net.njay.serverinterconnect.packet.reject.RejectionPacket;
import net.njay.serverinterconnect.packet.reject.RejectionReason;
import net.njay.serverinterconnect.packet.reponse.Response;
import net.njay.serverinterconnect.packet.success.SuccessPacket;
import net.njay.serverinterconnect.single.client.TcpClientManager;
import net.njay.serverinterconnect.utils.response.ResponseUtil;

import java.io.IOException;
import java.util.UUID;

public class AuthenticatedTcpClientManager extends TcpClientManager {

    /**
     * Constructor.
     *
     * @param address host name to connect to
     * @param port    port to connect to
     */
    public AuthenticatedTcpClientManager(String address, int port) {
        super(address, port);
    }

    public void initialize(AuthenticationPacket authenticationPacket) throws IOException {
        socket = TcpSocketFactory.generateSocket(address, port, false);
        activeConnection = new TcpConnection(socket);
        sendAccessPacket(authenticationPacket);
    }

    private void sendAccessPacket(final AuthenticationPacket authenticationPacket) {
        final AccessRequestPacket requestPacket = new AccessRequestPacket(UUID.randomUUID().toString() + UUID.randomUUID().toString());
        getConnection().sendPacket(requestPacket);
        ResponseUtil.getDefaultResponder().onResponse(requestPacket, new Response() {
            public void run() {
                if (packet instanceof AuthenticationRequestPacket){
                    if (((AuthenticationRequestPacket)packet).getSecret().equals(requestPacket.getSecret()))
                        event.getConnection().sendPacket(authenticationPacket);
                    else
                        throw new RuntimeException("Sent key: " + requestPacket.getSecret() + ", server returned with " + ((AuthenticationRequestPacket)packet).getSecret() + ". Possible malicious sender!");
                }else{
                    throw new RuntimeException("Unexpected Packet!");
                }
            }
        });
        ResponseUtil.getDefaultResponder().onResponse(authenticationPacket, new Response() {
            public void run() {
                if (packet instanceof RejectionPacket){
                    Event.callEvent(new AuthenticationFailureEvent(event.getConnection()));
                }else if (packet instanceof SuccessPacket){
                    Event.callEvent(new AuthenticationSuccessEvent(event.getConnection()));
                }else{
                    throw new RuntimeException("Unexpected Packet!");
                }
            }
        });
    }

}
