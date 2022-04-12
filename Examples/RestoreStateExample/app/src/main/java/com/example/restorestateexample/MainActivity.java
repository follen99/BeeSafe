package com.example.restorestateexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    SharedViewModel model;
    int activity_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        TextView message = findViewById(R.id.textview_act);
        TextView message2 = findViewById(R.id.textview_act2);


        model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.get().observe(this, item -> {
            message.setText("Count: "+item.count);
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Warn user
                Toast.makeText(getBaseContext(),"Increased counter!",Toast.LENGTH_SHORT);

                // update ModelView
                model.get().setValue(new Item(model.get().getValue().count+1));

                // update Local count
                activity_count+=1;
                message2.setText("Activity Count: "+activity_count);


                // force recreate
                if (model.get().getValue().count % 10 == 0){
                    System.out.println("Recreating");
                    recreate();
                }
            }
        });
    }
}