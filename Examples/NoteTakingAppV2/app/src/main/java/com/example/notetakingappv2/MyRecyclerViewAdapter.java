package com.example.notetakingappv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    Context context;
    RealmResults<Note> noteList;

    public MyRecyclerViewAdapter(Context context, RealmResults<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.note_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.titleOutput.setText(note.getTitle());
        holder.descriptionOutput.setText(note.getDescription());

        String formattedData = DateFormat.getDateTimeInstance().format(note.creationTimeStamp);
        holder.timeOutput.setText(formattedData);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu deleteMenu = new PopupMenu(context, view);
                deleteMenu.getMenu().add("ELIMINA");
                deleteMenu.getMenu().add("MODIFICA");

                deleteMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("ELIMINA")){
                            // eliminiamo la nota
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();

                            note.deleteFromRealm();
                            realm.commitTransaction();

                            Toast.makeText(context, "Nota eliminata!", Toast.LENGTH_SHORT).show();

                        }else if (menuItem.getTitle().equals("MODIFICA")){

                        }

                        return true;
                    }
                });

                deleteMenu.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView titleOutput;
        TextView descriptionOutput;
        TextView timeOutput;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOutput = itemView.findViewById(R.id.title_output);
            descriptionOutput = itemView.findViewById(R.id.description_output);
            timeOutput = itemView.findViewById(R.id.date_output);


        }
    }
}
