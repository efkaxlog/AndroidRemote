package com.bajera.xlog.rc.network;

import android.os.AsyncTask;
import android.util.Log;

import com.bajera.xlog.rc.settings.Config;
import com.bajera.xlog.rc.models.Server;

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
public class ServerDiscovery extends AsyncTask<ArrayList, Void, NetworkNotification> {

    // Used for Notifications for Observers
    public static final String serverSearchDone = "serverSearchDone";

    private NetworkObserver observer;
    private String address = "255.255.255.255"; // Broadcast address
    private InetAddress inetAddress;
    private DatagramSocket socket;
    private int responseBufferSize = 1024;
    private int socketTimeout = 200; // milliseconds

    public ServerDiscovery(NetworkObserver observer) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        socket.setSoTimeout(socketTimeout);
        inetAddress = InetAddress.getByName(address);
        this.observer = observer;
    }

    /**
     * Broadcast to servers, and receive responses.
     *
     * @param servers ArrayList where found servers will be added.
     */
    public void findServers(ArrayList<Server> servers) throws IOException {
        String message = "server_info_request";
        DatagramPacket messagePacket = new DatagramPacket(
                message.getBytes(), message.getBytes().length, inetAddress, Config.discovery_port);
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
                    hostname, responsePacket.getAddress().getHostAddress());
            servers.add(server);
        }
    }


    @Override
    protected NetworkNotification doInBackground(ArrayList... arrayLists) {
        ArrayList<Server> servers = arrayLists[0];
        boolean success = false;
        try {
            findServers(servers);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new NetworkNotification(serverSearchDone, success, null);
    }

    @Override
    protected void onPostExecute(NetworkNotification result) {
        observer.notify(result);
    }
}