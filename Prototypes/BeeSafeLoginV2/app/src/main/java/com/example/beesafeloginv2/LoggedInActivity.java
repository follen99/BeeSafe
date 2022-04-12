package com.example.beesafeloginv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beesafeloginv2.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoggedInActivity extends AppCompatActivity {
    private TextView email;
    private TextView fullName;
    private TextView password;
    private TextView age;

    private Button logOutButton;

    private Button adminInfo;


    private FirebaseUser currentUser;
    private DatabaseReference reference;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null){
            Toast.makeText(this, "Non sei loggato! Esegui il login", Toast.LENGTH_SHORT).show();
            goBackHome();
        }

        initViews();

        // riferimento al database users
        reference = FirebaseDatabase.getInstance().getReference("users");

        //id dell'utente corrente per trovare i suoi dati nel DB
        userID = currentUser.getUid();


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User myUser = snapshot.getValue(User.class);

                if (myUser != null){
                    boolean flag = false;

                    if (myUser.getRole().toString().equalsIgnoreCase("adm")) flag = true;
                    updateData(myUser.getFullName(), myUser.getAge(), myUser.getEmail(), myUser.getPassword(), flag);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoggedInActivity.this, "Qualcosa è andato storto!", Toast.LENGTH_SHORT).show();
            }
        });


        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                goBackHome();
            }
        });
    }



    private void updateData(String fullName, String age, String email, String password, boolean isAdmin){
        this.email.setText("Email: " + email);
        this.password.setText("Password: " + password);
        this.fullName.setText("Nome completo: " + fullName);
        this.age.setText("Età: " + age);

        if (isAdmin)
            this.adminInfo.setVisibility(View.VISIBLE);
        else this.adminInfo.setVisibility(View.GONE);
    }

    private void initViews(){
        email = findViewById(R.id.loggedin_email);
        password = findViewById(R.id.loggedin_password);
        age = findViewById(R.id.loggedin_age);
        fullName = findViewById(R.id.loggedin_full_name);
        logOutButton = findViewById(R.id.log_out_button);

        adminInfo = findViewById(R.id.admin_info);
    }

    private void goBackHome(){
        Intent intent = new Intent(LoggedInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}