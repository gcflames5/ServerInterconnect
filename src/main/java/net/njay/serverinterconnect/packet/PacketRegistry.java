package net.njay.serverinterconnect.packet;

import net.njay.serverinterconnect.api.packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class PacketRegistry {

    public static List<Class<? extends Packet>> registry = new ArrayList<Class<? extends Packet>>();

    public static void registerPacket(Class<? extends Packet> clazz){
        registry.add(clazz);
    }

    public static final int getPacketID(Packet packet){
        for (int i = 0; i < registry.size(); i++)
            if (registry.get(i) == packet.getClass())
                return i;
        throw new RuntimeException("Failed to find transferable id for: " + packet.getClass() + " (did you forget to register it?)");
    }

    public static List<Class<? extends Packet>> getRegistry(){
        return new ArrayList<Class<? extends Packet>>(registry);
    }

}
