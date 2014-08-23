package net.njay.serverinterconnect.api.transferable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Signifies that the class can be sent over and read from a stream
 */
public interface Transferable {

    /**
     * Writes all class members to a DataOutputStream
     *
     * @param output stream to write to
     * @throws IOException
     */
    public void writeToStream(DataOutputStream output) throws IOException;

    /**
     * Reads all class members from a DataInputStream (must maintain same order as writeToStream(...)
     *
     * @param input stream to read from
     * @throws IOException
     */
    public void readFromStream(DataInputStream input) throws IOException;

}
