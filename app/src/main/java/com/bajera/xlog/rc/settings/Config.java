package com.bajera.xlog.rc.settings;

/**
 * Until a real config file or Android's SharedPreferences is implemented, this class will
 * hold some settings and values.
 */
public abstract class Config {
    public static int discovery_port = 9997;
    public static int receive_port = 9998;
    public static int send_port = 9999;
    public static boolean autoConnect = true; // whether to auto connect to last server
}
