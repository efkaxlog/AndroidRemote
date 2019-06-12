package com.bajera.xlog.rc;

/**
 * Until a real config file or Android's SharedPreferences is implemented, this class will
 * hold some settings and values.
 */
public abstract class Config {
    public static int port = 9999;
    public static int socketTimeout = 2000; // milliseconds
}
