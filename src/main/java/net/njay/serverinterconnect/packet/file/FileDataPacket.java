package net.njay.serverinterconnect.packet.file;

import net.njay.serverinterconnect.packet.JsonPacket;

import java.util.UUID;

public class FileDataPacket extends JsonPacket{

    protected int id;
    protected UUID uuid;
    protected byte[] data;

    public FileDataPacket(int id, UUID uuid, byte... data){
        this.id = id;
        this.uuid = uuid;
        this.data = data;
    }

    public int getId(){ return this.id; }
    public UUID getInfoUuid(){ return this.uuid; }
    public byte[] getData(){ return this.data; }

}
