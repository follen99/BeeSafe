package com.example.beesafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beesafe.adapters.LogsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LogsView extends AppCompatActivity implements View.OnClickListener {
    private LogsAdapter adapter;
    private DatabaseReference logsDbReference;
    private ImageView backButton;
    private ImageView refreshButton;
    private ImageView searchButton;
    private EditText searchField;
    private TextView noLogsMessage;

    private HashMap<String, List<com.example.beesafe.model.Log>> logsByUser;
    private ArrayList<com.example.beesafe.model.Log> logs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs_view);
        logsByUser = new HashMap<>();

        RecyclerView recyclerView = findViewById(R.id.logs_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new LogsAdapter(this);
        recyclerView.setAdapter(adapter);

        backButton = findViewById(R.id.logs_back_button);
        backButton.setOnClickListener(this);

        refreshButton = findViewById(R.id.logs_refresh);
        refreshButton.setOnClickListener(this);

        searchButton = findViewById(R.id.logs_search_button);
        searchButton.setOnClickListener(this);

        searchField = findViewById(R.id.search_input);
        noLogsMessage = findViewById(R.id.no_log_message);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        logsDbReference = database.getReference("logs");
        logsDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logs = new ArrayList<>();

                for (DataSnapshot logSnapshot : snapshot.getChildren()){
                    String children = String.valueOf(logSnapshot.getValue());
                    Gson gson = new Gson();
                    com.example.beesafe.model.Log log = gson.fromJson(children, com.example.beesafe.model.Log.class);

                    if (logsByUser.containsKey(log.getUser())){
                        logsByUser.get(log.getUser()).add(log);
                    }else {
                        logsByUser.put(log.getUser(), new ArrayList<>());
                        logsByUser.get(log.getUser()).add(log);
                    }
                    logs.add(log);
                }
                Collections.reverse(logs);
                // limito a 100 risultati
                if (logs.size() > 100)
                    adapter.setLogsList(logs.subList(0,100));
                else adapter.setLogsList(logs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logs_back_button:
                onBackPressed();
                break;
            case R.id.logs_search_button:
                String textFieldText = searchField.getText().toString();
                List<com.example.beesafe.model.Log> retrievedList = new ArrayList<>();

                if (textFieldText.matches("") || searchField == null){
                    searchField.setError("Il campo non può essere vuoto.");
                    searchField.requestFocus();
                }else{
                    retrievedList= logsByUser.get(textFieldText);
                    if (retrievedList == null || retrievedList.isEmpty()){
                        retrievedList = new ArrayList<>();

                        for (String key : logsByUser.keySet()){
                            if (key.contains(textFieldText) || key.startsWith(textFieldText) || key.endsWith(textFieldText)){
                                List<com.example.beesafe.model.Log> foundLogs = logsByUser.get(key);
                                if (foundLogs != null || !foundLogs.isEmpty()){
                                    retrievedList.addAll(foundLogs);
                                }
                            }
                        }
                    }
                }

                if (retrievedList.isEmpty()){
                    noLogsMessage.setVisibility(View.VISIBLE);
                    adapter.setLogsList(new ArrayList<>());
                } else {
                    Collections.sort(retrievedList, Collections.reverseOrder());
                    // limito a 100 risultati
                    if (retrievedList.size() > 100)
                        adapter.setLogsList(retrievedList.subList(0, 100));
                    else adapter.setLogsList(retrievedList);
                    noLogsMessage.setVisibility(View.GONE);
                }
                break;
            case R.id.logs_refresh:
                // limito a 100 risultati
                if (logs.size() > 100)
                    adapter.setLogsList(logs.subList(0,100));
                else adapter.setLogsList(logs);
                noLogsMessage.setVisibility(View.GONE);
                searchField.setText("");
                break;
        }
    }
}