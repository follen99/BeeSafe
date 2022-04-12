package com.example.imagecollectionmvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FocusViewActivity extends AppCompatActivity {
    TextView titleView;
    TextView descriptionView;
    TextView priorityView;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_view);

        titleView = findViewById(R.id.focus_title);
        descriptionView = findViewById(R.id.focus_description);
        priorityView = findViewById(R.id.focus_priority);
        imageView = findViewById(R.id.focus_image);


        // ci viene passato l'intero oggetto come extra
        ImageCollection collection = getIntent().getParcelableExtra("collection");
        titleView.setText(collection.getName());
        descriptionView.setText(collection.getDescription());
        priorityView.setText("Priorità: " + collection.getPriority());

        if (collection.getImagePath() != null){
            imageView.setImageBitmap(BitmapFactory.decodeFile(collection.getImagePath()));
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.GONE);
        }
    }
}