package net.njay.serverinterconnect.event.connection;

import event.Event;
import net.njay.serverinterconnect.connection.TcpConnection;

/**
 * Created by Nick on 7/4/14.
 */
public class ConnectionTerminateEvent extends Event {

    private TcpConnection conn;

    public ConnectionTerminateEvent(TcpConnection conn){
        this.conn = conn;
    }

    public TcpConnection getConnection(){
        return this.conn;
    }

}
