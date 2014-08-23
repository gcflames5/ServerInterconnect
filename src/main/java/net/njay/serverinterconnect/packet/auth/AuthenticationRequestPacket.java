package net.njay.serverinterconnect.packet.auth;

import net.njay.serverinterconnect.packet.JsonPacket;

public class AuthenticationRequestPacket extends JsonPacket {

    private String secret;

    public AuthenticationRequestPacket() {}

    public AuthenticationRequestPacket(String secret){
        this.secret = secret;
    }

    public String getSecret(){ return this.secret; }

}
