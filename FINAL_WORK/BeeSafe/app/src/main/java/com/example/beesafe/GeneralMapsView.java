package com.example.beesafe;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beesafe.listeners.UserReferenceListener;
import com.example.beesafe.model.Report;
import com.example.beesafe.utils.Constants;
import com.example.beesafe.utils.FirebaseUserHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.beesafe.databinding.ActivityGeneralMapsViewBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class GeneralMapsView extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener{

    private GoogleMap mMap;
    private ActivityGeneralMapsViewBinding binding;

    private ArrayList<Report> reports;
    private boolean isMultiple;

    private ImageView backButton;
    private TextView centerButton;
    private ImageView previousPinButton;
    private ImageView nextPinButton;
    private TextView focusButton;

    private int currentPinIndex;
    private float currentZoomLevel;
    private float currentFocusZoomLevel;
    private ArrayList<LatLng> positions;

    private boolean isAdmin = false;

    private HashMap<String, Report> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isMultiple = (Boolean) getIntent().getBooleanExtra(Constants.MAPS_MULTIPLE_FLAG_NAME, false);
        if (isMultiple){
            Bundle args = getIntent().getBundleExtra(Constants.MAPS_REPORT_ELEMENTS_NAME);
            reports = (ArrayList<Report>) args.getSerializable("ARRAYLIST");
        } else{
            reports = new ArrayList<>();
            reports.add((Report) getIntent().getSerializableExtra(Constants.MAPS_REPORT_ELEMENT_NAME));
            currentPinIndex = 0;
        }

        binding = ActivityGeneralMapsViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        setViews();
    }

    /**
     * viene eseguito quando la callback di onMapReady viene invocata
     * @param googleMap la mappa
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        currentPinIndex = 0;
        currentZoomLevel = Constants.ZOOM_LEVEL;
        currentFocusZoomLevel = Constants.FIRST_FOCUS_LEVEL;


        if (reports != null && reports.size() != 0){
            positions = new ArrayList<>();

            for (Report report : reports){
                // display on map
                LatLng currentPosition = new LatLng(report.getLatitude(), report.getLongitude());
                MarkerOptions currentMarker = new MarkerOptions().position(currentPosition).title(report.getPlaceName());

                Marker marker =  mMap.addMarker(currentMarker);

                if (isAdmin)
                    markers.put(marker.getId(), report);

                positions.add(currentPosition);
            }
            //mapsListener.onAllMarkersPlaced(reports, positions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positions.get(currentPinIndex), currentZoomLevel));
        }

        if (isAdmin)
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(@NonNull Marker marker) {
                    Intent viewAnotherReport = new Intent(GeneralMapsView.this, ReportFocusViewActivity.class);
                    // belle le hashmap
                    viewAnotherReport.putExtra(Constants.FOCUS_VIEW_REPORT, markers.get(marker.getId()));

                    startActivity(viewAnotherReport);
                }
            });

    }

    /**
     * usato per gestire i click sulle varie view
     * @param view la view su cui è stato effettuato il click
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.new_maps_back_button:
                onBackPressed();
                break;
            case R.id.next_pin:
                if ((currentPinIndex + 1) >= positions.size())
                    currentPinIndex = 0;
                currentPinIndex += 1;
                moveCamera();
                break;
            case R.id.previous_pin:
                if ((currentPinIndex - 1) < 0)
                    currentPinIndex = positions.size();
                currentPinIndex -= 1;
                moveCamera();
                break;
            case R.id.center_map:
                centerCamera();
                break;
            case R.id.focus_pin:
                setFocusZoom();
                moveCamera(currentFocusZoomLevel);
                break;
        }
    }

    /**
     * muove la camera della mappa
     */
    private void moveCamera(){
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(positions.get(currentPinIndex), currentZoomLevel);
        mMap.animateCamera(location);
    }

    /**
     * muove la camera della mappa ad uno zoom personalizzato
     * @param zoom lo zoom personalizzato
     */
    private void moveCamera(float zoom){
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(positions.get(currentPinIndex), zoom);
        mMap.animateCamera(location);
    }

    /**
     * muove la camera della mappa allo zoom di default
     */
    private void centerCamera(){
        currentZoomLevel = Constants.ZOOM_LEVEL;
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(positions.get(currentPinIndex), currentZoomLevel);
        mMap.animateCamera(location);
    }

    /**
     * imposta lo zoom a seconda del livello di focus corrente
     */
    @SuppressLint("SetTextI18n")
    private void setFocusZoom(){
        if (currentZoomLevel == Constants.ZOOM_LEVEL) {
            currentFocusZoomLevel = Constants.FIRST_FOCUS_LEVEL;
            currentZoomLevel = currentFocusZoomLevel;
            focusButton.setText("focus (1)");
        } else if (currentZoomLevel == Constants.FIRST_FOCUS_LEVEL) {
            currentFocusZoomLevel = Constants.SECOND_FOCUS_LEVEL;
            currentZoomLevel = currentFocusZoomLevel;
            focusButton.setText("focus (2)");
        } else if (currentZoomLevel == Constants.SECOND_FOCUS_LEVEL) {
            currentFocusZoomLevel = Constants.THIRD_FOCUS_LEVEL;
            currentZoomLevel = currentFocusZoomLevel;
            focusButton.setText("focus (3)");
        } else if (currentZoomLevel == Constants.THIRD_FOCUS_LEVEL) {
            currentFocusZoomLevel = Constants.FIRST_FOCUS_LEVEL;
            currentZoomLevel = currentFocusZoomLevel;
            focusButton.setText("focus");
        }
    }

    /**
     * usato per trovare le views nel layout xml
     */
    private void setViews(){
        backButton = findViewById(R.id.new_maps_back_button);
        backButton.setOnClickListener(this);

        centerButton = findViewById(R.id.center_map);
        centerButton.setOnClickListener(this);

        nextPinButton = findViewById(R.id.next_pin);
        previousPinButton = findViewById(R.id.previous_pin);
        if (isMultiple){
            previousPinButton.setOnClickListener(this);
            nextPinButton.setOnClickListener(this);
        } else {
            previousPinButton.setVisibility(View.GONE);
            nextPinButton.setVisibility(View.GONE);
        }

        focusButton = findViewById(R.id.focus_pin);
        focusButton.setOnClickListener(this);


        if (FirebaseUserHelper.getInstance().getUserReference().getRole().equalsIgnoreCase(Constants.ADMINISTRATOR)){
            if (isMultiple){
                nextPinButton.setVisibility(View.VISIBLE);
                previousPinButton.setVisibility(View.VISIBLE);
            }
            isAdmin = true;
            markers = new HashMap<>();
        }else{
            nextPinButton.setVisibility(View.GONE);
            previousPinButton.setVisibility(View.GONE);
            isAdmin = false;
        }
    }


}