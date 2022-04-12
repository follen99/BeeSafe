package com.example.notetakingappadvanced.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notetakingappadvanced.R;
import com.example.notetakingappadvanced.database.NotesDatabase;
import com.example.notetakingappadvanced.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {
    private EditText inputNoteTitle;    // il titolo della nota
    private EditText inputNoteSubtitle; // il sottotitolo della nota
    private EditText inputNoteText;     // il corpo della nota - nota stessa

    private TextView textDateTime;      // data di creazione

    // roba per il colore
    private String selectedNoteColor;
    private View viewSubtitleIndicator;


    // roba per l'update
    private  Note alreadyAvailableNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView imageBack = findViewById(R.id.imageBack);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * linkiamo il button all'azione back della superclasse
                 * */
                onBackPressed();
            }
        });


        /**################################## LETTURA CAMPI PER SALVARE NOTA ##################################*/
        this.inputNoteTitle = findViewById(R.id.inputNoteTitle);
        this.inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        this.inputNoteText = findViewById(R.id.inputNote);
        this.textDateTime = findViewById(R.id.textDateTime);

        // cambiamo il colore dell'indicatore
        this.viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);


        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(new Date())
        );

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(new View.OnClickListener() {

            /**
             * eseguito quando l'utente clicca sull'immagine per salvare la nota
             * */
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        // default, roba per il colore
        selectedNoteColor = "#333333";


        // roba per l'update della nota
        if (getIntent().getBooleanExtra("isViewOrUpdate", false)){
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            // chiamiamo il metodo qui sotto
            setViewOrUpdateNote();
        }



        /**################################## INIZIALIZZAZIONE COLOR PICKER CARD ##################################*/
        // essenzialmente vengono impostati dei listener
        initMiscellaneous();
        setSubtitleIndicatorColor();

    }

    private void setViewOrUpdateNote(){
        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteText.setText(alreadyAvailableNote.getNoteText());
        inputNoteSubtitle.setText(alreadyAvailableNote.getSubtitle());
        textDateTime.setText(alreadyAvailableNote.getDateTime());

        // roba di immagine, da vedere dopo
        // roba di link, da vedere dopo



    }


    private void  saveNote(){
        if (editTextValidator(inputNoteTitle)){
            Toast.makeText(this, "Il titolo non può essere vuoto.", Toast.LENGTH_SHORT).show();
            return;
        }else if (editTextValidator(inputNoteSubtitle)){
            Toast.makeText(this, "Il sottotitolo non può essere vuoto.", Toast.LENGTH_SHORT).show();
            return;
        }else if (editTextValidator(inputNoteText)){
            Toast.makeText(this, "La nota non può essere vuota.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Note note = new Note();
        note.setTitle(inputNoteTitle.getText().toString());
        note.setSubtitle(inputNoteSubtitle.getText().toString());
        note.setNoteText(inputNoteText.getText().toString());
        note.setDateTime(textDateTime.getText().toString());    // qui potevamo ricreare la data, ma la data di creazione potrebbe essere diversa
        note.setColor(selectedNoteColor);

        /**
         * Room non permette operazioni sul db sul thread principale,
         * per questo motivo dobbiamo effettuare l'operazione in maniera asincrona (task)
         * */

        // update stuff
        if (alreadyAvailableNote != null){
            note.setId(alreadyAvailableNote.getId());
        }


        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void>{

            /**
             * azione da eseguire in background
             * */
            @Override
            protected Void doInBackground(Void... voids) {
                //la nota è visibile direttamente perchè abbiamo una inner class
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            /**
             * Quando l'azione in bg viene eseguita,
             * un evento chiama questo metodo
             * */
            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        // salviamo la nota con la classe precedentemente creata
        new SaveNoteTask().execute();
    }

    /**
     * metodo usato per aprire la card del color picking
     * */
    private void initMiscellaneous(){
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {

            /**
             * quando l'utente clicca sulla card in basso e questa è chiusa,
             * viene passata allo stato expanded
             *
             *
             * */
            @Override
            public void onClick(View view) {
                Log.d("CLICK_SHEET", String.valueOf(bottomSheetBehavior.getState()));
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    // altrimenti viene chiusa
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);

        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor = "#333333";  // colore di default
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);

                setSubtitleIndicatorColor();    // aggiorno
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor = "#FDBE3B";  // secondo
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);

                setSubtitleIndicatorColor();    // aggiorno
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor = "#FF4842";  // terzo
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);

                setSubtitleIndicatorColor();    // aggiorno
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor = "#3a52fC";  // quarto
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);

                setSubtitleIndicatorColor();    // aggiorno
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor = "#FF000000";  // quinto
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);

                setSubtitleIndicatorColor();    // aggiorno
            }
        });

        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !(alreadyAvailableNote.getColor().trim().isEmpty())){
            switch (alreadyAvailableNote.getColor()){
                // cambiamo il colore manualmente
                case "#FDBE3B":
                    layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#FF4842":
                    layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#3a52fC":
                    layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                    break;
                case "FF000000":
                    layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                    break;
            }
        }
    }


    private boolean editTextValidator(EditText toValidate){
        return toValidate.getText().toString().trim().isEmpty();
    }



    private void setSubtitleIndicatorColor(){
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }




}