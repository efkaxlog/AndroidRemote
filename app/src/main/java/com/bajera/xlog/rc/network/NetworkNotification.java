package com.bajera.xlog.rc.network;

import com.bajera.xlog.rc.models.Notification;

/**
 * Notification class specifically for network operations.
 */
public class NetworkNotification extends Notification {

    private Connection connection;

    public NetworkNotification(String message, boolean success, Connection connection) {
        super(message, success);
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

}
