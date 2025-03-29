package com.example.hotspot_control;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private HotspotManager hotspotManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.connectedDevices);
        hotspotManager = new HotspotManager(this, listView);
        hotspotManager.fetchConnectedDevices();
    }
}
