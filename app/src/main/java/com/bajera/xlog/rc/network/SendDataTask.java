package com.bajera.xlog.rc.network;

import android.os.AsyncTask;

import java.io.IOException;

public class SendDataTask extends AsyncTask<Object, Void, NetworkNotification> {

    private boolean expectingResponse;
    private Connection connection;
    private NetworkObserver observer;

    public SendDataTask(NetworkObserver observer, boolean expectResponse) {
        this.observer = observer;
        this.expectingResponse = expectResponse;
    }

    @Override
    protected NetworkNotification doInBackground(Object... params) {
        this.connection = (Connection) params[0];
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
        result.setExpectingResponse(expectingResponse);
        observer.notify(result);
    }
}
