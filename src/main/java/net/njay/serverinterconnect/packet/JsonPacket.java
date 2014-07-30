package net.njay.serverinterconnect.packet;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class JsonPacket extends Packet {

    @Override
    public void readPacketContent(DataInputStream input) throws IOException {}

    @Override
    public void writePacketContent(DataOutputStream output) throws IOException {
        Packet.writeString(new Gson().toJson(this), output);
    }
}
