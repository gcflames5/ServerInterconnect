package net.njay.serverinterconnect.packet.message;

import net.njay.serverinterconnect.packet.JsonPacket;

public class JsonMessagePacket extends JsonPacket {

	private String message;

	public JsonMessagePacket(){}

	public JsonMessagePacket(String message){
		this.message = message;
	}

	public String getMessage(){ return this.message; }

}
