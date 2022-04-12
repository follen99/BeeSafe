package it.ranauro.beesafe.backend.control;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.bson.Document;

import java.io.IOException;

public class ShowFirebaseDbChanges implements Runnable{
    private String uid;
    private DatabaseReference reference;
    FirebaseService service;

    public ShowFirebaseDbChanges(String uid){
        this.uid = uid;
        this.service = null;
    }


    @Override
    public void run() {

        if(service == null){
            try {
                service = FirebaseService.getService();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        /**
         * Path di base: /users
         * Questo path lista TUTTI gli users. A noi non serve questo, ma cercare uno specifico user;
         * di conseguenza passiamo alla classe un Uid dell'utente da cercare, e costruiamo
         * il path del DB in modo da avere
         * /users/Uid_user
         * */
        reference = service.getDatabase()
                .getReference("/users/" + uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Object document = dataSnapshot.getValue();
                    System.out.println(document);


                } else  {
                    // user not found
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error: " + databaseError.getMessage());
            }
        });

    }
}
