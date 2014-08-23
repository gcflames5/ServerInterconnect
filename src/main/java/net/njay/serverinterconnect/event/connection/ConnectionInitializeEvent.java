package net.njay.serverinterconnect.event.connection;

import event.Event;
import net.njay.serverinterconnect.api.connection.Connection;

public class ConnectionInitializeEvent extends Event {

    private Connection conn;

    public ConnectionInitializeEvent(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() {
        return this.conn;
    }
}
