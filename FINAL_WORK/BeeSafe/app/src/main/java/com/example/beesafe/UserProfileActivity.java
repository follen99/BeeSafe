package com.example.beesafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.beesafe.model.Report;
import com.example.beesafe.model.User;
import com.example.beesafe.utils.Constants;
import com.example.beesafe.utils.DigestHelper;
import com.example.beesafe.utils.FirebaseUserHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;


public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name;
    private TextView email;

    private ImageView backButton;
    private TextView changePasswordButton;
    private TextView logoutButton;

    private TextView totalReportsStat;
    private TextView mostFrequentReportStat;

    private TextView isAdmin;
    private LinearLayout launchMinigameButton;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Window window = UserProfileActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(UserProfileActivity.this, R.color.color_accent));

        name = findViewById(R.id.profile_user_name);
        email = findViewById(R.id.profile_user_email);
        backButton = findViewById(R.id.profile_back_button);
        changePasswordButton = findViewById(R.id.profile_change_password);
        logoutButton = findViewById(R.id.profile_logout);
        totalReportsStat = findViewById(R.id.profile_stats_total_reports);
        mostFrequentReportStat = findViewById(R.id.profile_most_frequent_report );
        isAdmin = findViewById(R.id.profile_is_admin);


        backButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        name.setText(FirebaseUserHelper.getInstance().getUserReference().getFullName());
        email.setText(FirebaseUserHelper.getInstance().getUserReference().getEmail());

        launchMinigameButton = findViewById(R.id.profile_launch_minigame);
        launchMinigameButton.setOnClickListener(this);

        getAllReportsRequest();

    }

    /**
     * usato per inviare una richiesta http volta a recuperare tutti i report relativi all'utente loggato
     */
    private void getAllReportsRequest(){
        FirebaseUserHelper firebaseUserHelper = FirebaseUserHelper.getInstance();

        if (firebaseUserHelper.getUserReference().getRole().equalsIgnoreCase(Constants.ADMINISTRATOR))
            isAdmin.setVisibility(View.VISIBLE);
        else isAdmin.setVisibility(View.GONE);

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://" + Constants.REST_HOSTNAME + ":" + Constants.REST_PORT + "/reports")
                .addHeader("uid", firebaseUserHelper.getCurrentFirebaseUser().getUid())
                .addHeader("digest", DigestHelper.getDigestFromCurrentUser())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //Log.d("OkHttp", e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                String responseBody = response.body().string();


                Gson gson = new Gson();
                Report[] reports = gson.fromJson(responseBody, Report[].class);

                /**
                 * non posso modificare l'interfaccia su un thread diverso da quello destinato alla UI
                 * */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        totalReportsStat.setText("Reports totali: " + reports.length);
                        totalReportsStat.setVisibility(View.VISIBLE);

                        String mostUsedKindOfProblem = getMostUsedReport(reports);
                        if (mostUsedKindOfProblem != null) {
                            mostFrequentReportStat.setText("Report più frequente:\n" + getMostUsedReport(reports));
                            mostFrequentReportStat.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    /**
     * L'esame di algoritmi e strutture dati è servito a qualcosa hehe
     * @param reports la lista dei reports scaricati
     * @return la categoria più comune
     */
    private String getMostUsedReport(Report[] reports){
        HashMap<String, Integer> kindOfProblemMap = new HashMap<>();

        for (Report report : reports){
            if (kindOfProblemMap.containsKey(report.getKindOfProblem())){
                kindOfProblemMap.put(report.getKindOfProblem(), kindOfProblemMap.get(report.getKindOfProblem())+1);
            } else {
                kindOfProblemMap.put(report.getKindOfProblem(), 1);
            }
        }

        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : kindOfProblemMap.entrySet()){
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
                maxEntry = entry;
            }
        }

        try {
            return maxEntry.getKey();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * usato per gestire i tocchi sullo schermo
     * @param view la vista
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_back_button:
                onBackPressed();
                break;
            case R.id.profile_logout:
                FirebaseAuth.getInstance().signOut();
                FirebaseUserHelper.destroySingleton();
                goBackToLogin();
                break;
            case R.id.profile_change_password:
                startActivity(new Intent(UserProfileActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.profile_launch_minigame:
                startActivity(new Intent(UserProfileActivity.this, NoConnectionActivity.class));
                break;
        }
    }

    /**
     * usato per tornare alla schermata di login
     */
    private void goBackToLogin(){
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        startActivity(intent);

        // finish() fondamentale: non permette all'utente di tornare indietro alla home
        finish();
    }
}