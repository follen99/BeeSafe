package com.example.beesafe.listeners;

import com.example.beesafe.model.Report;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface MapsListener {

    public void onAllMarkersPlaced(Report[] reports, ArrayList<LatLng> positions);
}
