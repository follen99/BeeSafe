package com.example.imagecollectionmvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import java.io.InputStream;
import java.util.Currency;
import java.util.Objects;

public class AddImageCollectionActivity extends AppCompatActivity {

    private EditText inputTitle;
    private EditText inputDescription;

    private LinearLayout add_image_button;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private ImageView pickedImage;
    private String selectedImagePath;

    private SeekBar prioritySeekBar;
    private int lastProgress;
    private TextView priorityText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_collection);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_save_24);
        setTitle("Aggiungi collezione di immagini");

        this.inputTitle = findViewById(R.id.input_title);

        this.inputDescription = findViewById(R.id.input_description);

        // immagine selezionata dall'user
        this.pickedImage = findViewById(R.id.picked_image);


        this.add_image_button = findViewById(R.id.add_image_button);
        add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddImageCollectionActivity.this, "Adding image...", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            AddImageCollectionActivity.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });

        selectedImagePath = "";

        prioritySeekBar = findViewById(R.id.input_priority);
        prioritySeekBar.setMin(1);
        prioritySeekBar.setMax(10);

        priorityText = findViewById(R.id.priority_text);
        prioritySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                lastProgress = progress;
                priorityText.setText("Priorità: " + lastProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(AddImageCollectionActivity.this, "Changed to: " + lastProgress, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // salva al database

        saveImageCollection();

        return true;
    }

    private void saveImageCollection(){

        if (editTextValidator(inputTitle)){
            Toast.makeText(this, "Il titolo non può essere vuoto.", Toast.LENGTH_SHORT).show();
            return;
        }else if (editTextValidator(inputDescription)){
            Toast.makeText(this, "La descrizione non può essere vuota", Toast.LENGTH_SHORT).show();
            return;
        }

        final  ImageCollection collection = new ImageCollection();
        collection.setName(inputTitle.getText().toString());
        collection.setDescription(inputDescription.getText().toString());
        collection.setImagePath(selectedImagePath);
        collection.setPriority(lastProgress);


        class SaveImageCollectionAsyncTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                ImageCollectionDatabase.getInstance(getApplicationContext()).imageCollectionDao().insert(collection);
                return null;
            }

            /**
             * Quando l'azione in bg viene eseguita,
             * un evento chiama questo metodo
             * */
            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                Toast.makeText(AddImageCollectionActivity.this, "Collezione salvata con successo!", Toast.LENGTH_SHORT).show();

                // chiudo la view e torno alla home; refresh
                finish();
            }
        }

        // salvo con la classe async su un altro thread
        new SaveImageCollectionAsyncTask().execute();
    }

    private boolean editTextValidator(EditText toValidate){
        return toValidate.getText().toString().trim().isEmpty();
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

                        pickedImage.setImageBitmap(bitmap);
                        pickedImage.setVisibility(View.VISIBLE);


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
                selectImage();
            } else {
                Toast.makeText(this, "Accesso non consentito.", Toast.LENGTH_SHORT).show();
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