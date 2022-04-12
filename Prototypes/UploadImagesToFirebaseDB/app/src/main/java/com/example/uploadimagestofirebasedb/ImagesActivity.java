package com.example.uploadimagestofirebasedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {
    // questa è l'email dell'utente che ha effettuato la richiesta, successivamente verrà ricavata dalla sessione.
    private static final String userEmail = "ranaurogln@gmail.com";

    private RecyclerView recyclerView;
    private ImageAdapter adapter;

    private DatabaseReference databaseReference;
    private List<Image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        images = new ArrayList<>();

        // il riferimento al db punta alla collezione di immagini (db non storage!)
        databaseReference = FirebaseDatabase.getInstance().getReference("images").child(userEmail.replace(".", "-"));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Image image = postSnapshot.getValue(Image.class);
                    images.add(image);
                }

                adapter = new ImageAdapter(ImagesActivity.this, images);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImagesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}