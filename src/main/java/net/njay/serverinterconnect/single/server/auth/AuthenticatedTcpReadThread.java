package net.njay.serverinterconnect.single.server.auth;

import event.Event;
import net.njay.serverinterconnect.api.packet.AuthenticationPacket;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpReadThread;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.packet.auth.AccessRequestPacket;
import net.njay.serverinterconnect.packet.auth.AuthenticationRequestPacket;
import net.njay.serverinterconnect.utils.packet.PacketUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class AuthenticatedTcpReadThread extends TcpReadThread {

    private AuthenticatedTcpServerManager manager;

    public AuthenticatedTcpReadThread(AuthenticatedTcpServerManager manager, TcpConnection conn) {
        super(conn);
        this.manager = manager;
    }

    @Override
    public void run(){
        while (!conn.isTerminated()){
            try {
                Packet p = PacketUtils.readPacket(conn.inputStream());
                if (manager.isAuthenticated(conn))
                    Event.callEvent(new PacketRecievedEvent(conn, p));
                else{
                    if (p instanceof AccessRequestPacket || p instanceof AuthenticationPacket || p instanceof AuthenticationRequestPacket)
                        Event.callEvent(new PacketRecievedEvent(conn, p));
                }
            } catch (SocketTimeoutException e){
                conn.terminate();
                e.printStackTrace();
                break;
            } catch (IOException e) {
                conn.terminate();
                e.printStackTrace();
                break;
            }
        }
    }
}
