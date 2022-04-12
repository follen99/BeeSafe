package com.example.notetakingappadvanced.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.notetakingappadvanced.R;
import com.example.notetakingappadvanced.adapters.NotesAdapter;
import com.example.notetakingappadvanced.database.NotesDatabase;
import com.example.notetakingappadvanced.entities.Note;
import com.example.notetakingappadvanced.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesListener {
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;    // usato per mostrare tutte le note

    // recycler view che mostrerà le singole note
    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;

    private int noteClickedPosition = -1;   // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {

            /**
             * eseguito quando si clicca sull'immagine per aggiungere una nota
             * (usato com button)
             * */
            @Override
            public void onClick(View view) {
                /**
                 * avviamo una activity che ci restituirà qualcosa
                 * creiamo un Intent con il contesto corrente e la classe di activity
                 * che vogliamo chiamare
                 * aggiungiamo anche il request code, che indica che vogliamo
                 * aggiungere (e non aggiornare) una nota
                 * */

                /**
                 * siccome abbiamo usato startActivityForResult,
                 * dobbiamo ascoltare l'evento di risposta per poter aggiornare
                 * la lista quando aggiungiamo una nota.
                 *
                 * Se non catchiamo l'evento, la lista non viene aggiornata!
                 * */
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });

        // cerchiamo la view
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        // usiamo un manager di tipo StaggeredGrid
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        // inizializziamo la lista
        noteList = new ArrayList<>();
        // istanziamo l'adapter
        // con "this" indichiamo questa classe come listener, visto che implementiamo NotesListener
        notesAdapter = new NotesAdapter(noteList, this, this);
        // aggiungiamo l'adapter alla rv
        notesRecyclerView.setAdapter(notesAdapter);









        // metodo locale per leggere le note dal database
        // quando l'app si apre per la prima volta vuole vedere tutte le note
        getNotes(REQUEST_CODE_SHOW_NOTES);
    }

    @Override
    public void onNoteClicked(Note note, int position, View view) {
        noteClickedPosition = position;
        // usiamo la stessa activity usata per creare la nota, ma passiamo il flag di update, e non create
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);

        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);

        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    @Override
    public void onNoteLongPressed(Note note, int position, View view) {

        // listener per il dialog
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        class DeleteNoteTask extends AsyncTask<Void, Void, Void>{

                            @Override
                            protected Void doInBackground(Void... voids) {
                                NotesDatabase.getNotesDatabase(view.getContext()).noteDao().deleteNote(note);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void unused) {
                                super.onPostExecute(unused);
                                Toast.makeText(view.getContext(), "Nota eliminata!", Toast.LENGTH_SHORT).show();

                                //* effettuo il refresh della home,
                                //                         * non andrebbe proprio fatto, sarebbe preferibile usare altri meccanismi,
                                //                         * ma visto che nell'applicazione finale non dovrò eliminare
                                //                         * entries nel database (da traccia) non c'è motivo di modificare
                                //                         * tutta l'applicazione solo per questa feature


                                // un po sketchy come soluzione
                                recreate();
                            }
                        }


                        new DeleteNoteTask().execute();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        Toast.makeText(MainActivity.this, "Nessun problema.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };



        PopupMenu deleteMenu = new PopupMenu(getApplicationContext(), view);
        deleteMenu.getMenu().add("ELIMINA");
        deleteMenu.getMenu().add("Visualizza Nota");
        deleteMenu.show();



        Context mainContext = this;

        deleteMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getTitle().toString().equalsIgnoreCase("ELIMINA")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
                    builder.setMessage("Are you sure?").setPositiveButton("Si", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                    return true;
                }else if (menuItem.getTitle().toString().equalsIgnoreCase("Visualizza Nota")){
                    // avvia una seconda activity qui

                    startActivity(new Intent(mainContext, ViewNoteActivity.class));
                    return true;
                }




                return false;
        }


    });






    }

    /**
     * Metodo usato per leggere le note dal database
     * anche in questo caso abbiamo bisogno di un task asincrono
     * */
    private void getNotes(final int requestCode){
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>>{

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase
                        .getNotesDatabase(getApplicationContext())
                        .noteDao().getAllNotes();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);

                /**
                 * stampiamo le note con Log
                 * usato per debug
                 * */

                //Log.d("MY_NOTES_LOG_TAG", notes.toString());

                /**
                 * Cosa fa la porzione di codice successiva?
                 * controlliamo se la lista delle note è vuota, se è vuota
                 * vuol dire che òa app è appena stata avviata, ed in questo caso aggiungiamo
                 * tutte le note dalla lista dal database, e notifichiamo all'adapter
                 * che il dataset è cambiato.
                 * se invece la lista non è vuota, significa che le note sono già state caricate
                 * dal database quindi aggiungiamo solo l'ultima nota alla lista, e notifichiamo
                 * all'adapter che solo una nota è stata inserita.
                 * Quando questo accade, scrolliamo manualmente la view al primo elemento.
                 * */

                /*
                // se la lista è vuota aggiungiamo tutte le note
                // old
                if (noteList.size() == 0){
                    noteList.addAll(notes);
                    //notifichiamo che i dati sono cambiati
                    notesAdapter.notifyDataSetChanged();
                }else {
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemChanged(0);
                }

                notesRecyclerView.smoothScrollToPosition(0);
                */

                if (requestCode == REQUEST_CODE_SHOW_NOTES){
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                }else if (requestCode == REQUEST_CODE_ADD_NOTE){
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemChanged(0);
                }else if (requestCode == REQUEST_CODE_UPDATE_NOTE){
                    noteList.remove(noteClickedPosition);
                    noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                    notesAdapter.notifyItemChanged(noteClickedPosition);
                }

                notesRecyclerView.smoothScrollToPosition(0);
            }
        }

        // eseguiamo il task
        new GetNotesTask().execute();
    }

    /**
     * questo metodo si occupa di catchare un evento di Intent di risposta,
     * lo usiamo per aggiornare la lista quando aggiungiamo una
     * nuova nota
     *
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * se il codice di richiesta è per aggiungere una nota,
         * e la risposta è ok, allora...
         *
         * La risposta RESULT_OK viene aggiunta all'intent nel metodo
         * onPostExecute chiamato in CreateNoteActivity
         * */
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){
            /**
             * Il metodo getNotes viene eseguito anche all'avvio,
             * nulla ci vieta di usarlo per aggiornare
             * la lista!
             * */
            // in questo metodo vogliamo vedere la nuova nota
            getNotes(REQUEST_CODE_ADD_NOTE);
        }else if(requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK){
            if (data != null){
                getNotes(REQUEST_CODE_UPDATE_NOTE);
            }
        }
    }


}