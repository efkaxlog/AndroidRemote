package com.bajera.xlog.rc.presenters;

import com.bajera.xlog.rc.settings.Config;
import com.bajera.xlog.rc.network.Connection;
import com.bajera.xlog.rc.models.Server;
import com.bajera.xlog.rc.settings.SharedPreferencesManager;
import com.bajera.xlog.rc.network.ConnectTask;
import com.bajera.xlog.rc.network.DisconnectTask;
import com.bajera.xlog.rc.network.NetworkNotification;
import com.bajera.xlog.rc.network.NetworkObserver;
import com.bajera.xlog.rc.network.ServerDiscovery;
import com.bajera.xlog.rc.ui.ServersActivity;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ServersActivityPresenter implements NetworkObserver {

    private SharedPreferencesManager sharedPrefs;
    private ArrayList<Server> servers;
    private View view;
    private boolean searching = false;

    public ServersActivityPresenter (
            ServersActivity view, SharedPreferencesManager sharedPrefs)
    {
        servers = new ArrayList<>();
        this.view = view;
        this.sharedPrefs = sharedPrefs;
    }

    /**
     * View calls this after initializing everything.
     */
    public void onViewCreated() {
        view.setupServersListView(servers);
        if (Config.autoConnect) {
            Server lastServer = sharedPrefs.getLastServer();
            if (lastServer != null) {
                // checks if lastServer is online by trying to connect to it
                new ConnectTask(this).execute(lastServer);
            }
        } else {
            serverSearch();
        }
    }

    /**
     * Called after attempting to connect to a server. This will open a different activity if
     * connecting was successful.
     * Not sure whether connecting and disconnecting to check if can connect, and then connecting
     * again in new activity is the best way of doing this but it works so far.
     */
    public void onServerConnectAttempt(Connection connection, boolean success) {
        Server server = connection.getServer();
        if (success) {
            sharedPrefs.setLastServer(server);
            new DisconnectTask(this).execute(connection);
            view.openControlsActivity();
        } else {
            // todo, maybe UI feedback so user knows that connecting to server failed?
        }
    }

    /**
     * A server was chosen by the user.
     */
    public void onListViewItemClicked(String hostname, String address) {
        Server server = new Server(hostname, address, Config.port);
        sharedPrefs.setLastServer(server);
        view.openControlsActivity();
    }

    /**
     * Starts the task of scanning the network for any online servers.
     */
    public void serverSearch() {
        if (!searching) {
            clearServers();
            searching = true;
            try {
                new ServerDiscovery(this).execute(servers);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called when searching for servers is done.
     */
    private void serverSearchDone() {
        searching = false;
        view.serverSearchDone();
    }

    private void clearServers() {
        servers.clear();
        view.notifyServerListChanged();
    }

    @Override
    public void notify(NetworkNotification notification) {
        switch (notification.getMessage()) {
            case Connection.actionConnect:
                onServerConnectAttempt(notification.getConnection(), notification.success());
                break;
            case ServerDiscovery.serverSearchDone:
                serverSearchDone();
                break;
            default:
                break;
        }
    }

    public interface View {
        void setupServersListView(ArrayList<Server> servers);
        void serverSearchDone();
        void notifyServerListChanged();
        void openControlsActivity();
    }
}
