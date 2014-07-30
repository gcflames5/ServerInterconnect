package net.njay.serverinterconnect.packet.packets.sample.message;

import net.njay.serverinterconnect.packet.JsonPacket;
import net.njay.serverinterconnect.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class JsonMessagePacket extends JsonPacket {

	private String message;

	public JsonMessagePacket(){}

	public JsonMessagePacket(String message){
		this.message = message;
	}
	
	public String getMessage(){ return this.message; }

}
