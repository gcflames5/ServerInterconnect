package net.njay.serverinterconnect.file;

import net.njay.serverinterconnect.packet.file.FileInfoPacket;

public interface FileFilter {

    /**
     * A method that filters incoming files and accepts/rejects them
     *
     * @param packet info of incoming file
     * @return whether or not to accept the incoming file
     */
    public boolean filter(FileInfoPacket packet);

}
