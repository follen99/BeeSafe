package com.example.notetakingappadvanced.listeners;

import android.view.View;

import com.example.notetakingappadvanced.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position, View view);
    void onNoteLongPressed(Note note, int position, View view);
}
