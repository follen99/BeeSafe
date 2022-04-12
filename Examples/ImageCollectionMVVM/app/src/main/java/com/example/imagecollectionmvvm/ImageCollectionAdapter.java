package com.example.imagecollectionmvvm;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagecollectionmvvm.listeners.ImageCollectionListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageCollectionAdapter extends RecyclerView.Adapter<ImageCollectionAdapter.ImageCollectionHolder> {
    // è importante inizializzare la lista prima dell'istanziamento della classe
    protected List<ImageCollection> imageCollectionList = new ArrayList<>();
    private ImageCollectionListener listener;

    public ImageCollectionAdapter(ImageCollectionListener listener) {
        this.listener = listener;
    }


    /**
     * questo è il metodo dove dobbiamo creare il viewholder
     * */
    @NonNull
    @Override
    public ImageCollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imagecollection_item, parent, false);

        return new ImageCollectionHolder(itemView, listener);
    }

    /**
     * qui ci preoccupiamo di  prendere i dati dal singolo oggetto java
     * all'interno di ImageCollectionHolder
     * */
    @Override
    public void onBindViewHolder(@NonNull ImageCollectionHolder holder, int position) {


        ImageCollection currentCollection = imageCollectionList.get(position);
        holder.textViewTitle.setText(currentCollection.getName());
        holder.textViewDescription.setText(currentCollection.getDescription());

        holder.priority.setText(String.valueOf(currentCollection.getPriority()));

        //holder.priority.setText(currentCollection.getPriority());

        // accediamo all'elemento UI tramite l'holder.
        // se l'utente ha specificato un'immagine la usiamo
        // altrimenti nascondiamo l'elemento UI
        if (holder.selectedImage != null){
            holder.selectedImage.setImageBitmap(BitmapFactory.decodeFile(currentCollection.getImagePath()));
            holder.selectedImage.setVisibility(View.VISIBLE);
        }else {
            holder.selectedImage.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return imageCollectionList.size();
    }

    public void setImageCollectionList(List<ImageCollection> collectionList){
        this.imageCollectionList = collectionList;
        notifyDataSetChanged();
    }

    class ImageCollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView textViewTitle;
        private TextView textViewDescription;

        private ImageCollectionListener listener;

        private RoundedImageView selectedImage;

        private TextView priority;



        /**
         * nel costruttore possiamo assegnare i nostri oggetti alla view
         * */
        public ImageCollectionHolder(@NonNull View itemView, ImageCollectionListener listener) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.imagecollection_name);
            textViewDescription = itemView.findViewById(R.id.imagecollection_description);

            selectedImage = itemView.findViewById(R.id.imagecollection_image);

            priority = itemView.findViewById(R.id.priority_show);


            this.listener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClicked(getAdapterPosition(), view, imageCollectionList.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onItemLongPressed(getAdapterPosition(), view,  imageCollectionList.get(getAdapterPosition()));
            return true;
        }
    }
}
