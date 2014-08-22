package net.njay.serverinterconnect.connection;

import java.io.IOException;
import java.net.SocketTimeoutException;

import net.njay.serverinterconnect.packet.Packet;

public class TcpWriteThread extends Thread{

	private TcpConnection conn;
	
	public TcpWriteThread(TcpConnection conn){
		this.conn = conn;
	}
	
	@Override
	public void run(){
		while (!conn.isTerminated()){
			Packet p;
			try {
				p = conn.getQueue().take();
				if (p == null)
					continue;
				Packet.writePacket(p, conn.outputStream());
			} catch (SocketTimeoutException e){
                conn.terminate();
                break;
            } catch (Exception e) {
				conn.terminate();
                break;
			}
		}
	}
}
