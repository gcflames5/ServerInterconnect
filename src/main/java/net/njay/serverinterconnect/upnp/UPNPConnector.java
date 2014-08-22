package net.njay.serverinterconnect.upnp;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UPNPConnector {

	private int[] ports;
	
	public UPNPConnector(int[] ports){
		this.ports = ports;
	}
	
	public UPNPConnector(int portMin, int portMax){
		ports = new int[portMax---portMin];
		for (int i = portMin, index = 0; i <= portMax; i++, index++)
			ports[index] = i;
	}
	
	public int forward() throws SocketException, UnknownHostException, IOException, SAXException, ParserConfigurationException{
		for (int port : this.ports){
			if (scan(port))
				return port;
		}
		return 0; //Failed!
	}
	
	private boolean scan(int port) throws SocketException, UnknownHostException, IOException, SAXException, ParserConfigurationException{
		UPNPManager upnp = new UPNPManager(port);
		upnp.discover();
		return upnp.map();
	}

}
