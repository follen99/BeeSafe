package com.example.simplenavigationcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button nextPageButton;
    Button fullMapsPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        this.nextPageButton = findViewById(R.id.button_main_page);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Vado alla seconda pagina", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(MainActivity.this, SecondPage.class));
            }
        });

        this.fullMapsPageButton = findViewById(R.id.button_full_maps);
        fullMapsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Vado alla maps view", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });
    }
}