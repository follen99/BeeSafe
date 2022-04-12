package com.example.notetakingappadvanced.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Delete;

import com.example.notetakingappadvanced.R;
import com.example.notetakingappadvanced.database.NotesDatabase;
import com.example.notetakingappadvanced.entities.Note;
import com.example.notetakingappadvanced.listeners.NotesListener;

import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>{

    private List<Note> notes;

    private NotesListener notesListener;
    Context context;

    public NotesAdapter(List<Note> notes, NotesListener notesListener, Context context) {
        this.notes = notes;
        this.notesListener = notesListener;
        this.context = context;
    }

    // ################################ METODI DA IMPLEMENTARE PER RECYCLERVIEW ################################
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ritorniamo un NoteViewHolder (classe sotto)
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setNote(notes.get(position));

        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            // usiamo il listener android per invocare il nostro
            @Override
            public void onClick(View view) {
                notesListener.onNoteClicked(notes.get(position), position, view);
            }
        });

        holder.layoutNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                notesListener.onNoteLongPressed(notes.get(position), position, view);
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    // ################################ METODI DA IMPLEMENTARE PER RECYCLERVIEW ################################


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle;
        TextView textSubtitle;
        TextView textDateTime;

        LinearLayout layoutNote;


        NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            // troviamo gli elementi visivi della view della singola nota
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);

            layoutNote = itemView.findViewById(R.id.layoutNote); // usato per mostrare il colore
        }

        // metodo di util usato per visualizzare la nota
        void setNote(Note note){
            textTitle.setText(note.getTitle());

            if (note.getSubtitle().trim().isEmpty())
                textSubtitle.setVisibility(View.GONE);
            else
                textSubtitle.setText(note.getSubtitle());

            textDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else {
                gradientDrawable.setColor(Color.parseColor("#333333")); // default
            }
        }
    }

}
