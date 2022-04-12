package com.example.cardview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView location;
    private boolean toggle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.location = (TextView) findViewById(R.id.location);
        this.location.setText("test");
    }

    public void updateText(View view) {
        this.location = (TextView) findViewById(R.id.location);

        if (toggle){
            this.location.setText("prova");
            toggle=!toggle;
            return;
        }else this.location.setText("test");
        toggle=!toggle;
    }
}