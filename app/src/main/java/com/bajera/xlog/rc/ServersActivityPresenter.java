package com.bajera.xlog.rc;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ServersActivityPresenter implements Observer {

    private ArrayList<Server> servers;
    private View view;
    private boolean searching = false;

    public ServersActivityPresenter(ServersActivity view) {
        servers = new ArrayList<>();
        this.view = view;
    }

    public void onViewCreated() {
        view.setupServersListView(servers);
        refresh();
    }

    public void onListViewItemClicked(String hostname, String address) {
        Client.setServer(new Server(hostname, address, Config.port));
        view.openControlsActivity();
    }

    /**
     * Starts the task of searching for online servers on the network.
     */
    public void refresh() {
        if (!searching) {
            searching = true;
            view.startServerSearch();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    serverSearch();
                }
            }).start();
        }
    }

    /**
     * Finds severs and updates server ArrayList and prompts ServersActivity to update UI.
     */
    private void serverSearch() {
        ServerDiscovery sc = null;
        try {
            sc = new ServerDiscovery(9999);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (sc != null) {
            clearServers();
            try {
                sc.addObserver(this);
                sc.findServers(servers);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        searching = false;
        view.stopServerSearch();
    }

    private void clearServers() {
        servers.clear();
        view.notifyServerListChanged();
    }

    /**
     * A server has been found! Notify the Activity.
     */
    @Override
    public void update() {
        view.notifyServerListChanged();
    }

    public interface View {
        void setupServersListView(ArrayList<Server> servers);
        void notifyServerListChanged();
        void startServerSearch();
        void stopServerSearch();
        void openControlsActivity();
    }
}
