package net.njay.serverinterconnect.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DiskFileAggregator {

    public static final String PART_EXTENSION = "part";

    private File file;
    private File partFile;
    private FileOutputStream outputStream;

    public DiskFileAggregator(File file){
        this.file = file;
        this.partFile = new File(file.getParentFile(), file.getName()+"."+PART_EXTENSION);
        try {
            this.outputStream = new FileOutputStream(this.partFile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to open file output stream!");
        }
    }

    public void addData(byte... data) throws IOException {
        outputStream.write(data);
    }

    public void finish() throws IOException {
        outputStream.close();
        partFile.renameTo(file);
    }

    public File getFile(){ return this.file; }

}
