package com.bajera.xlog.rc.network;

import android.os.AsyncTask;
import android.util.Log;

import com.bajera.xlog.rc.models.Server;

import java.io.IOException;

public class ConnectTask extends AsyncTask<Server, Void, NetworkNotification> {

    private NetworkObserver observer;

    public ConnectTask(NetworkObserver observer) {
        this.observer = observer;
    }

    @Override
    protected NetworkNotification doInBackground(Server... servers) {
        Connection connection = new Connection();
        boolean success = false;
        try {
            connection.connect(servers[0]);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new NetworkNotification(Connection.actionConnect, success, connection);
    }

    @Override
    protected void onPostExecute(NetworkNotification notification) {
        observer.notify(notification);
    }

}
