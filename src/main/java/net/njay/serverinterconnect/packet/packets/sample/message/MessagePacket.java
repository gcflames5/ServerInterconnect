package net.njay.serverinterconnect.packet.packets.sample.message;

import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.utils.packet.PacketUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessagePacket extends Packet{

	private String message;

	public MessagePacket(){}

	public MessagePacket(String message){
		this.message = message;
	}

	public String getMessage(){ return this.message; }

	@Override
	public void readFromStream(DataInputStream input) throws IOException {
		message = PacketUtils.readString(input);
	}

	@Override
	public void writeToStream(DataOutputStream output) throws IOException {
		PacketUtils.writeString(message, output);
	}

}
