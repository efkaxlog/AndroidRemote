package com.bajera.xlog.rc.models;

import java.io.Serializable;

/**
 * This class is only used for holding a server's information.
 * No functionality or network operations will take place here.
 */
public class Server implements Serializable {

    private String hostname, address;

    public Server(String hostname, String address) {
        this.hostname = hostname;
        this.address = address;
    }

    public String getHostname() {
        return hostname;
    }

    public String getAddress() {
        return address;
    }
}
