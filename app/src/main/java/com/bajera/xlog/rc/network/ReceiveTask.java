package com.bajera.xlog.rc.network;

import android.os.AsyncTask;

import com.bajera.xlog.rc.models.Data;

import java.io.IOException;

public class ReceiveTask extends AsyncTask<Connection, Void, DataReceiveNotification> {

    private NetworkObserver observer;

    public ReceiveTask(NetworkObserver observer) {
        this.observer = observer;
    }


    @Override
    protected DataReceiveNotification doInBackground(Connection... connections) {
        Connection connection = connections[0];
        boolean success = false;
        Data data = null;
        try {
            data = connection.receive();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DataReceiveNotification(Connection.actionReceive, success, connection, data);
    }

    @Override
    protected void onPostExecute(DataReceiveNotification result) {
        observer.notify(result);
    }
}
