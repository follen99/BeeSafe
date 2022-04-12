package com.example.notetakingappv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {
    MaterialButton addNoteButton;
    RecyclerView allNotesView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setTitle("Notes app");
        //getActionBar().setIcon(R.drawable.ic_notes_icon);

        this.addNoteButton = findViewById(R.id.addNewNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                /**
                 * quando clicco sul button, si apre la nuova activity per aggiungere una nota
                 * */
                startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
            }
        });


        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Note> notes = realm.where(Note.class).findAll().sort("creationTimeStamp", Sort.DESCENDING);
        allNotesView = findViewById(R.id.recyclerView);

        allNotesView.setLayoutManager(new LinearLayoutManager(this));
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(getApplicationContext(), notes);
        allNotesView.setAdapter(adapter);

        notes.addChangeListener(new RealmChangeListener<RealmResults<Note>>() {
            @Override
            public void onChange(RealmResults<Note> notes) {
                adapter.notifyDataSetChanged(); // ogni volta che i dati cambiano notifichiamo
            }
        });
    }
}