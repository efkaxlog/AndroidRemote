package com.bajera.xlog.rc.network;

/**
 * A NetworkNotification with additional int variable for storing ping time.
 */
public class PingNotification extends NetworkNotification {

    private int pingTime;

    public PingNotification(String message, boolean success, Connection connection, int pingTime) {
        super(message, success, connection);
        this.pingTime = pingTime;
    }

    public int getPingTime() {
        return pingTime;
    }
}
