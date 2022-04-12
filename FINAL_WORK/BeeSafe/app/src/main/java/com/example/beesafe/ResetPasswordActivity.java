package com.example.beesafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText emailText;
    private Button confirmButton;
    private ProgressBar progressBar;
    private ImageView backButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        auth = FirebaseAuth.getInstance();


        emailText = findViewById(R.id.reset_password_email);
        confirmButton = findViewById(R.id.reset_password_confirm_button);
        progressBar = findViewById(R.id.reset_password_progress_bar);
        backButton = findViewById(R.id.reset_password_back_button);

        confirmButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }


    private void resetPassword() {
        String email = emailText.getText().toString().trim();
        if (email.isEmpty()){
            emailText.setError("L'email non può essere vuota.");
            emailText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailText.setError("L'email fornita non è valida.");
            emailText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ResetPasswordActivity.this, "Il link di reset è stato inviato all'email fornita.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "L'email fornita non è corretta.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    /**
     * usato per gestire i tocchi sullo schermo
     * @param view la view che è stata toccata
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reset_password_confirm_button:
                resetPassword();
                break;
            case R.id.reset_password_back_button:
                onBackPressed();
                break;
        }
    }
}