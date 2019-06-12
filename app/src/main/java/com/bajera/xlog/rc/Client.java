package com.bajera.xlog.rc;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Class used to send and receive data from a server.
 * The whole class is abstract and static because it will be used by multiple activities
 * in the application and there is no reason to instantiate a new object each time or passing
 * it around. It's simpler to just set a new Server object to this class.
 */
public abstract class Client {

    private static Server server;
    private static Socket socket;
    private static OutputStream outputStream;
    private static DataOutputStream dataOutputStream;
    private static InputStream inputStream;
    private static BufferedReader reader;


    /**
     * Sets up a socket and connects to server.
     */
    public static void bind() throws IOException {
        socket = new Socket(server.getAddress(), server.getPort());
        outputStream = socket.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);
        inputStream = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * Sends a string of data to the connected server.
     */
    public static void send(String data) throws IOException {
        Log.v("Client", "Sending");
        String dataSize = Integer.toString(data.length());
        dataOutputStream.write(dataSize.getBytes());
        receiveAck();
        Log.v("Client", "After write and ack");
        dataOutputStream.write(data.getBytes());
        receiveAck();
    }

    /**
     * Receive a byte from the server. Most likely as confirmation that it received some data.
     */
    private static void receiveAck() throws IOException {
        reader.read();
    }

    public static void close() throws IOException {
        send("close");
        receiveAck();
        outputStream.close();
        dataOutputStream.close();
        inputStream.close();
        reader.close();
        socket.close();
    }

    public static void setServer(Server newServer) {
        server = newServer;
    }

    public static Server getServer() {
        return server;
    }
}
