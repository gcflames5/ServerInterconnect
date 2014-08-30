package net.njay.serverinterconnect.file;

public class FileAggregator {

    private int index;
    private byte[] bytes;

    public FileAggregator(int size){
        bytes = new byte[size];
        index = 0;
    }

    public void addData(byte... bytes){
        if (index+bytes.length > this.bytes.length)
            throw new IllegalArgumentException("Byte array too large! Max: " +
                this.bytes.length + "  Attempted: " + (index+bytes.length));
        for (int i = 0; i < bytes.length; i++)
            this.bytes[index++] = bytes[i];
    }

    public byte[] getBytes(){ return this.bytes; }

}
