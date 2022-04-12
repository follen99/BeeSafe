package com.example.notetakingapplocal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                // Inform User
                Toast.makeText(MainActivity.this, "Data Changed", Toast.LENGTH_SHORT).show();

                //update RecyclerView
                adapter.setNotes(notes);

            }
        });


        FloatingActionButton buttonAddNote = findViewById(R.id.addButton);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "entro", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AddOrModifyNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent i = new Intent(MainActivity.this, AddOrModifyNoteActivity.class);
                i.putExtra(AddOrModifyNoteActivity.EXTRA_ID,note.getId());
                i.putExtra(AddOrModifyNoteActivity.EXTRA_TITLE,note.getTitle());
                i.putExtra(AddOrModifyNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
                i.putExtra(AddOrModifyNoteActivity.EXTRA_PRIORITY,note.getPriority());
                startActivityForResult(i, EDIT_NOTE_REQUEST);
            }
        });

    }

    // Esempio di risposta dell'activity ( saved , cancelled )
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddOrModifyNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddOrModifyNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddOrModifyNoteActivity.EXTRA_PRIORITY, 1);
            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);
            Toast.makeText(this, "Nota salvata", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddOrModifyNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "La nota non può essere aggiornata", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddOrModifyNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddOrModifyNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddOrModifyNoteActivity.EXTRA_PRIORITY, 1);
            Note note = new Note(title, description, priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Nota aggiornata", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Nota non salvata", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "Note cancellate!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}