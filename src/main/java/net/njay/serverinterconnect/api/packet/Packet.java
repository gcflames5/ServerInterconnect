package net.njay.serverinterconnect.api.packet;

import net.njay.serverinterconnect.api.transferable.Transferable;

import java.util.UUID;

public abstract class Packet implements Transferable {

    public static final int MAX_STRING_SIZE = 32767;

    private UUID packetUuid;

    public Packet(){
        packetUuid = UUID.randomUUID();
    }

    public UUID getPacketUUID(){ return packetUuid; }

}
