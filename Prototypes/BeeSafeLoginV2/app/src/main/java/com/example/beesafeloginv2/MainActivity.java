package com.example.beesafeloginv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beesafeloginv2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText emailInput;
    private EditText passwordInput;

    private Button showPasswordButton;
    private Button loginButton;

    private TextView forgotPasswordButton;
    private TextView registerButton;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            //l'utente è già loggato, si può aprire la home page
            startActivity(new Intent(MainActivity.this, LoggedInActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        initViews();



        showPasswordButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
    }

    private void initViews(){
        this.emailInput = findViewById(R.id.login_email);
        this.passwordInput = findViewById(R.id.login_password);

        this.showPasswordButton = findViewById(R.id.login_show_password);
        this.loginButton = findViewById(R.id.login_button);

        this.forgotPasswordButton = findViewById(R.id.forgot_button);
        this.registerButton = findViewById(R.id.register_button);

        this.progressBar = findViewById(R.id.login_progress_bar);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_show_password:
                if (showPasswordButton.getText().toString().equalsIgnoreCase("mostra")){
                    //inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    passwordInput.setTransformationMethod(new PasswordTransformationMethod());
                    showPasswordButton.setText("nascondi");
                } else {
                    passwordInput.setTransformationMethod(null);
                    showPasswordButton.setText("mostra");
                }
                break;
            case R.id.login_button:
                // login user
                signinUser();
                break;
            case R.id.forgot_button:
                // send email
                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.register_button:
                // registra l'utente
                startActivity(new Intent(MainActivity.this, RegisterUserActivity.class));
                break;
        }
    }

    private void signinUser(){


        // leggo dalle views; è bene tagliare la stringa perchè l'utente potrebbe aggiungere uno spazio alla fine
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        Log.d("credentials", "Email: " + email + ", Password: " + password);


        if (validateEmail(email) && validatePassword(password)){
            this.progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                // redirect to home page
                                //Toast.makeText(MainActivity.this, "Accesso conesentito! quì verrà mostrata la home", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                                startActivity(new Intent(MainActivity.this, LoggedInActivity.class));
                                finish();
                            }else {
                                Toast.makeText(MainActivity.this, "Accesso negato! Controlla le tue credenziali.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Errore inaspettato.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }else return;



    }

    private boolean validateEmail(String email){
        if (email.isEmpty()){
            emailInput.setError("L'email non può essere vuota!");
            emailInput.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailInput.setError("L'email non è valida.");
            emailInput.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password){
        //check password
        if (password.isEmpty()){
            passwordInput.setError("La password non può essere vuota!");
            passwordInput.requestFocus();
            return false;
        }

        if (password.length() < 6){
            passwordInput.setError("La password è troppo corta!\nLunghezza minima: 6 caratteri.");
            passwordInput.requestFocus();
            return false;
        }

        return true;
    }
}