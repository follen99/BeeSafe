package com.example.emergencyui_v1.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencyui_v1.R;
import com.example.emergencyui_v1.Report;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportHolder> {

    // è importante inizializzare la lista prima dell'istanziamento della classe
    protected List<Report> allReports = new ArrayList<>();

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
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(curretReport.getImagePath(), options);

            holder.selectedImage.setImageBitmap(bitmap);
            holder.selectedImage.setVisibility(View.VISIBLE);
        } else {
            holder.selectedImage.setVisibility(View.GONE);
        }

        int gravity = curretReport.getGravity();

        if (gravity <= 5 || gravity > 10){
            holder.gravitySymbol.setImageResource(R.drawable.ic_baseline_priority_low_24);
        } else if (gravity <= 8){
            holder.gravitySymbol.setImageResource(R.drawable.ic_baseline_priority_medium_24);
        } else {
            holder.gravitySymbol.setImageResource(R.drawable.ic_baseline_priority_high_24);
        }

        Log.d("item", curretReport.toString());
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

        // aggiungere l'immagine della priorità


        public ReportHolder(@NonNull View itemView) {
            super(itemView);

            textViewPlace = itemView.findViewById(R.id.report_place_name);
            textViewKindOfProblem = itemView.findViewById(R.id.report_kind_of_problem);
            textViewDateTime = itemView.findViewById(R.id.report_datetime);
            selectedImage = itemView.findViewById(R.id.image_from_item);

            gravitySymbol = itemView.findViewById(R.id.priority_symbol);
        }
    }
}
