package net.njay.serverinterconnect.single.client.auth;

import net.njay.serverinterconnect.api.packet.AuthenticationPacket;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpSocketFactory;
import net.njay.serverinterconnect.packet.auth.AccessRequestPacket;
import net.njay.serverinterconnect.packet.reponse.Response;
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

    private void sendAccessPacket(final AuthenticationPacket authenticationPacket){
        AccessRequestPacket packet = new AccessRequestPacket(UUID.randomUUID().toString() + UUID.randomUUID().toString());
        getConnection().sendPacket(packet);
        System.out.println("Sent auth packet!");
        ResponseUtil.getDefaultResponder().onResponse(packet, new Response() {
            @Override
            public void run() {
                event.getConnection().sendPacket(authenticationPacket);
                System.out.println("Sent auth pacet2!");
            }
        });
    }
}
