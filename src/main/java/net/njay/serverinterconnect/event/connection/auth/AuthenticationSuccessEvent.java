package net.njay.serverinterconnect.event.connection.auth;

import event.Event;
import net.njay.serverinterconnect.api.connection.Connection;

public class AuthenticationSuccessEvent extends Event {

    private Connection conn;

    public AuthenticationSuccessEvent(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() {
        return this.conn;
    }

}
