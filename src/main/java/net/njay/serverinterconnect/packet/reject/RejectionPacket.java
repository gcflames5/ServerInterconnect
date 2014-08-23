package net.njay.serverinterconnect.packet.reject;

import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.packet.JsonPacket;

import java.util.UUID;

public class RejectionPacket extends JsonPacket{

    private UUID rejectedPacket;
    private RejectionReason rejectionReason;

    public RejectionPacket() {}

    public RejectionPacket(Packet rejectedPacket, RejectionReason rejectionReason){
        this.rejectedPacket = rejectedPacket.getPacketUUID();
        this.rejectionReason = rejectionReason;
    }

    public UUID getRejectedPacket(){ return this.rejectedPacket; }
    public RejectionReason getReason(){ return this.rejectionReason; }

}
