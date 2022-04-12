package com.example.notetakingappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;

public class AddNoteActivity extends AppCompatActivity {

    EditText titleInput;
    EditText descriptionInput;
    Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        okButton = findViewById(R.id.saveButton);


        Realm.init(getApplicationContext());

        // con questo oggetto potremo aggiungere le note al DB
        Realm realm = Realm.getDefaultInstance();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                long creationTimestamp = System.currentTimeMillis();

                realm.beginTransaction();   // avviamo la transazione

                Note nota = realm.createObject(Note.class);
                nota.title = title;
                nota.description = description;
                nota.creationTimeStamp = creationTimestamp;

                realm.commitTransaction();

                Toast.makeText(getApplicationContext(), "Nota salvata!", Toast.LENGTH_SHORT).show();

                finish();   // chiudiamo l'activity
            }
        });
    }
}