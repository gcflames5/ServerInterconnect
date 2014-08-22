package net.njay.serverinterconnect.event;

import event.Event;
import net.njay.serverinterconnect.api.connection.Connection;
import net.njay.serverinterconnect.api.packet.Packet;

public class PacketRecievedEvent extends Event {

    private Connection conn;
	private Packet packet;

    /**
     * Called when a transferable is recieved (both server and client)
     *
     * @param conn connection that the transferable was received from
     * @param packet transferable that was received
     */
	public PacketRecievedEvent(Connection conn, Packet packet){
        this.conn = conn;
		this.packet = packet;
	}

    /** @return connection that the transferable was received from */
    public Connection getConnection(){ return this.conn; }

    /** @return transferable that was recieved */
	public Packet getPacket(){ return this.packet; }

}
