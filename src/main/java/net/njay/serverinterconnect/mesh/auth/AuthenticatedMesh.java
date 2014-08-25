package net.njay.serverinterconnect.mesh.auth;

import net.njay.serverinterconnect.mesh.Mesh;

public class AuthenticatedMesh extends Mesh {

    public AuthenticatedMesh(int listenPort, String... ipsToConnect) {
        super(listenPort, ipsToConnect);
    }


}
