package com.bajera.xlog.rc.network;

import com.bajera.xlog.rc.models.Data;

public class DataReceiveNotification extends NetworkNotification {

    private Data data;

    public DataReceiveNotification(
            String message, boolean success, Connection connection, Data data) {
        super(message, success, connection);
        this.data = data;
    }

    public Data getData() {
        return data;
    }
}
