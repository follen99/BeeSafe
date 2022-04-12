package com.example.beesafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beesafe.utils.Constants;
import com.example.beesafe.utils.FirebaseUserHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class NoConnectionActivity extends AppCompatActivity implements View.OnClickListener{
    private WebView easterEgg;
    private ImageView backButton;

    private ImageView noInternet;
    private ImageView connectedToInternet;
    private boolean isConnectionShowing;
    // il path dell'index per aprire l'html che contiene il minigame
    private String url = "file:///android_asset/index.html";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);
        isConnectionShowing = false;

        easterEgg = findViewById(R.id.easter_egg);
        // abilito javascript nella view
        easterEgg.getSettings().setJavaScriptEnabled(true);
        easterEgg.loadUrl(url);

        backButton = findViewById(R.id.easter_egg_back);
        backButton.setOnClickListener(this);

        noInternet = findViewById(R.id.easter_egg_no_internet);
        noInternet.setOnClickListener(this);

        connectedToInternet = findViewById(R.id.easter_egg_connected);
        connectedToInternet.setOnClickListener(this);

        if (!isConnectionShowing){
            noInternet.setVisibility(View.VISIBLE);
            isConnectionShowing = false;
        }



        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isNetworkAvailable()){
                    // aggiorno solo se necessario
                    if (!isConnectionShowing)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                connectedToInternet.setVisibility(View.VISIBLE);
                                noInternet.setVisibility(View.GONE);
                                isConnectionShowing = true;
                            }
                        });
                }else{
                    // aggiorno solo se necessario
                    if (isConnectionShowing){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                connectedToInternet.setVisibility(View.GONE);
                                noInternet.setVisibility(View.VISIBLE);
                                isConnectionShowing = false;
                            }
                        });
                    }
                }
            }
        }, 0, 5000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.easter_egg_back:
                onBackPressed();
                break;
            case R.id.easter_egg_no_internet:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(NoConnectionActivity.this)
                                .setTitle(Constants.NO_CONNECTION_TITLE)
                                .setMessage(Constants.NO_CONNECTION_MINIGAME_MESSAGE)
                                .setPositiveButton("Ho capito", null)
                                .setIcon(R.drawable.ic_baseline_wifi_off_24)
                                .show();
                    }
                });
                break;
            case R.id.easter_egg_connected:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(NoConnectionActivity.this)
                                .setTitle(Constants.CONNECTED_TO_INTERNET_MINIGAME_TITLE)
                                .setMessage(Constants.CONNECTED_TO_INTERNET_MINIGAME_MESSAGE)
                                .setNegativeButton("Rimani qui", null)
                                .setPositiveButton("Torna alla home", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(NoConnectionActivity.this, MainActivity.class));
                                        finish();
                                    }
                                })
                                .setIcon(R.drawable.ic_baseline_cloud_circle_24)
                                .show();
                    }
                });
                break;
        }
    }



    /**
     * usato per controllare se la connessione è disponibile
     * @return booleano che ci dice se si è connessi ad internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}