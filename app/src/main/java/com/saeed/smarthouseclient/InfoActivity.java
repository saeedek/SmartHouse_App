package com.saeed.smarthouseclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import me.aflak.bluetooth.Bluetooth;

public class InfoActivity extends AppCompatActivity {

    private Bluetooth mBluetooth;
    private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        txt = (TextView) findViewById(R.id.txt);
        mBluetooth = new Bluetooth(this);
        
    }
}
