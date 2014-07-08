ServerInterconnect
==================
A quick and secure solution for all java client-server needs.

Tutorial
======================

Setting up the Server
---
```java
//First, select the port that you want to listen o
int port = 1337;

//Use the static generateServerSocket(int) from TcpSocketFactory to automatically generate an SSLServerSocket with default settings. If you would like to use a different encryption, you can use your own socket 
SSLServerSocket socket = TcpSocketFactory.generateServerSocket(port)

//Create a new Server Manager with the socket
TcpServerManager manager = new TcpServerManager(socket);
```

Setting up the Client
----
```java
//Address that you wish to connect to
String address = "127.0.0.1";

//Port that you wish to connect to
int port = 1337

//Create a client manager
TcpClientManager manager = new TcpClientManager(address, port);

//Initialize the connection
try { manager.initialize(); } 
catch (IOException e) { e.printStackTrace(); }
```

Setting up the Listeners
-----
```java
//make the class implement Listener
class PacketHandler implements Listener{
    
    public PacketHandler(){
        //register the listener
        Event.addListener(this);
    }
    
    //create the handler
    @EventHander //necessary
    public void packetRecieved(PacketRecievedEvent e){
        //do something with recieved packet
    }

}
```

Sending From the Server
-------

```java
TcpServerManager manager = new TcpServerManager(socket); //manager we made before

for (TcpConnection connection : manager.getConnections()){
    //check to see whether this is a desired destination
    connection.sendPacket(new MessagePacket("Test Message"));
}
```


Sending From the Client
-------

```java
TcpClientManager manager = new TcpClientManager(address, port); // manager we made before

manager.getConnection().sendPacket(new MessagePacket("Test Message"));
```

Making a New Packet
------
```java
class NewPacket extends Packet{
    private String testString;
    
    public NewPacket(){} //MUST be present
    
    public NewPacket(String testString){
        this.testString = testString;
    }
    
    @Override
    public void readPacketContent(DataInputStream input) throws IOException {
		testString = Packet.readString(input);
	}

	@Override
	public void writePacketContent(DataOutputStream output) throws IOException {
		Packet.writeString(testString, output);		
	}
    
    //remember to read/write in the same order!
}
```
*Each packet must be assigned a new ID and registered in the registry, like this:
```java
Packet.registerPacket(1, new NewPacket().getClass());
```