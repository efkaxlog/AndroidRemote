package com.bajera.xlog.rc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Class used for broadcasting to LAN in order to find active servers on the network.
 */
public class ServerDiscovery implements Observable {

    private int port;
    private String address = "255.255.255.255"; // Broadcast address
    private InetAddress inetAddress;
    private DatagramSocket socket;
    private int responseBufferSize= 1024;

    ServerDiscovery(int port) throws SocketException, UnknownHostException {
        this.port = port;
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        socket.setSoTimeout(Config.socketTimeout);
        inetAddress = InetAddress.getByName(address);
    }

    /**
     * Broadcast to servers, and receive responses.
     * @param servers ArrayList where found servers will be added.
     */
    public void findServers(ArrayList<Server> servers) throws IOException {
        String message = "server_info_request";
        DatagramPacket messagePacket = new DatagramPacket(
                message.getBytes(), message.getBytes().length, inetAddress, port);
        socket.send(messagePacket);

        while (true) {
            byte[] buffer = new byte[responseBufferSize];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(responsePacket);
            } catch (SocketTimeoutException e) {
                break;
            }

            String hostname = new String(
                    responsePacket.getData(), 0, responsePacket.getLength());
            Server server = new Server(
                    hostname, responsePacket.getAddress().getHostAddress(), Config.port);
            servers.add(server);
            notifyObservers();
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}
