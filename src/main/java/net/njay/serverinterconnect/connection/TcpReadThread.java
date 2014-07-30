package net.njay.serverinterconnect.connection;

import java.io.IOException;

import event.Event;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.packet.Packet;

public class TcpReadThread extends Thread{

	private TcpConnection conn;
	
	public TcpReadThread(TcpConnection conn){
		this.conn = conn;
	}
	
	@Override
	public void run(){
		while (!conn.isTerminated()){
			try {
				Packet p = Packet.readPacket(conn.inputStream());
				Event.callEvent(new PacketRecievedEvent(conn, p));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
