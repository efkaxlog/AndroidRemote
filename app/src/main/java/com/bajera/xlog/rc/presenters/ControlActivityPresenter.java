package com.bajera.xlog.rc.presenters;

import com.bajera.xlog.rc.network.Connection;
import com.bajera.xlog.rc.models.Server;
import com.bajera.xlog.rc.settings.SharedPreferencesManager;
import com.bajera.xlog.rc.models.Tasks;
import com.bajera.xlog.rc.network.ConnectTask;
import com.bajera.xlog.rc.network.DisconnectTask;
import com.bajera.xlog.rc.network.NetworkNotification;
import com.bajera.xlog.rc.network.NetworkObserver;
import com.bajera.xlog.rc.network.SendDataTask;

public class ControlActivityPresenter implements NetworkObserver {

    private View view;
    private String toolbarTitle = "Controls";
    private Server server;
    private Connection connection;
    private SharedPreferencesManager sharedPrefs;

    public ControlActivityPresenter(View view, SharedPreferencesManager sharedPrefs) {
        this.view = view;
        this.sharedPrefs = sharedPrefs;
        this.server = this.sharedPrefs.getLastServer();
    }

    public void onViewCreated() {
        view.setToolbarText(toolbarTitle, server.getHostname());
    }

    public void onTaskClick(String task) {
        new SendDataTask(this).execute(connection, Tasks.get(task));
    }

    public void onResume() {
        new ConnectTask(this).execute(server);
    }

    public void onStop() {
        new DisconnectTask(this).execute(connection);
    }

    @Override
    public void notify(NetworkNotification notification) {
        switch (notification.getMessage()) {
            case Connection.actionConnect:
                this.connection = notification.getConnection();
                break;
            case Connection.actionSendData:
                break;
            case Connection.actionDisconnect:
                break;
            default:
                break;
        }
    }

    public interface View {
        void setToolbarText(String title, String subtitle);
    }
}
