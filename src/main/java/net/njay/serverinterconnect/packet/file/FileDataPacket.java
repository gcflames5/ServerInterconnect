package net.njay.serverinterconnect.packet.file;

import net.njay.serverinterconnect.packet.JsonPacket;

public class FileDataPacket extends JsonPacket{
    
    protected int id;
    protected byte[] data;

    public FileDataPacket(int id, byte... data){
        this.id = id;
        this.data = data;
    }

    public int getId(){ return this.id; }
    public byte[] getData(){ return this.data; }

}
