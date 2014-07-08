package net.njay.serverinterconnect.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet {

    public static final int MAX_STRING_SIZE = 32767;

	public static ArrayList<Class<? extends Packet>> registry = new ArrayList<Class<? extends Packet>>();
	
	public static void registerPacket(Class<? extends Packet> clazz){
		registry.add(clazz);
	}
	
	public static final int getPacketID(Packet packet){ 
		for (int i = 0; i < registry.size(); i++)
			if (registry.get(i) == packet.getClass())
				return i;
		throw new RuntimeException("Failed to find packet id for: " + packet.getClass() + " (did you forget to register it?)");
	}
	
	public static Packet readPacket(DataInputStream input) throws IOException{
		int packetID = input.readInt();
		if (packetID >= registry.size())
			throw new RuntimeException("Invalid Packet id " + packetID);
		Packet packet = getNewPacket(packetID);
		packet.readPacketContent(input);
		return packet;
	}
	
	public static void writePacket(Packet packet, DataOutputStream output) throws IOException{
		output.writeInt(getPacketID(packet));
		packet.writePacketContent(output);
	}
	
	public static void safeWritePacket(Packet packet, DataOutputStream output){
		try{ writePacket(packet, output); }
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public static Packet getNewPacket(int id) {
		try{ return registry.get(id).newInstance(); }
		catch(Exception e){ throw new RuntimeException("Failed to instantiate packet of Packet ID: " + id + "!"); }
	}
	
	/* READING/WRITING UTILS */
	public static void writeString(String string, DataOutputStream output) throws IOException{
        if (string.length() > MAX_STRING_SIZE)
            throw new IOException("String length (" + string.length() + ") is over the max value of " + MAX_STRING_SIZE);
        else{
        	output.writeShort(string.length());
            output.writeChars(string);
        }
    }

    public static String readString(DataInputStream input) throws IOException{
        short size = input.readShort();
        if (size < 0)
            throw new IOException("Received string length is less than zero! Weird string!");
        else{
            StringBuilder string = new StringBuilder();
            for (int i = 0; i < size; i++)
            	string.append(input.readChar());
            return string.toString();
        }
    }
    
    public static void writeByteArray(DataOutputStream output, byte... bytes) throws IOException{
    	output.writeShort(bytes.length);
    	output.write(bytes);
    }
    
    public static byte[] readBytes(DataInputStream input) throws IOException{
		short bytesToRead = input.readShort();
		if (bytesToRead <= 0)
			throw new IOException("Invalid Packet Header: Number of Bytes=" + bytesToRead);
		byte[] buffer = new byte[bytesToRead];
		input.read(buffer);
		return buffer;
	}
    
    public static void writeList(List<? extends Transferable> list, DataOutputStream output) throws IOException{
    	output.writeShort(list.size());
    	for (Transferable object : list)
    		object.writeToStream(output);
    }
    
    public static List<Transferable> readList(Class<? extends Transferable> clazz, DataInputStream input) throws IOException{
    	short size = input.readShort();
    	List<Transferable> returnList = new ArrayList<Transferable>();
    	for (int i = 0; i < size; i++){
    		try{
	    		Transferable object = ((Transferable)clazz.newInstance());
	    		object.readFromStream(input);
	    		returnList.add(object);
    		}catch(Exception e){ e.printStackTrace(); }
    	}
    	return returnList;
    }
    
    public static <T> List<T> convertList(List<? extends Transferable> list){
    	List<T> returnList = new ArrayList<T>();
    	for (Transferable transferable : list)
    		returnList.add((T) transferable);
    	return returnList;
    }
	/*-----------------------*/
	
	public abstract void readPacketContent(DataInputStream input) throws IOException;
	public abstract void writePacketContent(DataOutputStream output) throws IOException;
}
