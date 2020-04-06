package com.tracker.covid_19tracker.client;

import android.os.Build;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.client.packets.out.PacketOut;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Client implements Runnable {

    private static final String HOST = "159.65.228.221";
    private static final int PORT = 8080;
    private static final int TIMEOUT = 5 * 1000;

    private MainActivity mainActivity;
    private Thread connectionThread;
    private PacketHandler packetHandler;
    private Handler handler;
    private Socket socket;
    private BufferedReader bufferedReader;
    private OutputStream outputStream;
    private volatile Queue<PacketOut> outgoing;
    private volatile boolean listening;
    private boolean connected = false;

    public Client(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.connectionThread = new Thread(this);
        this.packetHandler = new PacketHandler(mainActivity);
        this.handler = new Handler(mainActivity.getMainLooper());
        this.outgoing = new LinkedList<>();
        this.listening = true;
    }

    public synchronized void connect(){
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
            this.connected = true;
            Log.d("Debugging", "Successfully connected");
        } catch (IOException e) {
            this.connected = false;
        }

        onConnectionAttempt(connected);

        while (listening && connected){

            // Send any pending packets
            if (!outgoing.isEmpty()){
                PacketOut packet = outgoing.poll();
                if (packet != null) {
                    Log.d("Debugging", packet.toString());
                    try {
                        Log.d("Debugging", "Sending packet: " + new String(packet.getData()));
                        outputStream.write(packet.getData());
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("Client Error", "Error sending packet");
                    }
                }
            }

            // Listen for response
            String msg = null;

            try {
                if (bufferedReader.ready()) {
                    msg = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (msg == null){
                continue;
            }

            Log.d("Debugging", msg);

            try {
                packetHandler.handlePacket(msg);
            } catch (JSONException e){
                e.printStackTrace();
                Log.e("Client Error", "Error parsing packet");
            }
        }

        this.connected = false;
        stop();
    }

    public abstract void onConnectionAttempt(boolean connected);

    public void send(PacketOut packetOut){
        outgoing.add(packetOut);
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

        try {
            connectionThread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean isConnected() {
        return connected;
    }
}
