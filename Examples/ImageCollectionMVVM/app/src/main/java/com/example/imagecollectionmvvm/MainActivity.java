package com.example.imagecollectionmvvm;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.imagecollectionmvvm.listeners.ImageCollectionListener;

import java.util.List;


public class MainActivity extends AppCompatActivity implements ImageCollectionListener {
    private ImageCollectionViewModel imageCollectionViewModel;
    ImageView addCollectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //recyclerView.setHasFixedSize(true);

        ImageCollectionAdapter adapter = new ImageCollectionAdapter(this);
        recyclerView.setAdapter(adapter);

        /*
        optionsMenu = new PopupMenu(getApplicationContext(), recyclerView);
        optionsMenu.getMenu().add("ELIMINA");

        optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().toString().equalsIgnoreCase("elimina")){
                    Toast.makeText(MainActivity.this, "Elimino", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });*/


        imageCollectionViewModel = new ViewModelProvider(this).get(ImageCollectionViewModel.class);

        /**
         * LiveData ci permette di aggiornare la nostra activity solo se è in primo piano
         * quando l'activity viene distrutta, livedata pulirà automaticamente la reference all'activity
         * */
        imageCollectionViewModel.getAllImageCollections().observe(this, new Observer<List<ImageCollection>>() {
            @Override
            public void onChanged(List<ImageCollection> imageCollections) {
                Toast.makeText(MainActivity.this, "View updated", Toast.LENGTH_SHORT).show();

                /**
                 * ogni volta che onChanged è invocato (ogni volta che i dati cambiano)
                 * il nostro adapter viene aggiornato
                 * */
                adapter.setImageCollectionList(imageCollections);
            }
        });

        addCollectionButton = findViewById(R.id.add_collection_button);
        addCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddImageCollectionActivity.class));
            }
        });

    }

    @Override
    public void onItemClicked(int position, View view, ImageCollection collection) {
        Toast.makeText(this, "Nota cliccata, pos: " + position, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, FocusViewActivity.class);
        intent.putExtra("collection", collection);
        startActivity(intent);

    }

    @Override
    public void onItemLongPressed(int position, View view, ImageCollection collection) {




        PopupMenu deleteMenu = new PopupMenu(getApplicationContext(), view);
        deleteMenu.getMenu().add("ELIMINA");
        deleteMenu.show();





        deleteMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        //NotesDatabase.getNotesDatabase(view.getContext()).noteDao().deleteNote(note);
                        //Toast.makeText(MainActivity.this, "Elimino nota", Toast.LENGTH_SHORT).show();
                        ImageCollectionDatabase.getInstance(view.getContext()).imageCollectionDao().delete(collection);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void unused) {
                        super.onPostExecute(unused);
                        Toast.makeText(getApplicationContext(), "Collezione eliminata!", Toast.LENGTH_SHORT).show();

                        //* effettuo il refresh della home,
                        //                         * non andrebbe proprio fatto, sarebbe preferibile usare altri meccanismi,
                        //                         * ma visto che nell'applicazione finale non dovrò eliminare
                        //                         * entries nel database (da traccia) non c'è motivo di modificare
                        //                         * tutta l'applicazione solo per questa feature



                    }
                }

                if (menuItem.getTitle().toString().equalsIgnoreCase("elimina")){

                    new DeleteNoteTask().execute();
                    return true;
                }

                return false;
            }


        });



    }
}