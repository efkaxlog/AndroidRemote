package com.bajera.xlog.rc.network;

import java.io.IOException;

/**
 * Class used to ping a server. The plan was to make this class extend AsyncTask but the
 * Pinger.ping() method will be called once every e.g. 1 second so it's easier to manage like this.
 */
public class Pinger {

    private NetworkObserver observer;
    private volatile boolean running = true;
    private int delay = 1000; // milliseconds

    public Pinger(NetworkObserver observer) {
        this.observer = observer;
    }

    public PingNotification ping(Connection connection) {
        long pingTime = 0;
        long startTime = System.currentTimeMillis();
        boolean success = false;
        try {
            connection.ping();
            pingTime = System.currentTimeMillis() - startTime;
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PingNotification(Connection.actionPing, success, connection, (int) pingTime);
    }

    /**
     * Starts the main pinging loop. Will be running until boolean running is set to false.
     */
    public void start(final Connection connection) {
        running = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    PingNotification result = ping(connection);
                    observer.notify(result);
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * Set the running flag to false which will stop the thread as apparently manually
     * stopping a thread is unsafe and Thread.stop() is deprecated because of that reason.
     */
    public void stop() {
        running = false;
    }

}
