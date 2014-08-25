package net.njay.serverinterconnect.mesh.auth;

import event.Event;
import net.njay.serverinterconnect.api.auth.Authenticator;
import net.njay.serverinterconnect.api.packet.AuthenticationPacket;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpSocketFactory;
import net.njay.serverinterconnect.connection.TcpWriteThread;
import net.njay.serverinterconnect.event.connection.auth.AuthenticationFailureEvent;
import net.njay.serverinterconnect.event.connection.auth.AuthenticationSuccessEvent;
import net.njay.serverinterconnect.mesh.Mesh;
import net.njay.serverinterconnect.mesh.MeshReadThread;
import net.njay.serverinterconnect.packet.auth.AccessRequestPacket;
import net.njay.serverinterconnect.packet.auth.AuthenticationRequestPacket;
import net.njay.serverinterconnect.packet.reject.RejectionPacket;
import net.njay.serverinterconnect.packet.reponse.Response;
import net.njay.serverinterconnect.packet.success.SuccessPacket;
import net.njay.serverinterconnect.utils.response.ResponseUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthenticatedMesh extends Mesh {

    protected Authenticator authenticator;
    protected AuthenticationPacket authPacket;
    protected List<Packet> preAuthPacketBuffer;
    protected boolean authenticated;

    public AuthenticatedMesh(Authenticator authenticator, AuthenticationPacket authPacket, int listenPort, String... ipsToConnect) {
        super(listenPort, ipsToConnect);
        this.authenticator = authenticator;
        this.authPacket = authPacket;
        this.preAuthPacketBuffer = new ArrayList<Packet>();
    }

    public void sendPacketSafely(Packet packet){
        if (!isAuthenticated())
            preAuthPacketBuffer.add(packet);
    }

    public void setAuthenticated(boolean authenticated){
        this.authenticated = authenticated;
        if (isAuthenticated()) {
            for (Packet p : preAuthPacketBuffer)
                getServerManager().broadcast(p);
            preAuthPacketBuffer.clear();
        }
    }

    public boolean isAuthenticated(){
        return authenticated;
    }

    @Override
    public void initialize(){
        serverManager = new AuthenticatedMeshServerManager(this, TcpSocketFactory.generateServerSocket(port), authenticator, authPacket.getClass());
        serverManager.startConnManager();
        createConnections();
    }

    public AuthenticatedMeshServerManager getServerManager(){
        return (AuthenticatedMeshServerManager) super.serverManager;
    }

    @Override
    protected void createConnections() {
        for (String ip : ipsToConnect) {
            int port;
            if (ip.contains(":"))
                port = Integer.valueOf(ip.split(":")[1]);
            else
                port = this.port;
            try {
                TcpConnection tcpConnection = new TcpConnection(TcpSocketFactory.generateSocket(ip.split(":")[0], port, false), false);
                tcpConnection.startThreads(new TcpWriteThread(tcpConnection), new MeshReadThread(tcpConnection, this));
                serverManager.submitConnection(tcpConnection);
                sendAccessPacket(tcpConnection, authPacket);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void sendAccessPacket(TcpConnection conn, final AuthenticationPacket authenticationPacket) {
        final AccessRequestPacket requestPacket = new AccessRequestPacket(UUID.randomUUID().toString() + UUID.randomUUID().toString());
        conn.sendPacket(requestPacket);
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
