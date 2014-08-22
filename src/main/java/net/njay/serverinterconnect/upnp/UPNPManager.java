package net.njay.serverinterconnect.upnp;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UPNPManager {

	private int port;
	private GatewayDevice d;
	
	public UPNPManager(int port){
		this.port = port;
	}
	
	public GatewayDevice discover() throws SocketException, UnknownHostException, IOException, SAXException, ParserConfigurationException{
		GatewayDiscover discover = new GatewayDiscover();
		Log.debug("Looking for Gateway Devices");
		discover.discover();
		this.d = discover.getValidGateway();
		return this.d;
	}
	
	public boolean map() throws IOException, SAXException{
		if (d != null) {
		    Log.debug("Gateway device found.\n + " + new Object[]{d.getModelName() + d.getModelDescription()});
		} else {
		    Log.debug("No valid gateway device found.");
		    return false;
		}

		InetAddress localAddress = d.getLocalAddress();
		Log.debug("Using local address: " + localAddress);
		String externalIPAddress = d.getExternalIPAddress();
		Log.debug("External address: " + externalIPAddress);
		PortMappingEntry portMapping = new PortMappingEntry();

		Log.debug("Attempting to map port {0} " + port);
		Log.debug("Querying device to see if mapping for port {0} already exists " + port);

		if (!d.getSpecificPortMappingEntry(port,"TCP",portMapping)) {
		    Log.debug("Sending port mapping request");

		    if (d.addPortMapping(port,port,localAddress.getHostAddress(),"TCP","test")) {
		        Log.debug("Mapping succesful!");
		        return true;
		 
		    } else {
		        Log.debug("Port mapping removal failed");
		        return false;
		    }
		    
		} else {
		    Log.debug("Port was already mapped. Deleting and trying again!");
		    d.deletePortMapping(port, "TCP");
		    return map();
		}
	}
	
	public void remove() throws IOException, SAXException{
	      d.deletePortMapping(port,"TCP");
	      Log.debug("Port mapping removed");

	}

}
