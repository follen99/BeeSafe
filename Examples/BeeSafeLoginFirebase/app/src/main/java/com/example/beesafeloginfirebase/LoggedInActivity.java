package com.example.beesafeloginfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beesafeloginfirebase.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInActivity extends AppCompatActivity {
    private TextView email;
    private Button logOutButton;


    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(this, "Non sei loggato! Esegui il login", Toast.LENGTH_SHORT).show();
            goBackHome();
        }
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        initViews();

        email.setText(currentUser.getEmail());

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                goBackHome();
            }
        });
    }

    private void initViews(){
        email = findViewById(R.id.logged_in_email);
        logOutButton = findViewById(R.id.log_out_button);
    }

    private void goBackHome(){
        Intent intent = new Intent(LoggedInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}