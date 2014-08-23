package net.njay.serverinterconnect.packet.reponse;

import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.event.PacketRecievedEvent;

public abstract class Response implements Runnable{

    protected PacketRecievedEvent event;
    protected Packet packet;

}
