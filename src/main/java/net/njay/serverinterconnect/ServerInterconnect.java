package net.njay.serverinterconnect;

import net.njay.serverinterconnect.packet.PacketRegistry;
import net.njay.serverinterconnect.packet.auth.AccessRequestPacket;
import net.njay.serverinterconnect.packet.auth.AuthenticationRequestPacket;
import net.njay.serverinterconnect.packet.message.JsonMessagePacket;
import net.njay.serverinterconnect.packet.message.MessagePacket;
import net.njay.serverinterconnect.packet.reject.RejectionPacket;
import net.njay.serverinterconnect.packet.success.SuccessPacket;

public class ServerInterconnect {

    public static void registerCorePackets() {
        PacketRegistry.registerPacket(AccessRequestPacket.class);
        PacketRegistry.registerPacket(AuthenticationRequestPacket.class);
        PacketRegistry.registerPacket(RejectionPacket.class);
        PacketRegistry.registerPacket(SuccessPacket.class);

        PacketRegistry.registerPacket(MessagePacket.class);
        PacketRegistry.registerPacket(JsonMessagePacket.class);
    }

}
