package com.tracker.covid_19tracker.client;

import android.os.Build;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.R;
import com.tracker.covid_19tracker.client.packets.out.PacketOut;
import com.tracker.covid_19tracker.client.packets.out.PacketOutLogin;
import com.tracker.covid_19tracker.files.CachedPacketsFile;
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
import java.util.UUID;

public abstract class Client implements Runnable {

    private static final int TIMEOUT = 5 * 1000;

    private final String HOST;
    private final int PORT;

    private MainActivity mainActivity;
    private Thread connectionThread;
    private PacketHandler packetHandler;
    private Handler handler;
    private Socket socket;
    private BufferedReader bufferedReader;
    private OutputStream outputStream;
    private volatile Queue<PacketOut> outgoing;
    private CachedPacketsFile cachedPacketsFile;
    private volatile boolean listening;
    private boolean connected = false;

    public Client(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.connectionThread = new Thread(this);
        this.packetHandler = new PacketHandler(mainActivity);
        this.handler = new Handler(mainActivity.getMainLooper());
        this.outgoing = new LinkedList<>();
        this.cachedPacketsFile = mainActivity.getFileManager().getCachedPacketsFile();
        this.listening = true;

        this.HOST = mainActivity.getString(R.string.ip_address);
        this.PORT = Integer.parseInt(mainActivity.getString(R.string.port));
    }

    public synchronized void connect(){
        connectionThread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        tryConnecting();

        if (connected){
            trySendingCache();
        }

        while (listening){

            // Do not waste time with a closed connection
            if (!connected){

                tryConnecting();

                if (!connected) {
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    trySendingCache();
                }

            } else {

                // Send any pending packets
                if (!outgoing.isEmpty()) {
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
                            cachedPacketsFile.cache(packet);
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

                if (msg == null) {
                    continue;
                }

                Log.d("Debugging", msg);

                final String finalMsg = msg;
                handler.post(() -> packetHandler.handlePacket(finalMsg));
            }
        }

        this.connected = false;
        shutdown();
    }

    private void tryConnecting(){
        Log.d("Debugging", "Connecting...");
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

        if (connected) {
            PacketOut packet = new PacketOutLogin(mainActivity.getFileManager().getSessionDataFile().getUserId());
            send(packet);
        }
    }

    private void trySendingCache(){
        outgoing.addAll(cachedPacketsFile.getCached());
        cachedPacketsFile.getCached().clear();
    }

    public abstract void onConnectionAttempt(boolean connected);

    public void send(PacketOut packetOut){
        if (connected) {
            outgoing.add(packetOut);
        } else {
            cachedPacketsFile.cache(packetOut);
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

    public synchronized void stop(){
        this.listening = false;
    }

    public void shutdown(){
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
        }

        try {
            connectionThread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
