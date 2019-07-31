package com.bajera.xlog.rc.models;

/**
 * This class is only used for holding a server's information.
 * No functionality or network operations will take place here.
 */
public class Server {

    private String hostname, address;
    private int port;

    public Server(String hostname, String address, int port) {
        this.hostname = hostname;
        this.address = address;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
