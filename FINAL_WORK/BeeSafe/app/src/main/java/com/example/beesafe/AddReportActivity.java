package com.example.beesafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beesafe.model.Image;
import com.example.beesafe.model.Report;
import com.example.beesafe.model.ReportBuilder;
import com.example.beesafe.utils.Constants;
import com.example.beesafe.utils.DigestHelper;
import com.example.beesafe.utils.FirebaseUserHelper;
import com.example.beesafe.utils.LocalDatabaseHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class AddReportActivity extends AppCompatActivity implements LocationListener, View.OnClickListener{
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private static final int REQUEST_CODE_PHONE_PERMISSION = 3;
    private static final int REQUEST_CODE_LOCATION = 4;
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText inputPlaceName;
    private EditText inputReportDescription;

    private SeekBar inputPrioritySeekBar;
    private int lastChangeGravity;

    private SeekBar inputUrgencySeekBar;
    private int lastChangeUrgency;

    private Spinner inputKindOfProblemSpinner;

    private TextView priorityTextview;
    private TextView urgencyTextView;

    private LinearLayout addImagebutton;

    private RoundedImageView selectedImageView;
    //private String selectedImagePath;

    private ImageView saveReport;
    private ImageView backButton;

    private TextView dateTime;

    private boolean isPhonePermissionGranted = false;
    private boolean isLocationPermissionGranted = false;

    private Uri mImageUri;
    private String remoteImageUrl;

    // database
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private StorageTask uploadTask;

    private ProgressBar saveProgressBar;

    // manager per la posizione live
    private LocationManager locationManager;

    private final OkHttpClient client = new OkHttpClient();

    FusedLocationProviderClient fusedLocationProviderClient;
    Location lastKnownLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        remoteImageUrl = null;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // get location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkLocationPermission()){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,500.0f, this);
        }

        findViews();

        storageReference = FirebaseStorage.getInstance().getReference("images");
        databaseReference = FirebaseDatabase.getInstance().getReference("images");
        mAuth = FirebaseAuth.getInstance();


        addImagebutton.setOnClickListener(this);

        backButton.setOnClickListener(this);


        //selectedImagePath = "";

        prioritySeekBarSettings();
        urgencySeekBarSettings();

        saveReport.setOnClickListener(this);
    }

    /**
     * usato per gestire i click sugli items
     * @param view la view che ha generato il click
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_image_button:
                openFileChooser();
                break;
            case R.id.imageBack:
                onBackPressed();
                break;
            case R.id.imageSave:
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            AddReportActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION
                    );
                    return;
                } else isLocationPermissionGranted = true;

                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_PHONE_NUMBERS
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            AddReportActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_NUMBERS},
                            REQUEST_CODE_PHONE_PERMISSION
                    );
                    //return;
                } else isPhonePermissionGranted = true;
                // non è necessario che si conceda il telefono

                break;

        }
    }


    /**
     * usato per inviare una HTTP request di tipo POST
     * questa richiesta viene usata per salvare il report sul database MongoDB
     * @param report il report da salvare
     * @param localDatabaseHelper l'helper che salva il report in locale
     */
    private void sendPostRequest(Report report, LocalDatabaseHelper localDatabaseHelper){
        Gson gson = new Gson();
        String jsonData = gson.toJson(report);

        MediaType mt = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mt, jsonData);

        // helper usato per ricevere un'istanza di firebase user, non è necessario in questa classe, potevo usare anche la variabile mAuth
        FirebaseUserHelper currentUserHelper = FirebaseUserHelper.getInstance();
        FirebaseUser user = currentUserHelper.getCurrentFirebaseUser();


        /**
         * Richiesta effettuata usando la libreria OkHttp
         * Semantica più semplice, ma non può eseguire Toast nel thread secondario.
         * */
        assert user != null;
        FirebaseUserHelper helper = FirebaseUserHelper.getInstance();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://" + Constants.REST_HOSTNAME + ":" + Constants.REST_PORT + "/reports")
                //.addHeader("uid", "qCou2nf9I8UcEsju9yAMBDu8Ihv2")
                //.addHeader("digest", "C3AA0E70ACB552D9E7FC504BA6618A6E")
                .addHeader("uid", user.getUid())
                .addHeader("digest", DigestHelper.getDigestFromCurrentUser())
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddReportActivity.this, "Connessione al server fallita.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveProgressBar.setVisibility(View.GONE);
                        saveReport.setVisibility(View.VISIBLE);
                        Toast.makeText(AddReportActivity.this, "Report salvato.", Toast.LENGTH_SHORT).show();
                    }
                });
                localDatabaseHelper.saveReportAsync(report);
            }
        });
    }

    /**
     * usato per caricare l'immagine su firebase
     *
     * solo quando l'immagine è stata caricata con successo su firebase,
     * il report viene salvato.
     */
    private void uploadFile(){
        // se l'immagine è stata scelta...
        if (mImageUri != null){
            StorageReference fileReference = storageReference.child(mAuth.getCurrentUser().getEmail().replace(".", "-")).child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            //Toast.makeText(AddReportActivity.this, "Caricamento completato.", Toast.LENGTH_SHORT).show();


                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Image image = new Image(System.currentTimeMillis(),
                                            uri.toString());

                                    remoteImageUrl = uri.toString();
                                    saveReport();

                                    String uploadId = databaseReference.push().getKey();
                                    databaseReference.child(mAuth.getCurrentUser().getEmail().replace(".", "-")).child(uploadId).setValue(image);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddReportActivity.this, "Errore nel caricamento", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            //Toast.makeText(this, "Nessuna immagine è stata selezionata.", Toast.LENGTH_SHORT).show();
            saveReport();
            return;
        }
    }

    /**
     * usato per aprire la view che seleziona l'immagine
     */
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Questo metodo ritorna l'estensione del file passato come uri
     * @param uri l'uri del file
     * @return l'estensione del file
     */
    private String getFileExtension(Uri uri){
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    /**
     * usato per salvare il report sia nel database locale che su MongoDB grazie ad una richiesta HTTP
     */
    private void saveReport() {

        if (editTextValidator(inputPlaceName)) {
            //Toast.makeText(this, "Il posto non può essere vuoto!", Toast.LENGTH_SHORT).show();
            inputPlaceName.setError("Il nome del posto non può essere vuoto!");
            inputPlaceName.requestFocus();
            return;
        }


        if (checkLocationPermission()){
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d("reportbuilder", "position: " + lastKnownLocation.getLatitude()+"");
        } else {
            Toast.makeText(this, "Per continuare devi consentire la localizzazione!", Toast.LENGTH_LONG).show();
            return;
        }



        String description = inputReportDescription.getText().toString();
        if (description == null || description.matches(""))
            description = "Nessuna descrizione";

        Log.d("reportbuilder", "description: " + description);

        String imagePaht = null;
        if (remoteImageUrl != null)
            imagePaht = remoteImageUrl;

        Log.d("reportbuilder", "image path: " + imagePaht);

        long phoneNumber = 0L;
        if (isPhonePermissionGranted){
            TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = null;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//            Toast.makeText(this, "E' richiesto l'accesso al telefono.", Toast.LENGTH_SHORT).show();
                return;
            }
            mPhoneNumber = tMgr.getLine1Number();
            if (mPhoneNumber != null) {
                phoneNumber = Long.parseLong(mPhoneNumber);
                Log.d("reportbuilder", "numero telefono: " + mPhoneNumber);
            }
        }



        final Report report = new ReportBuilder()
                .setPlaceName(inputPlaceName.getText().toString())
                .setDescription(description)
                .setGravity(lastChangeGravity)
                .setUrgency(lastChangeUrgency)
                .setImagePath(imagePaht)
                .setKindOfProblem(inputKindOfProblemSpinner.getSelectedItem().toString())
                .setDateTime(dateTime.getText().toString())
                .setLatitude(lastKnownLocation.getLatitude())
                .setLongitude(lastKnownLocation.getLongitude())
                .setFromUser(mAuth.getCurrentUser().getEmail())
                .setId(calcReportId(mAuth.getCurrentUser().getEmail()))
                .setPhoneNumber(phoneNumber)

                .createReport();

        Log.d("reportbuilder", report.toString());



        LocalDatabaseHelper helper = new LocalDatabaseHelper(this);

        // salvo nel db locale ed in quello remoto
        //helper.saveReportAsync(report);
        sendPostRequest(report, helper);

        finish();
    }

    /**
     * usato per calcolare l'id del report a partire dall'oggetto report
     * @param report il report
     * @return l'id calcolato
     */
    @Deprecated
    private int calcReportId(Report report){
        return (int) (System.currentTimeMillis()+report.getFromUser().hashCode());
    }

    /**
     * usato per calcolare l'id del report a partire dall'email
     * @param userEmail l'email del report
     * @return l'id
     */
    private int calcReportId(String userEmail){
        return (int) (System.currentTimeMillis()+userEmail.hashCode());
    }

    /**
     * usato per vedere se il permesso al telefono è stato concesso
     * @return booleano che dice se il telefono è stato concesso
     */
    @Deprecated
    private boolean checkPhonePermission(){
        // se il permesso è già stato concesso
        if (ContextCompat.checkSelfPermission(AddReportActivity.this,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            // permesso concesso
            return true;
        }
        return false;
    }

    /**
     * usato per vedere se l'utente ha dato il permesso alla posizione
     * @return booleano che dice se la posizione è stata concessa
     */
    private boolean checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(AddReportActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }


    /**
     * usato per validare un oggetto EditText
     * @param toValidate l'edittext da validare
     * @return booleano che ci dice se il campo è valido
     */
    private boolean editTextValidator(EditText toValidate){
        return toValidate.getText().toString().trim().isEmpty();
    }

    /**
     * usato per inizializzare e gestire gli eventi della seekbar per la priorità
     */
    private void prioritySeekBarSettings(){
        inputPrioritySeekBar.setMin(1);
        inputPrioritySeekBar.setMax(10);
        inputPrioritySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                lastChangeGravity = progress;
                priorityTextview.setText("Priorità: " + lastChangeGravity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * usato per inizializzare e gestire gli eventi della seekbar per l'urgenza
     */
    private void urgencySeekBarSettings(){
        inputUrgencySeekBar.setMin(1);
        inputUrgencySeekBar.setMax(10);
        inputUrgencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                lastChangeUrgency = progress;
                urgencyTextView.setText("Urgenza: " + lastChangeUrgency);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    /**
     * usato per inizializzare le views dal file xml
     */
    private void findViews(){
        inputPlaceName = findViewById(R.id.input_report_title);
        inputReportDescription = findViewById(R.id.input_report_body);
        inputPrioritySeekBar = findViewById(R.id.input_priority_seekbar);
        inputUrgencySeekBar = findViewById(R.id.input_urgency_seekbar);
        inputKindOfProblemSpinner = findViewById(R.id.input_kind_spinner);

        priorityTextview = findViewById(R.id.input_priority_text);
        urgencyTextView = findViewById(R.id.input_urgency_text);

        addImagebutton = findViewById(R.id.add_image_button);
        selectedImageView = findViewById(R.id.picked_image);

        saveReport = findViewById(R.id.imageSave);
        backButton = findViewById(R.id.imageBack);

        dateTime = findViewById(R.id.textDateTime);

        dateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        lastChangeGravity = 1;
        lastChangeUrgency = 1;

        saveProgressBar = findViewById(R.id.save_progress_bar);
    }

    /**
     * usato per avviare un intent implicito che seleziona un'immagine dalla memoria
     */
    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    /**
     * invocato quando è disponibile il risultato di un'activity invocata
     * in questo caso l'activity è avviata tramite un intent implicito volto a
     * selezionare l'immagine dalla memoria che viene poi caricata
     * @param requestCode   il codice di richiesta
     * @param resultCode    il codice risultato
     * @param data          i dati recuperati
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.with(this)
                    .load(mImageUri)
                    .into(selectedImageView);
            selectedImageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * invocato quando è disponibile un risultato inerente ai permessi
     * @param requestCode il codice della richiesta
     * @param permissions i permessi
     * @param grantResults i risultati
     *
     * viene chiesto all'utente se vuole fornire l'accesso alla galleria;
     * se risponde di si, selezioniamo l'immagine,
     * altrimenti comunichiamo che l'accesso è stato negato.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // se l'user ha concesso l'accesso allo storage
                selectImage();
            } else {
                Toast.makeText(this, "Accesso non consentito.", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUEST_CODE_PHONE_PERMISSION ){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // se l'user ha concesso l'accesso al telefono
                Toast.makeText(this, "Permesso al telefono concesso.", Toast.LENGTH_SHORT).show();
                isPhonePermissionGranted = true;
            } else {
                //Toast.makeText(this, "Devi concedere l'accesso al telefono per aggiungere segnalazioni.", Toast.LENGTH_SHORT).show();
                isPhonePermissionGranted = false;
            }

            if (isLocationPermissionGranted){
                if (uploadTask == null || !uploadTask.isInProgress()) {
                    saveReport.setVisibility(View.GONE);
                    saveProgressBar.setVisibility(View.VISIBLE);
                    uploadFile();
                } else
                    Toast.makeText(AddReportActivity.this, "C'è già un caricamento in corso...", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == REQUEST_CODE_LOCATION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permesso alla posizione concesso.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Devi concedere l'accesso alla posizione per aggiungere segnalazioni.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * restituisce il percorso dell'immagine selezionata
     * @param imageUri la URI dell'immagine
     * @return il path dell'immagine
     */
    @Deprecated
    private String getPathFromUri(Uri imageUri){
        String filePath;

        Cursor cursor = getContentResolver()
                .query(imageUri, null, null, null, null);

        if (cursor == null){
            filePath = imageUri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }

        return filePath;
    }

    /**
     * invocato quando il dispositivo si sposta
     * @param location la posizione geografica
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        lastKnownLocation = location;
        //Log.d("location", "latitude: " + lastKnownLocation.getLatitude() + "\nLongitude: " + lastKnownLocation.getLongitude());
    }


}