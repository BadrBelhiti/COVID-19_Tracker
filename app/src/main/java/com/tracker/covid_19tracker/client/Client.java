package com.tracker.covid_19tracker.client;

import android.os.Build;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client implements Runnable {

    private static final String HOST = "google.com";
    private static final int PORT = 80;
    private static final int TIMEOUT = 5 * 1000;

    private MainActivity mainActivity;
    private Thread connectionThread;
    private PacketHandler packetHandler;
    private Handler handler;
    private Socket socket;
    private BufferedReader bufferedReader;
    private OutputStream outputStream;
    private volatile boolean listening = true;

    public Client(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.connectionThread = new Thread(this);
        this.packetHandler = new PacketHandler();
        this.handler = new Handler(mainActivity.getMainLooper());
    }

    public synchronized void start(){
        connectionThread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {

        this.socket = new Socket();

        try {
            socket.connect(new InetSocketAddress(HOST, PORT), TIMEOUT);
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            this.outputStream = socket.getOutputStream();
            Log.d("Debugging", "Successfully connected");
        } catch (IOException e) {
            e.printStackTrace();
            closeOnError("Failed to connect to server.");
            return;
        }

        while (listening){
            String msg = null;

            try {
                msg = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (msg != null){
                packetHandler.handlePacket(msg);
                Log.d("Debugging", msg);
            }
        }
    }

    private void closeOnError(final String errMsg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                mainActivity.exit(errMsg);
            }
        });
    }

    public synchronized boolean stop(){
        this.listening = false;
        try {
            connectionThread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
            return false;
        }

        try {
            if (socket != null) {
                socket.close();
            }

            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
