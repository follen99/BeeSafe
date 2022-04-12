package it.ranauro.beesafe.backend.control;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class FirebaseService {

    FirebaseDatabase database;
    static FirebaseService singletonService;

    private FirebaseService() throws IOException{
        // cerco il file di autenticazione
        File file = new File(
                Objects.requireNonNull(getClass().getClassLoader().getResource("key.json")).getFile()
        );

        FileInputStream stream = new FileInputStream(file);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(stream))
                .setDatabaseUrl("https://beesafe-pc-default-rtdb.europe-west1.firebasedatabase.app")
                .build();

        FirebaseApp.initializeApp(options);

        database = FirebaseDatabase.getInstance();

    }

    public static FirebaseService getService() throws IOException {
        if (singletonService == null){
            singletonService = new FirebaseService();
        }
        return singletonService;
    }



    public FirebaseDatabase getDatabase(){
        return database;
    }
}
