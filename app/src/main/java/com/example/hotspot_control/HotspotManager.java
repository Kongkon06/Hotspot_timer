package com.example.hotspot_control;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HotspotManager {
    private static final String TAG = "HotspotManager";
    private final ListView listView;
    private final ArrayAdapter<String> adapter;
    private final ArrayList<String> connectedDevices = new ArrayList<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public HotspotManager(Context context, ListView listView) {
        this.listView = listView;
        this.adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, connectedDevices);
        this.listView.setAdapter(adapter);
    }

    public void fetchConnectedDevices() {
        executor.execute(() -> {
            ArrayList<String> devices = getConnectedDevices();
            mainHandler.post(() -> {
                connectedDevices.clear();
                connectedDevices.addAll(devices);
                adapter.notifyDataSetChanged();
            });
        });
    }

    private ArrayList<String> getConnectedDevices() {
        ArrayList<String> devices = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 4 && !parts[0].equals("IP")) {
                    String ip = parts[0];
                    String mac = parts[3];
                    devices.add("IP: " + ip + " | MAC: " + mac);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading /proc/net/arp", e);
        }
        return devices;
    }
}
