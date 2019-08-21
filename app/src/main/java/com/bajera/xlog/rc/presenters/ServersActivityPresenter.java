package com.bajera.xlog.rc.presenters;


import com.bajera.xlog.rc.models.Server;
import com.bajera.xlog.rc.settings.Config;
import com.bajera.xlog.rc.settings.SharedPreferencesManager;
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
    private boolean autoConnect = Config.autoConnect;

    public ServersActivityPresenter(
            ServersActivity view, SharedPreferencesManager sharedPrefs) {
        servers = new ArrayList<>();
        this.view = view;
        this.sharedPrefs = sharedPrefs;
    }

    /**
     * View calls this after initializing everything.
     */
    public void onViewCreated() {
        view.setupServersListView(servers);
        serverSearch();
    }

    /**
     * A server was chosen by the user.
     */
    public void onListViewItemClicked(String hostname, String address) {
        Server server = new Server(hostname, address);
        view.openControlsActivity(server);
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
        if (autoConnect && isLastServerOnline()) {
            view.openControlsActivity(sharedPrefs.getLastServer());
        }
        autoConnect = false; // makes sure auto connects to last server only when app is started.

    }

    private void clearServers() {
        servers.clear();
        view.notifyServerListChanged();
    }

    /**
     * @return true if last used server is in the class field servers ArrayList
     */
    private boolean isLastServerOnline() {
        Server lastServer = sharedPrefs.getLastServer();
        if (lastServer != null) {
            for (Server server : servers) {
                /** Checks against server address and hostname. Something to keep in mind is that
                 *  a machine's address and hostname could change from the last time it was connected to.
                 */
                if (server.getAddress().equals(lastServer.getAddress()) &&
                        server.getHostname().equals(lastServer.getHostname())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void notify(NetworkNotification notification) {
        switch (notification.getMessage()) {
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

        void openControlsActivity(Server server);
    }
}
