package com.saeed.smarthouseclient;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, Bluetooth.DiscoveryCallback {

    private Button scan;
    private Bluetooth mBluetooth;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private List<BluetoothDevice> devices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        mBluetooth = new Bluetooth(this);
        mBluetooth.setDiscoveryCallback(this);
        mBluetooth.enableBluetooth();
        devices = new ArrayList<>();
        scan = (Button) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clear();
                        scan.setEnabled(false);
                        scan.setText("Scanning...");
                    }
                });
                devices = new ArrayList<>();
                mBluetooth.scanDevices();
            }
        });
        if(Utils.readSharedSetting(this,"pairedDevice","false")!="false"){
            Intent i = new Intent(MainActivity.this, InfoActivity.class);
            startActivity(i);
            finish();
        }
        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
    }

    private void getDevices() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mBluetooth.pair(devices.get(i));
    }

    @Override
    public void onFinish() {
        scan.setText("Scan finished!,Click to Scan Again");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scan.setEnabled(true);
                listView.setEnabled(true);
            }
        });
    }

    @Override
    public void onDevice(BluetoothDevice device) {
        final BluetoothDevice tmp = device;
        devices.add(device);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(tmp.getAddress()+" - "+tmp.getName());
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onPair(BluetoothDevice device) {
        Intent i = new Intent(MainActivity.this, InfoActivity.class);
        Utils.saveSharedSetting(this,"pairedDevice","true");
        startActivity(i);
        finish();
    }

    @Override
    public void onUnpair(BluetoothDevice device) {

    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
