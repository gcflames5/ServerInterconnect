package net.njay.serverinterconnect.packet.packets.sample.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.njay.serverinterconnect.packet.Packet;

public class MessagePacket extends Packet{

	private String message;
	
	public MessagePacket(){}
	
	public MessagePacket(String message){
		this.message = message;
	}
	
	public String getMessage(){ return this.message; }
	
	@Override
	public void readPacketContent(DataInputStream input) throws IOException {
		message = Packet.readString(input);
	}

	@Override
	public void writePacketContent(DataOutputStream output) throws IOException {
		Packet.writeString(message, output);		
	}

}
