package com.example.beesafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beesafe.adapters.ReportAdapter;
import com.example.beesafe.model.Report;
import com.example.beesafe.utils.Constants;
import com.google.firebase.database.core.Repo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.InputStream;

public class ReportFocusViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_PHONE = 10;

    ImageView backButton;
    TextView placeNameText;
    TextView dateText;
    TextView descriptionText;
    TextView urgencyText;
    TextView priorityText;
    TextView kindOfProblemText;
    RoundedImageView image;
    private TextView phoneToCallText;

    LinearLayout openInMapsButton;

    private ProgressBar loadingImage;

    Report report;

    private boolean isImageHidden;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_focus_view);
        isImageHidden = true;

        report = (Report) getIntent().getSerializableExtra(Constants.FOCUS_VIEW_REPORT);

        loadingImage = findViewById(R.id.focus_progress_bar);

        backButton = findViewById(R.id.focus_back_button);
        backButton.setOnClickListener(this);

        placeNameText = findViewById(R.id.focus_report_place_name);
        placeNameText.setText(report.getPlaceName());

        dateText = findViewById(R.id.focus_date);
        dateText.setText(report.getDateTime());

        descriptionText = findViewById(R.id.focus_problem_description);
        descriptionText.setText(report.getDescription());

        urgencyText = findViewById(R.id.focus_urgency_message);
        urgencyText.setText("Urgenza: " + report.getUrgency() + "/10");

        priorityText = findViewById(R.id.focus_priority_message);
        priorityText.setText("Priorità: " + report.getGravity() + "/10");

        kindOfProblemText = findViewById(R.id.focus_kind_of_problem_message);
        kindOfProblemText.setText("Tipo di emergenza: " + report.getKindOfProblem());

        phoneToCallText = findViewById(R.id.focus_number_to_call_text);
        phoneToCallText.setText(report.getPhoneNumber()+"");
        phoneToCallText.setOnClickListener(this);

        image = findViewById(R.id.focus_image);

        if (report.getImagePath() != null){
            hideImage(report);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isImageHidden){
                        image.setVisibility(View.GONE);
                        loadingImage.setVisibility(View.VISIBLE);
                        new DownloadImageTask(image)
                                .execute(report.getImagePath());
                        isImageHidden = false;
                    } else {
                        hideImage(report);
                        isImageHidden = true;
                    }
                }
            });
        } else image.setVisibility(View.GONE);

        openInMapsButton = findViewById(R.id.open_in_maps_layout);
        openInMapsButton.setOnClickListener(this);
    }

    /**
     * usato per nascondere un'immagine
     * @param curretReport il report corrente
     */
    private void hideImage(Report curretReport){
        loadingImage.setVisibility(View.VISIBLE);
        image.setVisibility(View.GONE);
        if (curretReport.getKindOfProblem().equalsIgnoreCase(Constants.FILTER_INCIDENTE_STRADALE)){
            new DownloadImageTask(image)
                    .execute(Constants.CAR_ACCIDENT_BLURRED_IMAGE);
        } else if (curretReport.getKindOfProblem().equalsIgnoreCase(Constants.FILTER_INCENDIO)){
            new DownloadImageTask(image)
                    .execute(Constants.FIRE_BLURRED_IMAGE);
        } else if (curretReport.getKindOfProblem().equalsIgnoreCase(Constants.FILTER_SALUTE)){
            new DownloadImageTask(image)
                    .execute(Constants.HEALTH_BLURRED_IMAGE);
        } else if (curretReport.getKindOfProblem().equalsIgnoreCase(Constants.FILTER_FURTO)){
            new DownloadImageTask(image)
                    .execute(Constants.THIEF_BLURRED_IMAGE);
        } else if (curretReport.getKindOfProblem().equalsIgnoreCase(Constants.FILTER_VIOLENZA)){
            new DownloadImageTask(image)
                    .execute(Constants.VIOLENCE_BLURRED_IMAGE);
        } else{
            new DownloadImageTask(image)
                    .execute(curretReport.getImagePath());
        }
    }

    /**
     * usato per gestire i tocchi sullo schermo
     * @param view la vista che è stata toccata
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.focus_back_button:
                onBackPressed();
                break;
            case R.id.open_in_maps_layout:
                Intent mapsIntent = new Intent(ReportFocusViewActivity.this, GeneralMapsView.class);
                mapsIntent.putExtra(Constants.MAPS_REPORT_ELEMENT_NAME, report);
                mapsIntent.putExtra(Constants.MAPS_MULTIPLE_FLAG_NAME, false);
                startActivity(mapsIntent);
                break;
            case R.id.focus_number_to_call_text:

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            ReportFocusViewActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CODE_PHONE
                    );
                }else{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+report.getPhoneNumber()));
                    startActivity(callIntent);
                }


                break;
        }
    }

    /**
     * viene eseguito quando l'utente concede o rifiuta un permesso
     * @param requestCode il codice della richiesta
     * @param permissions i permessi
     * @param grantResults i risultati
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PHONE && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Ora puoi effettuare chiamate dall'app.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * classe usata per eseguire un task su un thread secondario
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        /**
         * costruttore per la classe
         * @param bmImage l'immagine da caricare
         */
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        /**
         * viene eseguito il background, corrispondente di "run()" nei java thread
         * @param urls l'url dell'immagine
         * @return il bitmap dell'immagine
         */
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        /**
         * viene eseguito quando il task è completo
         * @param result il bitmap
         */
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            image.setVisibility(View.VISIBLE);
            loadingImage.setVisibility(View.GONE);
        }
    }
}