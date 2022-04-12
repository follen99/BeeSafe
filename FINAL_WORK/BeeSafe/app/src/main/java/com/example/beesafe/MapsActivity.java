package com.example.beesafe;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.beesafe.listeners.MapsListener;
import com.example.beesafe.model.Report;
import com.example.beesafe.utils.Constants;
import com.example.beesafe.utils.DigestHelper;
import com.example.beesafe.utils.FirebaseUserHelper;
import com.example.beesafe.utils.LocalDatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.beesafe.databinding.ActivityMapsBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Deprecated
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private ImageView backButton;

    private final OkHttpClient client = new OkHttpClient();

    private MapsListener mapsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMapsListener(this);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        backButton = findViewById(R.id.maps_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        /*
        // Add a marker in Sydney and move the camera
        LatLng apiceVecchio = new LatLng(41.119374, 14.931535);
        LatLng apice = new LatLng(41.118960,14.914732);
        mMap.addMarker(new MarkerOptions().position(apice).title("Apice"));
        mMap.addMarker(new MarkerOptions().position(apiceVecchio).title("Apice vecchio"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(apice, Constants.ZOOM_LEVEL));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);*/

        FirebaseUserHelper userHelper = FirebaseUserHelper.getInstance();
        Request getAllReportsRequest = new Request.Builder()
                .url("http://" + Constants.REST_HOSTNAME + ":" + Constants.REST_PORT + "/reports")
                .addHeader("uid", userHelper.getCurrentFirebaseUser().getUid())
                .addHeader("digest", DigestHelper.getDigestFromCurrentUser())
                .build();

        client.newCall(getAllReportsRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //Log.d("OkHttp", e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                String responseBody = response.body().string();


                Gson gson = new Gson();
                Report[] reports = gson.fromJson(responseBody, Report[].class);

                /*for (Report report : reports)
                    Log.d("getallrequests", report.toString());*/

                if(FirebaseUserHelper.getInstance().getUserReference().getRole().equalsIgnoreCase(Constants.ADMINISTRATOR)){
                    /**
                     * non posso modificare l'interfaccia con un thread diverso da quello che l'ha creata
                     * per questo motivo uso questa porzione di codice per eseguire l'aggiornamento
                     * sul thread principale
                     */


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<LatLng> positions = new ArrayList<>();

                            for (Report report : reports){
                                // display on map
                                LatLng currentPosition = new LatLng(report.getLatitude(), report.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(currentPosition).title(report.getPlaceName()));

                                positions.add(currentPosition);
                            }
                            mapsListener.onAllMarkersPlaced(reports, positions);
                        }

                    });
                }else{
                    Toast.makeText(MapsActivity.this, "Non sei autorizzato!", Toast.LENGTH_SHORT).show();
                    goBackToLogin();
                }




            }
        });

    }

    private void goBackToLogin(){
        Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
        startActivity(intent);

        // finish() fondamentale: non permette all'utente di tornare indietro alla home
        finish();
    }

    public void setMapsListener(MapsListener listener){
        this.mapsListener = listener;
    }

    @Override
    public void onAllMarkersPlaced(Report[] reports, ArrayList<LatLng> positions) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positions.get(0), Constants.ZOOM_LEVEL));
    }
}