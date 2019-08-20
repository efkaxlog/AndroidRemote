package com.bajera.xlog.rc.models;

/**
 * This class is only used for holding a server's information.
 * No functionality or network operations will take place here.
 */
public class Server {

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
