package net.njay.serverinterconnect.packet;

import com.google.gson.Gson;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.utils.packet.PacketUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class JsonPacket extends Packet {

    @Override
    public void readFromStream(DataInputStream input) throws IOException {
    }

    @Override
    public void writeToStream(DataOutputStream output) throws IOException {
        PacketUtils.writeString(new Gson().toJson(this), output);
    }
}
