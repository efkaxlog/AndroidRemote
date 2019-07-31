package com.bajera.xlog.rc.settings;

/**
 * Until a real config file or Android's SharedPreferences is implemented, this class will
 * hold some settings and values.
 */
public abstract class Config {
    public static int port = 9999;
    public static boolean autoConnect = true; // whether to auto connect to last server
}
