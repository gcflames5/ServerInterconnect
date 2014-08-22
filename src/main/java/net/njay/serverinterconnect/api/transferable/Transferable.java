package net.njay.serverinterconnect.api.transferable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Transferable {

    /**
     * Signifies that the class can be sent over and read from a stream
     */
	public Transferable() {	}

    /**
     * Writes all class members to a DataOutputStream
     *
     * @param output stream to write to
     * @throws IOException
     */
	public abstract void writeToStream(DataOutputStream output) throws IOException;

    /**
     * Reads all class members from a DataInputStream (must maintain same order as writeToStream(...)
     *
     * @param input stream to read from
     * @throws IOException
     */
	public abstract void readFromStream(DataInputStream input)throws IOException;

}
