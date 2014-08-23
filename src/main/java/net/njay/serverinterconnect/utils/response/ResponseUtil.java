package net.njay.serverinterconnect.utils.response;

import event.Event;
import net.njay.serverinterconnect.packet.reponse.AutoResponder;

public class ResponseUtil {

    private static AutoResponder defaultResponder = new AutoResponder();

    public static AutoResponder getDefaultResponder() {
        return defaultResponder;
    }

    public static void enableDefaultResponder() {
        if (!Event.getListeners().contains(defaultResponder))
            Event.addListener(defaultResponder);
    }

    public static void disableDefaultResponder() {
        if (Event.getListeners().contains(defaultResponder))
            Event.removeListener(defaultResponder);
    }
}
