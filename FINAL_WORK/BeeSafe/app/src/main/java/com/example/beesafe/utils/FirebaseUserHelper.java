package com.example.beesafe.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.beesafe.LoginActivity;
import com.example.beesafe.MainActivity;
import com.example.beesafe.listeners.UserReferenceListener;
import com.example.beesafe.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class FirebaseUserHelper {
    private final FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;

    private static FirebaseUserHelper instance;

    private User currentUser;
    private FirebaseUser currentFirebaseUser;

    private UserReferenceListener userListener;

    private FirebaseUserHelper(){
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentFirebaseUser = auth.getCurrentUser();


        reference = database.getReference("/users/" + auth.getCurrentUser().getUid()) ;



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                Log.d("retr-user1", currentUser.toString());

                userListener.onUserReady();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println(error.getMessage());
                Log.d("retr-user", error.getMessage());
            }
        });



    }

    /**
     * usato per recuperare l'utente trovato dal database firebase
     * @return l'utente
     */
    public User getUserReference(){
        return currentUser;
    }

    /**
     * usato per recuperare l'utente trovato dall'auth firebase
     * @return l'utente
     */
    public FirebaseUser getCurrentFirebaseUser(){
        return currentFirebaseUser;
    }

    /**
     * usato per recuperare l'istanza di firebase helper
     * @return l'istanza di helper
     */
    public static FirebaseUserHelper getInstance(){
        if (instance == null){
            instance = new FirebaseUserHelper();
            return instance;
        }
        return instance;
    }

    /**
     * @// TODO: 03/03/2022 questo metodo è molto pericoloso, andrà cambiato con una callback
     */
    public static void destroySingleton(){
        instance = null;
    }

    /**
     * usato per impostare il listener
     * @param listener il listener da impostare
     */
    public void setUserListener(UserReferenceListener listener){
        userListener = listener;
    }

}
