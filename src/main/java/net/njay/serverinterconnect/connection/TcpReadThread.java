package net.njay.serverinterconnect.connection;

import event.Event;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.utils.packet.PacketUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class TcpReadThread extends Thread{

	protected TcpConnection conn;

	public TcpReadThread(TcpConnection conn){
		this.conn = conn;
	}

	@Override
	public void run(){
		while (!conn.isTerminated()){
			try {
				Packet p = PacketUtils.readPacket(conn.inputStream());
				Event.callEvent(new PacketRecievedEvent(conn, p));
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
