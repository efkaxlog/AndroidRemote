package com.bajera.xlog.rc.models;


/**
 * Generic notification, used for notifying something of what happened and whether it was successful.
 */
public class Notification {

    private String message;
    private boolean success;

    public Notification(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean success() {
        return success;
    }
}
