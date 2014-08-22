package net.njay.serverinterconnect.mesh.thread;

import event.Event;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.connection.TcpReadThread;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.mesh.Mesh;
import net.njay.serverinterconnect.packet.Packet;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class MeshReadThread extends TcpReadThread {

    private Mesh mesh;

    public MeshReadThread(TcpConnection conn, Mesh mesh) {
        super(conn);
        this.mesh = mesh;
    }

    @Override
    public void run(){
        while (!conn.isTerminated()){
            try {
                Packet p = Packet.readPacket(conn.inputStream());
                if (!mesh.recieved(p)) {
                    mesh.addRecentPacket(p);
                    Event.callEvent(new PacketRecievedEvent(conn, p));
                }
            } catch (SocketTimeoutException e){
                conn.terminate();
                break;
            } catch (IOException e) {
                conn.terminate();
                break;
            }
        }
    }
}
