package com.example.simplenavigationcontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SecondPage extends AppCompatActivity implements OnMapReadyCallback {
    Button secondPageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        secondPageButton = findViewById(R.id.button_second_page);
        secondPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SecondPage.this, "Torno alla prima pagina", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(SecondPage.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng testPosition = new LatLng(37.442140,122.143489);
        googleMap.addMarker(new MarkerOptions()
        .position(testPosition)
        .draggable(false)
        .title("Palo Alto")
        );

        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        // zoom sulla mappa
        float zoomLevel = 16.0f; //This goes up to 21
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(testPosition, zoomLevel));
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }
}