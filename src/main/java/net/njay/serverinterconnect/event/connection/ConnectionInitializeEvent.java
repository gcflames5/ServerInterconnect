package net.njay.serverinterconnect.event.connection;

import net.njay.customevents.event.Event;
import net.njay.serverinterconnect.connection.TcpConnection;

/**
 * Created by Nick on 7/4/14.
 */
public class ConnectionInitializeEvent extends Event {

    private TcpConnection conn;

    public ConnectionInitializeEvent(TcpConnection conn){
        this.conn = conn;
    }

    public TcpConnection getConnection(){
        return this.conn;
    }
}
