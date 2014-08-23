package net.njay.serverinterconnect.event.connection;

import event.Event;
import net.njay.serverinterconnect.api.connection.Connection;


public class ConnectionTerminateEvent extends Event {

    private Connection conn;

    public ConnectionTerminateEvent(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() {
        return this.conn;
    }

}
