package com.example.beesafe.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beesafe.R;
import com.example.beesafe.listeners.ReportListener;
import com.example.beesafe.model.Report;
import com.example.beesafe.utils.Constants;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportHolder>{
    // è importante inizializzare la lista prima dell'istanziamento della classe
    protected List<Report> allReports = new ArrayList<>();
    private Context context;
    private ReportListener reportListener;

    private boolean isImageHiding;



    public ReportAdapter(Context context, ReportListener listener){
        this.context = context;
        this.reportListener = listener;
        this.isImageHiding = true;
    }

    @NonNull
    @Override
    public ReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, parent, false);

        return new ReportHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportHolder holder, int position) {
        Report curretReport = allReports.get(position);

        holder.textViewPlace.setText(curretReport.getPlaceName());
        holder.textViewDateTime.setText(curretReport.getDateTime());
        holder.textViewKindOfProblem.setText(curretReport.getKindOfProblem());



        if (holder.selectedImage != null){
            /*BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(curretReport.getImagePath(), options);

            holder.selectedImage.setImageBitmap(bitmap);
            holder.selectedImage.setVisibility(View.VISIBLE);*/

            // se l'immagine è in locale la prelevo dalla memoria
            if (curretReport.getImagePath() != null){
                if (!curretReport.getImagePath().startsWith("http")){
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(curretReport.getImagePath(), options);

                    holder.selectedImage.setImageBitmap(bitmap);
                    holder.selectedImage.setVisibility(View.VISIBLE);
                }
                // altrimenti, se è in remoto la prelevo da internet con un task asincrono
                else{
                    hideImage(curretReport, holder);

                    holder.selectedImage.setVisibility(View.VISIBLE);
                }

                holder.selectedImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.progressBar.setVisibility(View.VISIBLE);
                        holder.selectedImage.setVisibility(View.GONE);
                        if ( curretReport.getImagePath()!= null){
                            Log.d("images", curretReport.getImagePath());
                            if (isImageHiding){
                                new DownloadImageTask(holder.selectedImage, holder)
                                        .execute(curretReport.getImagePath());
                                isImageHiding = false;
                            } else{
                                hideImage(curretReport, holder);
                                isImageHiding = true;
                            }
                        }
                    }
                });
            } else {
                holder.selectedImage.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
            }


        } else {
            holder.selectedImage.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
        }

        int gravity = curretReport.getGravity();

        if (gravity <= 5 || gravity > 10){
            holder.gravitySymbol.setImageResource(R.drawable.ic_baseline_priority_low_24);
        } else if (gravity <= 8){
            holder.gravitySymbol.setImageResource(R.drawable.ic_baseline_priority_medium_24);
        } else {
            holder.gravitySymbol.setImageResource(R.drawable.ic_baseline_priority_high_24);
        }

        /**
         * inviamo delle callback quando avviene un click sulla view principale di ogni report
         *
         * la callback viene gestita nell'activity main
         */
        holder.reportMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportListener.onReportClicked(curretReport, position, view);
            }
        });
        holder.reportMainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                reportListener.onReportLongClicked(curretReport, position, view);
                return true;
            }
        });

        Log.d("item", curretReport.toString());
    }

    private void hideImage(Report curretReport, ReportHolder holder){
        if (curretReport.getKindOfProblem().equalsIgnoreCase(Constants.FILTER_INCIDENTE_STRADALE)){
            new DownloadImageTask(holder.selectedImage, holder)
                    .execute(Constants.CAR_ACCIDENT_BLURRED_IMAGE_SHORT);
        } else if (curretReport.getKindOfProblem().equalsIgnoreCase(Constants.FILTER_INCENDIO)){
            new DownloadImageTask(holder.selectedImage, holder)
                    .execute(Constants.FIRE_BLURRED_IMAGE_SHORT);
        } else if (curretReport.getKindOfProblem().equalsIgnoreCase(Constants.FILTER_SALUTE)){
            new DownloadImageTask(holder.selectedImage, holder)
                    .execute(Constants.HEALTH_BLURRED_IMAGE_SHORT);
        } else if (curretReport.getKindOfProblem().equalsIgnoreCase(Constants.FILTER_FURTO)){
            new DownloadImageTask(holder.selectedImage, holder)
                    .execute(Constants.THIEF_BLURRED_IMAGE_SHORT);
        } else if (curretReport.getKindOfProblem().equalsIgnoreCase(Constants.FILTER_VIOLENZA)){
            new DownloadImageTask(holder.selectedImage, holder)
                    .execute(Constants.VIOLENCE_BLURRED_IMAGE_SHORT);
        } else{
            new DownloadImageTask(holder.selectedImage, holder)
                    .execute(curretReport.getImagePath());
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        ReportHolder holder;

        public DownloadImageTask(ImageView bmImage, ReportHolder holder) {
            this.bmImage = bmImage;
            this.holder = holder;
        }

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

        protected void onPostExecute(Bitmap result) {
            holder.progressBar.setVisibility(View.GONE);
            bmImage.setImageBitmap(result);
            bmImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return allReports.size();
    }

    public void setReportList(List<Report> reports){
        this.allReports = reports;
        notifyDataSetChanged();
    }

    class ReportHolder extends RecyclerView.ViewHolder{
        private TextView textViewPlace;
        private TextView textViewKindOfProblem;
        private RoundedImageView selectedImage;
        private TextView textViewDateTime;

        private ImageView gravitySymbol;

        private ProgressBar progressBar;

        private LinearLayout reportMainLayout;

        // aggiungere l'immagine della priorità


        public ReportHolder(@NonNull View itemView) {
            super(itemView);

            textViewPlace = itemView.findViewById(R.id.report_place_name);
            textViewKindOfProblem = itemView.findViewById(R.id.report_kind_of_problem);
            textViewDateTime = itemView.findViewById(R.id.report_datetime);
            selectedImage = itemView.findViewById(R.id.image_from_item);

            gravitySymbol = itemView.findViewById(R.id.priority_symbol);

            progressBar = itemView.findViewById(R.id.loading_image_progress_bar);

            reportMainLayout = itemView.findViewById(R.id.layout_report_item);
        }
    }
}
