package com.bajera.xlog.rc.models;

import java.util.HashMap;

public class Tasks {

    private static HashMap<String, String> tasks;

    static {
        // The values from the map is the data that will be sent to the server to execute.
        // Until needed more flexible functionality, task strings are hardcoded here.
        tasks = new HashMap<>();
        tasks.put("shutdown", "cmd|shutdown -s -t 0");
        tasks.put("shutdown_args", "cmd|shutdown -s -t "); // number will be added to this string
        tasks.put("right_click", "mouse|click|right");
        tasks.put("left_click", "mouse|click|left");
        tasks.put("alt_tab", "keys|press|alt|tab");
        tasks.put("space", "keys|press|space");
        tasks.put("volume_up", "keys|press|volumeup");
        tasks.put("volume_down", "keys|press|volumedown");
        tasks.put("volume_mute", "keys|press|volumemute");
        tasks.put("play_pause", "keys|press|playpause");
        tasks.put("request_screenshot", "request_screenshot");
    }

    public static String get(String key) {
        return tasks.get(key);
    }
}
