package com.saeed.smarthouseclient;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import me.aflak.bluetooth.Bluetooth;

public class InfoActivity extends AppCompatActivity implements Bluetooth.CommunicationCallback, View.OnClickListener {

    private Bluetooth mBluetooth;
    private TextView txt_status,txt_temp;
    private Button blue,red,orange,green;
    private boolean b,r,o,g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        b=false;
        r=false;
        o=false;
        g=false;
        txt_status = (TextView) findViewById(R.id.txt_status);
        txt_temp = (TextView) findViewById(R.id.txt_temp);
        blue = (Button) findViewById(R.id.btn_blue);
        blue.setOnClickListener(this);
        red = (Button) findViewById(R.id.btn_red);
        red.setOnClickListener(this);
        orange = (Button) findViewById(R.id.btn_orange);
        orange.setOnClickListener(this);
        green = (Button) findViewById(R.id.btn_green);
        green.setOnClickListener(this);
        mBluetooth = new Bluetooth(this);
        mBluetooth.setCommunicationCallback(this);
        //Toast.makeText(this, MyApp.myDevice.getName(), Toast.LENGTH_SHORT).show();
        mBluetooth.connectToDevice(MyApp.myDevice);
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(mBluetooth.isConnected())
                    handleOutMessage("temp");
            }
        }, 1000, 3000); // initial delay 1 second, interval 1 second
    }

    @Override
    public void onConnect(BluetoothDevice device) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_status.setText("We are connected");
            }
        });
    }


    @Override
    public void onDisconnect(BluetoothDevice device, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_status.setText(message);
            }
        });
    }

    @Override
    public void onMessage(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleInMessage(message);
            }
        });
    }

    private void handleInMessage(String message) {
        String str[] = message.split("#");
        String whattodo="";
        if(str[0].equals("FF")){
            switch (str[1]){
                case "marco":
                    whattodo="polo";
                    break;
                default:
                    txt_temp.setText(str[1]+" "+(char) 0x00B0+"C");
                    break;
            }
            handleOutMessage(whattodo);
        }
    }

    private void handleOutMessage(String whattodo) {
        switch (whattodo){
            case "polo":
                mBluetooth.send("FF#polo#FF");
                break;
            case "temp":
                mBluetooth.send("FF#t#FF");
                break;
            default:
                mBluetooth.send("FF#"+whattodo+"#FF");
                break;
        }
    }

    @Override
    public void onError(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_status.setText(message);
            }
        });
    }

    @Override
    public void onConnectError(BluetoothDevice device, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_status.setText(message);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_blue:
                if(b){
                    handleOutMessage("b0");
                    b=false;
                    blue.setAlpha(.5f);
                }
                else {
                    handleOutMessage("b1");
                    b=true;
                    blue.setAlpha(1f);
                }

                break;
            case R.id.btn_red:
                if(r){
                    handleOutMessage("r0");
                    r=false;
                    red.setAlpha(.5f);
                }
                else{
                    handleOutMessage("r1");
                    r=true;
                    red.setAlpha(1f);
                }
                break;
            case R.id.btn_orange:
                if(o){
                    handleOutMessage("o0");
                    o=false;
                    orange.setAlpha(.5f);
                }
                else{
                    handleOutMessage("o1");
                    o=true;
                    orange.setAlpha(1f);
                }
                break;
            case R.id.btn_green:
                if(g){
                    handleOutMessage("g0");
                    g=false;
                    green.setAlpha(.5f);
                }
                else {
                    handleOutMessage("g1");
                    g=true;
                    green.setAlpha(1f);
                }
                break;
        }
    }
}
