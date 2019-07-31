package com.bajera.xlog.rc.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class SendDataTask extends AsyncTask<Object, Void, NetworkNotification> {

    private NetworkObserver observer;

    public SendDataTask(NetworkObserver observer) {
        this.observer = observer;
    }

    @Override
    protected NetworkNotification doInBackground(Object... params) {
        Log.v("SendDataTask", "In doInBackgrround(...)");
        Connection connection = (Connection) params[0];
        String data = (String) params[1];
        boolean success = false;
        try {
            connection.send(data);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new NetworkNotification(Connection.actionSendData, success, connection);
    }

    @Override
    protected void onPostExecute(NetworkNotification result) {
        Log.v("SendDataTask", "in onPostExecute()");
        observer.notify(result);
    }
}
