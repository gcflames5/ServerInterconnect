package net.njay.serverinterconnect.file;

import event.*;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.packet.file.FileDataPacket;
import net.njay.serverinterconnect.packet.file.FileInfoPacket;
import net.njay.serverinterconnect.packet.reponse.Response;
import net.njay.serverinterconnect.utils.response.ResponseUtil;

public class FileReciever implements Listener{

    protected FileFilter fileFilter;

    public FileReciever(FileFilter fileFilter){
        this.fileFilter = fileFilter;
    }

    protected void setupResposnes(){
        final FileReciever instance = this;
        ResponseUtil.getDefaultResponder().respondTo(FileInfoPacket.class, new Response() {
            @Override
            public void run() {
                FileInfoPacket fileInfoPacket = (FileInfoPacket) packet;
                if (fileFilter.filter(fileInfoPacket)){
                    FilePacketListener filePacketListener = new FilePacketListener(fileInfoPacket);
                    Event.addListener(filePacketListener);
                }
            }
        });
    }

    class FilePacketListener implements Listener{
        protected FileInfoPacket fileInfo;

        public FilePacketListener(FileInfoPacket fileInfo){
            this.fileInfo = fileInfo;
        }

        @EventHandler
        public void onFilePacketRecieve(PacketRecievedEvent e){
            if (fileInfo == null) return;
            if (!(e.getPacket() instanceof FileDataPacket)) return;
            FileDataPacket dataPacket = (FileDataPacket) e.getPacket();
            if (dataPacket.getPacketUUID().equals(fileInfo.getPacketUUID())){

            }
        }

    }

}
