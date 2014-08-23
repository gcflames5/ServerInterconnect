package net.njay.serverinterconnect.packet.reponse;

import event.Event;
import event.EventHandler;
import event.Listener;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.event.PacketRecievedEvent;

import java.util.HashMap;
import java.util.Map;

public class AutoResponder implements Listener {

    private Map<Class<? extends Packet>, Response> waitingResponses = new HashMap<Class<? extends Packet>, Response>();

    public AutoResponder(){
        Event.addListener(this);
    }

    public void respondTo(Class<? extends Packet> toRespond, Response response){
        waitingResponses.put(toRespond, response);
    }

    public Map<Class<? extends Packet>, Response> getWaitingResponses(){
        return waitingResponses;
    }

    @EventHandler
    public void onRecieve(PacketRecievedEvent e){
        if (!waitingResponses.containsKey(e.getPacket().getClass())) return;
        Response response = waitingResponses.get(e.getPacket().getClass());
        response.event = e;
        response.packet = e.getPacket();
        response.run();
    }

}
