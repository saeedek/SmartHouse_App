package com.saeed.smarthouseclient;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

/**
 * Created by ektes on 09-Aug-17.
 */

public class MyApp extends Application {

    public static BluetoothDevice myDevice;
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
    }
}
