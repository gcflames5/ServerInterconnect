package net.njay.serverinterconnect.packet.reponse;

import event.Event;
import event.EventHandler;
import event.Listener;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.event.PacketRecievedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AutoResponder implements Listener {

    private Map<Class<? extends Packet>, Response> pendingResponses = new HashMap<Class<? extends Packet>, Response>();
    private Map<UUID, Response> pendingPacketResponses = new HashMap<UUID, Response>();

    public AutoResponder() {
        Event.addListener(this);
    }

    public void respondTo(Class<? extends Packet> toRespond, Response response) {
        pendingResponses.put(toRespond, response);
    }

    public void onResponse(Packet packet, Response response) {
        pendingPacketResponses.put(packet.getPacketUUID(), response);
    }

    public Map<Class<? extends Packet>, Response> getPendingResponses() {
        return pendingResponses;
    }

    public Map<UUID, Response> getPendingPacketResponses() {
        return pendingPacketResponses;
    }


    @EventHandler
    public void onRecieve(PacketRecievedEvent e) {
        if (!pendingResponses.containsKey(e.getPacket().getClass())) return;
        Response response = pendingResponses.get(e.getPacket().getClass());
        response.event = e;
        response.packet = e.getPacket();
        response.run();
    }

    @EventHandler
    public void onPacketRecieve(PacketRecievedEvent e) {
        if (!e.getPacket().isResponse()) return;
        if (!pendingPacketResponses.containsKey(e.getPacket().getResponseTo())) return;
        Response response = pendingPacketResponses.get(e.getPacket().getResponseTo());
        response.event = e;
        response.packet = e.getPacket();
        response.run();
        pendingPacketResponses.remove(e.getPacket().getPacketUUID());
    }

}
