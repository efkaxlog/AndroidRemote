package com.bajera.xlog.rc.network;

import android.os.AsyncTask;

import java.io.IOException;

public class DisconnectTask extends AsyncTask<Connection, Void, NetworkNotification> {

    private NetworkObserver observer;

    public DisconnectTask(NetworkObserver observer) {
        this.observer = observer;
    }

    @Override
    protected NetworkNotification doInBackground(Connection... connections) {
        Connection connection = connections[0];
        boolean success = false;
        try {
            connection.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new NetworkNotification(Connection.actionDisconnect, success, connection);
    }

    @Override
    protected void onPostExecute(NetworkNotification result) {
        observer.notify(result);
    }
}
