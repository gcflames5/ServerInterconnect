package net.njay.serverinterconnect.event;

import event.Event;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.packet.Packet;

public class PacketRecievedEvent extends Event {

    private TcpConnection conn;
	private Packet packet;

    /**
     * Called when a packet is recieved (both server and client)
     *
     * @param conn connection that the packet was received from
     * @param packet packet that was received
     */
	public PacketRecievedEvent(TcpConnection conn, Packet packet){
        this.conn = conn;
		this.packet = packet;
	}

    /** @return connection that the packet was received from */
    public TcpConnection getConnection(){ return this.conn; }

    /** @return packet that was recieved */
	public Packet getPacket(){ return this.packet; }

}
