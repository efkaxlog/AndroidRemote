package com.bajera.xlog.rc.network;

import android.util.Log;

import com.bajera.xlog.rc.models.Data;
import com.bajera.xlog.rc.models.Server;
import com.bajera.xlog.rc.presenters.ControlActivityPresenter;
import com.bajera.xlog.rc.settings.Config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * Connection class used for handling client server network operations.
 */
public class Connection {

    public static final String actionConnect = "connect";
    public static final String actionSendData = "sendData";
    public static final String actionDisconnect = "disconnect";
    public static final String actionPing = "ping";
    public static final String actionReceive = "receiveData";

    private Server server;
    private Socket recvSocket;
    private Socket senderSocket;
    private DataOutputStream recvDataOutputStream;
    private DataInputStream recvDataInputStream;
    private DataOutputStream senderDataOutputStream;
    private DataInputStream senderDataInputStream;
    private int defaultTimeout = 200; // ms
    private int receiveTimeout = 2000; // for receiving operations

    private ControlActivityPresenter presenter;

    /**
     * Sets up a socket and connects to server.
     */
    public void connect(Server server) throws IOException {
        Log.v("Connect()", server.getAddress());
        recvSocket = connectSocket(server.getAddress(), Config.receive_port);
        recvDataOutputStream = new DataOutputStream(recvSocket.getOutputStream());
        recvDataInputStream = new DataInputStream(recvSocket.getInputStream());
        senderSocket = connectSocket(server.getAddress(), Config.send_port);
        senderDataOutputStream = new DataOutputStream(senderSocket.getOutputStream());
        senderDataInputStream = new DataInputStream(senderSocket.getInputStream());
        this.server = server;
    }

    private Socket connectSocket(String address, int port) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(address, port), defaultTimeout);
        socket.setSoTimeout(defaultTimeout);
        return socket;
    }

    /**
     * Sends a data String to the connected server.
     */
    public void send(String data) throws IOException {
        senderDataOutputStream.writeInt(data.length());
        receiveAck(senderDataInputStream);
        Log.v("Send()", "data size: " + data.length());
        senderDataOutputStream.write(data.getBytes());
        senderDataOutputStream.flush();
        receiveAck(senderDataInputStream);
    }

    public Data receive() throws IOException {
        recvSocket.setSoTimeout(receiveTimeout);
        int length = recvDataInputStream.readInt();
        int dataTypeLength = recvDataInputStream.readInt();
        byte[] data = new byte[length];
        byte[] dataTypeBytes = new byte[dataTypeLength];
        recvDataInputStream.readFully(dataTypeBytes);
        String dataType = new String(dataTypeBytes);
        recvDataInputStream.readFully(data);
        sendAck(recvDataOutputStream);
        recvSocket.setSoTimeout(defaultTimeout);
        return new Data(dataType, data);
    }

    /**
     * Receive a byte from the server. Most likely as confirmation that it received some data.
     */
    private void receiveAck(DataInputStream dataInputStream) throws IOException {
        dataInputStream.read();
    }

    private void sendAck(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.write(1);
    }

    /**
     * Lets the server know that connection will be closed and closes all streams and the socket.
     */
    public void close() throws IOException {
        send("close");
        receiveAck(recvDataInputStream);
        recvDataOutputStream.close();
        recvDataInputStream.close();
        senderDataOutputStream.close();
        senderDataInputStream.close();
    }

    /**
     * Send some data and receive a bit too.
     */
    public void ping() throws IOException {
        send("ping");
        receiveAck(senderDataInputStream);
    }

    public Server getServer() {
        return server;
    }

    public void setPresenter(ControlActivityPresenter presenter) {
        this.presenter = presenter;
    }
}