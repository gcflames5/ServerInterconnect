package net.njay.serverinterconnect.connection;

import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.utils.packet.PacketUtils;

import java.net.SocketTimeoutException;

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
				PacketUtils.writePacket(p, conn.outputStream());
			} catch (SocketTimeoutException e){
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
}
