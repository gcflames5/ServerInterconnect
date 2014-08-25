package net.njay.serverinterconnect.connection;

import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.utils.packet.PacketUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class TcpWriteThread extends Thread {

    protected TcpConnection conn;

    public TcpWriteThread(TcpConnection conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        while (!conn.isTerminated()) {
            try {
                sendPacket();
            } catch (SocketTimeoutException e) {
                conn.terminate();
                e.printStackTrace();
                break;
            } catch (Exception e) {
                conn.terminate();
                e.printStackTrace();
                break;
            }
        }
    }

    protected void sendPacket() throws InterruptedException, IOException {
        Packet p = conn.getQueue().take();
        if (p == null) return;
        PacketUtils.writePacket(p, conn.outputStream());
    }
}
