package com.example.firebasestoredataexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User newUser = new User("luigino@gmail.com", "ADM", "passwordLuigino99", "Luigino il grande");


                // qui al posto della chiave va messo FirebaseAuth.getInstance().getCurrentUser().getUid()
                reference.child(String.valueOf(newUser.getEmail().hashCode())).setValue(newUser)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Utente salvato.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Errore inaspettato", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    class User{
        private String email;
        private String role;
        private String password;
        private String fullName;

        public User(String email, String role, String password, String fullName) {
            this.email = email;
            this.role = role;
            this.password = password;
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        @Override
        public String toString() {
            return "User{" +
                    "email='" + email + '\'' +
                    ", role='" + role + '\'' +
                    ", password='" + password + '\'' +
                    ", fullName='" + fullName + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return email.equals(user.email) && role.equals(user.role) && password.equals(user.password) && fullName.equals(user.fullName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(email, role, password, fullName);
        }
    }
}