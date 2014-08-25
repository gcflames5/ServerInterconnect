package net.njay.serverinterconnect.single.client.auth;

import event.Event;
import net.njay.serverinterconnect.api.packet.AuthenticationPacket;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpSocketFactory;
import net.njay.serverinterconnect.event.connection.auth.AuthenticationFailureEvent;
import net.njay.serverinterconnect.event.connection.auth.AuthenticationSuccessEvent;
import net.njay.serverinterconnect.packet.auth.AccessRequestPacket;
import net.njay.serverinterconnect.packet.auth.AuthenticationRequestPacket;
import net.njay.serverinterconnect.packet.reject.RejectionPacket;
import net.njay.serverinterconnect.packet.reponse.Response;
import net.njay.serverinterconnect.packet.success.SuccessPacket;
import net.njay.serverinterconnect.single.client.TcpClientManager;
import net.njay.serverinterconnect.utils.response.ResponseUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthenticatedTcpClientManager extends TcpClientManager {

    protected AuthenticationPacket authenticationPacket;
    protected List<Packet> preAuthPacketBuffer;
    protected boolean authenticated;

    /**
     * Constructor.
     *
     * @param address host name to connect to
     * @param port    port to connect to
     */
    public AuthenticatedTcpClientManager(String address, int port, AuthenticationPacket authenticationPacket) {
        super(address, port);
        this.authenticationPacket = authenticationPacket;
        this.preAuthPacketBuffer = new ArrayList<Packet>();
        setAuthenticated(false);
    }

    public void setAuthenticated(boolean authenticated){
        this.authenticated = authenticated;
        if (isAuthenticated()) {
            for (Packet p : preAuthPacketBuffer)
                getConnection().sendPacket(p);
            preAuthPacketBuffer.clear();
        }
    }

    public boolean isAuthenticated(){
        return authenticated;
    }

    public void sendPacketSafely(Packet packet){
        if (!isAuthenticated())
            preAuthPacketBuffer.add(packet);
    }

    @Override
    public void initialize() throws IOException {
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
                    setAuthenticated(true);
                }else{
                    throw new RuntimeException("Unexpected Packet!");
                }
            }
        });
    }

}
