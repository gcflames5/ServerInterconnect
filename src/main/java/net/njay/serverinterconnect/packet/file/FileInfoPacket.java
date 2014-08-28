package net.njay.serverinterconnect.packet.file;

import net.njay.serverinterconnect.packet.JsonPacket;

public class FileInfoPacket extends JsonPacket {

    protected String name;
    protected long size;

    public FileInfoPacket() {}

    public FileInfoPacket(String name, long size){
        this.name = name;
        this.size = size;
    }

    public String getName(){ return this.name; }
    public long getSize(){ return this.size; }

}
