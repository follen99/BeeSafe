 package com.example.beesafeloginfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.beesafeloginfirebase.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

 public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{
     private EditText inputName;
     private EditText inputAge;
     private EditText inputEmail;
     private EditText inputPassword;

     private Button confirmButton;
     private ImageView backButton;
     private Button showPasswordButton;

     private ProgressBar progressBar;

     private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // nascondo l'action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // inizializzo le views e le trovo
        initViews();

        mAuth = FirebaseAuth.getInstance();

        confirmButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        showPasswordButton.setOnClickListener(this);


    }


     private void initViews(){
         this.inputAge = findViewById(R.id.input_age);
         this.inputName = findViewById(R.id.input_name);
         this.inputEmail = findViewById(R.id.input_email);
         this.inputPassword = findViewById(R.id.input_password);

         this.confirmButton = findViewById(R.id.confirm_button);
         this.backButton = findViewById(R.id.back_button);

         this.showPasswordButton = findViewById(R.id.show_password_button);

         this.progressBar = findViewById(R.id.progress_bar);
     }

     @SuppressLint("NonConstantResourceId")
     @Override
     public void onClick(View view) {
         //handle registration
         switch (view.getId()){
             case R.id.back_button:
                 onBackPressed();
                 break;
             case R.id.confirm_button:
                 registerUser();
                 break;
             case R.id.show_password_button:
                 if (showPasswordButton.getText().toString().equalsIgnoreCase("mostra")){
                     //inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                     inputPassword.setTransformationMethod(new PasswordTransformationMethod());
                     showPasswordButton.setText("nascondi");
                 } else {
                     inputPassword.setTransformationMethod(null);
                     showPasswordButton.setText("mostra");
                 }
         }
     }


     private void registerUser(){
        String email = inputEmail.getText().toString().trim();
        String name = inputName.getText().toString().trim();
        String age = inputAge.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // validiamo i campi

         if (name.isEmpty()){
             inputName.setError("Questo campo non pu?? essere vuoto!");
             inputName.requestFocus();
             return;
         }

         if (age.isEmpty()){
             inputAge.setError("Questo campo non pu?? essere vuoto!");
             inputAge.requestFocus();
             return;
         }

         // check email
         if (email.isEmpty()){
             inputEmail.setError("L'email non pu?? essere vuota!");
             inputEmail.requestFocus();
             return;
         }
         if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
             inputEmail.setError("L'email non ?? valida.");
             inputEmail.requestFocus();
             return;
         }

         //check password
         if (password.isEmpty()){
             inputPassword.setError("La password non pu?? essere vuota!");
             inputPassword.requestFocus();
         }

         if (password.length() < 6){
             inputPassword.setError("La password ?? troppo corta!\nLunghezza minima: 6 caratteri.");
             inputPassword.requestFocus();
         }

         // mostriamo all'user che stiamo caricando
         progressBar.setVisibility(View.VISIBLE);



         mAuth.createUserWithEmailAndPassword(email, password)
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()){

                             Toast.makeText(RegisterUserActivity.this, "Utente registrato correttamente.", Toast.LENGTH_SHORT).show();
                             progressBar.setVisibility(View.GONE);

                             /**
                              * Torno alla home
                              * Ma ATTENZIONE! se volessi tornare alla home questa activity deve poter essere invocata
                              * solo dalla home page.
                              * */
                             onBackPressed();

                         } else {
                             Toast.makeText(RegisterUserActivity.this, "Registrazione fallita. Riprova pi?? tardi.", Toast.LENGTH_SHORT).show();
                             progressBar.setVisibility(View.GONE);
                         }
                     }
                 });

     }


 }