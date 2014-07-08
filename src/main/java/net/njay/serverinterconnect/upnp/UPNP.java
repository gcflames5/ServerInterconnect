package net.njay.serverinterconnect.upnp;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class UPNP {

	public static int forward(int start, int end) throws SocketException, UnknownHostException, IOException, SAXException, ParserConfigurationException {
		UPNPConnector connector = new UPNPConnector(start, end);
		return connector.forward();	
	}
}
