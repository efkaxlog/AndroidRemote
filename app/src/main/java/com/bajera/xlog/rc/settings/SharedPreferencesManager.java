package com.bajera.xlog.rc.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.bajera.xlog.rc.R;
import com.bajera.xlog.rc.models.Server;

/**
 * Singleton class for saving/loading data which is saved to device, like Android's SharedPreferences.
 */
public class SharedPreferencesManager {

    private static SharedPreferencesManager sharedPreferencesManager = null;
    private Context context;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPrefsEditor;

    private SharedPreferencesManager(Context context) {
        this.context = context;
        sharedPrefs = context.getSharedPreferences(
                context.getString((R.string.preference_file_key)), Context.MODE_PRIVATE);
        sharedPrefsEditor = sharedPrefs.edit();
    }

    public static SharedPreferencesManager getInstance(Context context) {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = new SharedPreferencesManager(context);
        }
        return sharedPreferencesManager;
    }

    /**
     * Writes last connected server to SharedPreferences
     */
    public void setLastServer(Server server) {
        sharedPrefsEditor.putString(
                context.getString(R.string.key_last_server_hostname), server.getHostname());
        sharedPrefsEditor.putString(
                context.getString(R.string.key_last_server_address), server.getAddress());
        sharedPrefsEditor.commit();
    }

    /**
     *
     */
    public Server getLastServer() {
        String hostname = sharedPrefs.getString(
                context.getString(R.string.key_last_server_hostname), null);
        String address = sharedPrefs.getString(
                context.getString(R.string.key_last_server_address), null);
        if (hostname != null && address != null) {
            return new Server(hostname, address);
        } else {
            return null;
        }
    }
}
