package com.bajera.xlog.rc.network;

import com.bajera.xlog.rc.models.Notification;

/**
 * Notification class specifically for network operations.
 */
public class NetworkNotification extends Notification {

    private Connection connection;
    private boolean expectingResponse = false;

    public NetworkNotification(String message, boolean success, Connection connection) {
        super(message, success);
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isExpectingResponse() {
        return expectingResponse;
    }

    public void setExpectingResponse(boolean expectingResponse) {
        this.expectingResponse = expectingResponse;
    }

}
