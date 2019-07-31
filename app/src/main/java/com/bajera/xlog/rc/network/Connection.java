package com.bajera.xlog.rc.network;

import com.bajera.xlog.rc.models.Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Connection class used for handling client server network operations.
 */
public class Connection {

    public static final String actionConnect = "connect";
    public static final String actionSendData = "sendData";
    public static final String actionDisconnect  = "disconnect";

    private Server server;
    private Socket socket;
    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;
    private InputStream inputStream;
    private BufferedReader reader;
    private int timeout = 200; // ms

    /**
     * Sets up a socket and connects to server.
     */
    public void connect(Server server) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(server.getAddress(), server.getPort()), timeout);
        socket.setSoTimeout(timeout);
        outputStream = socket.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);
        inputStream = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(inputStream));
        this.server = server;
    }

    /**
     * Sends a data String to the connected server.
     */
    public void send(String data) throws IOException {
        String dataSize = Integer.toString(data.length());
        dataOutputStream.write(dataSize.getBytes());
        receiveAck();
        dataOutputStream.write(data.getBytes());
        receiveAck();
    }

    /**
     * Receive a byte from the server. Most likely as confirmation that it received some data.
     */
    private void receiveAck() throws IOException {
        reader.read();
    }

    /**
     * Lets the server know that connection will be closed and closes all streams and the socket.
     */
    public void close() throws IOException {
        send("close");
        receiveAck();
        outputStream.close();
        dataOutputStream.close();
        inputStream.close();
        reader.close();
        socket.close();
    }

    public Server getServer() {
        return server;
    }
}