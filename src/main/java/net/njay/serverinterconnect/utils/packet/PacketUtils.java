package net.njay.serverinterconnect.utils.packet;

import com.google.gson.Gson;
import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.api.transferable.Transferable;
import net.njay.serverinterconnect.packet.JsonPacket;
import net.njay.serverinterconnect.packet.PacketRegistry;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketUtils {

    public static Packet readPacket(DataInputStream input) throws IOException {
        int packetID = input.readInt();
        if (packetID >= PacketRegistry.getRegistry().size())
            throw new RuntimeException("Invalid Packet id " + packetID);
        Packet packet = getNewPacket(packetID);
        if (packet instanceof JsonPacket) {
            String json = readString(input);
            return new Gson().fromJson(json, packet.getClass());
        } else {
            packet.readFromStream(input);
            return packet;
        }
    }

    public static void writePacket(Packet packet, DataOutputStream output) throws IOException {
        output.writeInt(PacketRegistry.getPacketID(packet));
        packet.writeToStream(output);
    }

    public static void safeWritePacket(Packet packet, DataOutputStream output) {
        try {
            writePacket(packet, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Packet getNewPacket(int id) {
        try {
            return PacketRegistry.getRegistry().get(id).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate transferable of Packet ID: " + id + "!");
        }
    }

    /* READING/WRITING UTILS */
    public static void writeString(String string, DataOutputStream output) throws IOException {
        if (string.length() > Packet.MAX_STRING_SIZE)
            throw new IOException("String length (" + string.length() + ") is over the max value of " + Packet.MAX_STRING_SIZE);
        else {
            output.writeShort(string.length());
            output.writeChars(string);
        }
    }

    public static String readString(DataInputStream input) throws IOException {
        short size = input.readShort();
        if (size < 0)
            throw new IOException("Received string length is less than zero! Weird string!");
        else {
            StringBuilder string = new StringBuilder();
            for (int i = 0; i < size; i++)
                string.append(input.readChar());
            return string.toString();
        }
    }

    public static void writeByteArray(DataOutputStream output, byte... bytes) throws IOException {
        output.writeShort(bytes.length);
        output.write(bytes);
    }

    public static byte[] readBytes(DataInputStream input) throws IOException {
        short bytesToRead = input.readShort();
        if (bytesToRead <= 0)
            throw new IOException("Invalid Packet Header: Number of Bytes=" + bytesToRead);
        byte[] buffer = new byte[bytesToRead];
        input.read(buffer);
        return buffer;
    }

    public static void writeList(List<? extends Transferable> list, DataOutputStream output) throws IOException {
        output.writeShort(list.size());
        for (Transferable object : list)
            object.writeToStream(output);
    }

    public static List<Transferable> readList(Class<? extends Transferable> clazz, DataInputStream input) throws IOException {
        short size = input.readShort();
        List<Transferable> returnList = new ArrayList<Transferable>();
        for (int i = 0; i < size; i++) {
            try {
                Transferable object = ((Transferable) clazz.newInstance());
                object.readFromStream(input);
                returnList.add(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnList;
    }

    public static <T> List<T> convertList(List<? extends Transferable> list) {
        List<T> returnList = new ArrayList<T>();
        for (Transferable transferable : list)
            returnList.add((T) transferable);
        return returnList;
    }

}
