package com.example.emergencyui_v1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencyui_v1.database.ReportDatabase;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddReportActivity extends AppCompatActivity{
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private static final int REQUEST_CODE_PHONE_PERMISSION = 3;
    private static final int REQUEST_CODE_LOCATION = 4;

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
    private String selectedImagePath;

    private ImageView saveReport;
    private ImageView backButton;

    private TextView dateTime;

    private boolean isPhonePermissionGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        Objects.requireNonNull(getSupportActionBar()).hide();
        findViews();

        addImagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddReportActivity.this, "Aggiungo l'immagine...", Toast.LENGTH_SHORT).show();

                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            AddReportActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        selectedImagePath = "";

        prioritySeekBarSettings();
        urgencySeekBarSettings();

        saveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            AddReportActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION
                    );
                } else {
                    if (ContextCompat.checkSelfPermission(
                            getApplicationContext(), Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                AddReportActivity.this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                REQUEST_CODE_PHONE_PERMISSION
                        );
                    } else {
                        saveReport();
                    }
                }


            }
        });

    }

    private void saveReport() {
        if (editTextValidator(inputPlaceName)) {
            Toast.makeText(this, "Il posto non può essere vuoto!", Toast.LENGTH_SHORT).show();
            return;
        }

        final Report report = new Report();
        report.setPlaceName(inputPlaceName.getText().toString());
        report.setDescription(inputReportDescription.getText().toString());
        report.setGravity(lastChangeGravity);
        report.setUrgency(lastChangeUrgency);
        if (selectedImagePath != "") report.setImagePath(selectedImagePath);
        else report.setImagePath(null);
        report.setKindOfProblem(inputKindOfProblemSpinner.getSelectedItem().toString());
        report.setDateTime(dateTime.getText().toString());

        // da prendere automaticamente



        report.setLatitude(41.067215);
        report.setLongitude(14.854777);
        //if (!isPhonePermissionGranted) return;

        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "E' richiesto l'accesso al telefono.", Toast.LENGTH_SHORT).show();
            return;
        }
        String mPhoneNumber = tMgr.getLine1Number();
        long phoneNubr = Long.parseLong(mPhoneNumber);
        report.setPhoneNumber(phoneNubr);

        class SaveReportAsyncTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                ReportDatabase.getInstance(getApplicationContext())
                        .reportDao().insert(report);
                Log.d("add_report", report.toString());
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                Toast.makeText(AddReportActivity.this, "Report salvato!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        new SaveReportAsyncTask().execute();
    }



    // vediamo se i permessi di accesso alla lettura del telefono sono già stati concessi
    private boolean checkPhonePermission(){
        // se il permesso è già stato concesso
        if (ContextCompat.checkSelfPermission(AddReportActivity.this,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            // permesso concesso
            return true;
        }
        return false;
    }

    private boolean editTextValidator(EditText toValidate){
        return toValidate.getText().toString().trim().isEmpty();
    }

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
                Toast.makeText(AddReportActivity.this, "Changed to: " + lastChangeGravity, Toast.LENGTH_SHORT).show();
            }
        });
    }

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
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(AddReportActivity.this, "Changed to: " + lastChangeUrgency, Toast.LENGTH_SHORT).show();
            }
        });
    }

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
    }

    private void selectImage(){
        // usiamo un intent implicito
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if (data != null){
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null){
                    try {
                        InputStream stream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);

                        selectedImageView.setImageBitmap(bitmap);
                        selectedImageView.setVisibility(View.VISIBLE);


                        // imposto la url dell'immagine
                        selectedImagePath = getPathFromUri(selectedImageUri);
                    }catch (Exception exception){
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * viene chiesto all'utente se vuole fornire l'accesso alla galleria;
     * se risponde di si, selezioniamo l'immagine,
     * altrimenti comunichiamo che l'accesso è stato negato.
     * */
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
                Toast.makeText(this, "Permesso al telefono negato.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_LOCATION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permesso alla posizione concesso.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permesso alla posizione negato.", Toast.LENGTH_SHORT).show();
            }
        }
    }

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

}