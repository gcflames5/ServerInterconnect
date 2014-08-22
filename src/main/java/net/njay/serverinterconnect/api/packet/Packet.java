package net.njay.serverinterconnect.api.packet;

import com.sun.istack.internal.Nullable;
import net.njay.serverinterconnect.api.transferable.Transferable;

import java.util.UUID;

public abstract class Packet implements Transferable {

    public static final int MAX_STRING_SIZE = 32767;

    private UUID packetUuid;
    @Nullable
    private UUID responseTo;

    public Packet(){
        this.responseTo = null;
        this.packetUuid = UUID.randomUUID();
    }

    public UUID getPacketUUID(){ return packetUuid; }

    public Packet setResponse(UUID responseToUUID){
        this.responseTo = responseToUUID;
        return this;
    }

    public Packet setResponseTo(Packet responseTo){
        this.responseTo = responseTo.getPacketUUID();
        return this;
    }

    @Nullable
    public UUID getResponseTo(){
        return responseTo;
    }

    public boolean isResponse(){
        return responseTo != null;
    }

}
