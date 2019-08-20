package com.bajera.xlog.rc.util;

import android.graphics.Bitmap;

public class Util {

    public static Bitmap bitmapFromBytes(byte[] data, int width, int height) {
        int[] colors = new int[data.length / 3];
        int alpha = 255;
        if ((data.length / 3) != (width * height)) {
            throw new ArrayStoreException();
        }
        for (int index = 0; index < data.length - 2; index += 3) {
            colors[index / 3] = (alpha & 0xff) << 24 | (data[index] & 0xff) << 16 | (data[index + 1] & 0xff) << 8 | (data[index + 2] & 0xff);
        }
        return Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
    }
}
