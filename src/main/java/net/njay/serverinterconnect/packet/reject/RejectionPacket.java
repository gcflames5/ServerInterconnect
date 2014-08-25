package net.njay.serverinterconnect.packet.reject;

import net.njay.serverinterconnect.packet.JsonPacket;

public class RejectionPacket extends JsonPacket {

    private RejectionReason rejectionReason;

    public RejectionPacket() { }

    public RejectionPacket(RejectionReason rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public RejectionReason getReason() {
        return this.rejectionReason;
    }

}
