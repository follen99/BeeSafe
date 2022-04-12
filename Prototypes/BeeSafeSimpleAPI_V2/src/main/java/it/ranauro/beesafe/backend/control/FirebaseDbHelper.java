package it.ranauro.beesafe.backend.control;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import it.ranauro.beesafe.backend.model.Log;
import it.ranauro.beesafe.backend.model.User;
import it.ranauro.beesafe.backend.utils.Constants;
import it.ranauro.beesafe.backend.utils.Operation;
import it.ranauro.beesafe.backend.utils.Outcome;
import it.ranauro.beesafe.backend.utils.RequestType;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Classe di comodo usata per verificare cambiamenti al database Firebase
 */
public class FirebaseDbHelper {
    private String uid;
    private DatabaseReference reference;
    private DatabaseReference logsReference;
    FirebaseService service;

    private boolean isUserValid;

    private User retrievedUser = null;




    /**
     * Costruttore della classe
     * @param uid   id dell'utente che ha effettuato la richiesta
     */
    public FirebaseDbHelper(String uid){
        this.uid = uid;
        this.service = null;
        isUserValid = false;
    }

    /**
     * Usato per recuperare uno specifico utente dal database firebase
     */
    public void getUserFromFirebaseDB() {

        if(service == null){
            try {
                service = FirebaseService.getService();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        CountDownLatch done = new CountDownLatch(1);



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


                    retrievedUser = dataSnapshot.getValue(User.class);
                    //System.out.println(retievedUser.toString());
                    done.countDown();
                    if(retrievedUser != null)
                        isUserValid = true;
                    else isUserValid = false;


                } else  {
                    isUserValid = false;
                    // user not found
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error: " + databaseError.getMessage());
            }
        });

        try {
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Usato per salvare un log nel database firebase
     * @param requestType   il tipo di richiesta effettuata
     * @param operation     il tipo di operazione effettuata
     * @param outcome       il risultato dell'operazione
     */
    public void saveLog(RequestType requestType, Operation operation, Outcome outcome){
        String email = uid;
        boolean isAdm = false;
        long currentTimeStamp = System.currentTimeMillis();
        String path = "failedAttempts";

        if (isUserValid){
            if(retrievedUser.getRole().equalsIgnoreCase("ADM")) isAdm = true;
            email = retrievedUser.getEmail();
            path = retrievedUser.getEmail().substring(0, retrievedUser.getEmail().indexOf('@'));
        }

        Log log = new Log(currentTimeStamp, email, requestType, operation, outcome, isAdm);
        System.out.println(log);

        if (service == null){
            try {
                service = FirebaseService.getService();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logsReference = service.getDatabase()
                .getReference("/logs");
        logsReference.child(path)
                .child(currentTimeStamp+"")
                .setValueAsync(log);
    }


    /**
     * Usato per ritornare l'utente trovato
     * @return l'utente trovato
     */
    public User getRetievedUser(){
        return retrievedUser;
    }

    public boolean isUserValid(){
        return isUserValid;
    }

    /**
     * Questo metodo è usato per calcolare il digest lato server.
     * Utilizza un segreto comune, che concatenato alla password recuperata dall'utente firebase,
     * verrà confrontata con quella inviata dal client.
     * Se i due digest corrispondono, l'utente è autenticato (ma non per forza autorizzato)
     * @return  il digest
     */
    public String calcRemoteDigest(){
        String password = retrievedUser.getPassword();
        String md5Hex = DigestUtils.md5Hex( password + Constants.SECRET).toUpperCase();
        //System.out.println("Digest: " + md5Hex);
        return md5Hex;
    }

    /**
     * ritorna il ruolo dell'utente
     * 0 = USR
     * 1 = ADM
     * */
    @Deprecated
    public boolean getUserRole(){
        return false;
    }
}
