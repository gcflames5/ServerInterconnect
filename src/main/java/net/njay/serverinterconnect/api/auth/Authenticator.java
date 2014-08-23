package net.njay.serverinterconnect.api.auth;

import net.njay.serverinterconnect.api.packet.AuthenticationPacket;

public interface Authenticator {

    public boolean authenticate(AuthenticationPacket packet);

}
