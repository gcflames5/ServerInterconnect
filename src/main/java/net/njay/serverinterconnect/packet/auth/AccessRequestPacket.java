package net.njay.serverinterconnect.packet.auth;

import net.njay.serverinterconnect.packet.JsonPacket;

public class AccessRequestPacket extends JsonPacket {

    private String secret;

    public AccessRequestPacket() {}

    public AccessRequestPacket(String secret){
        this.secret = secret;
    }

    public String getSecret(){ return this.secret; }

}
