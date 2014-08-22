package net.njay.serverinterconnect.event;

import event.Event;
import net.njay.serverinterconnect.connection.TcpConnection;
import net.njay.serverinterconnect.packet.Packet;

public class PacketRecievedEvent extends Event {

    private TcpConnection conn;
	private Packet packet;

    /**
     * Called when a transferable is recieved (both server and client)
     *
     * @param conn connection that the transferable was received from
     * @param packet transferable that was received
     */
	public PacketRecievedEvent(TcpConnection conn, Packet packet){
        this.conn = conn;
		this.packet = packet;
	}

    /** @return connection that the transferable was received from */
    public TcpConnection getConnection(){ return this.conn; }

    /** @return transferable that was recieved */
	public Packet getPacket(){ return this.packet; }

}
