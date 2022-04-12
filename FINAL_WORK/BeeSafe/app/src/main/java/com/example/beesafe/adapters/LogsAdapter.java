package com.example.beesafe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beesafe.R;
import com.example.beesafe.model.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogHolder> {
    protected List<Log> allLogs = new ArrayList<>();
    private Context context;

    public LogsAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public LogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_expanded_item,
                        parent,
                        false);
        return new LogHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LogHolder holder, int position) {
        Log currentLog = allLogs.get(position);

        holder.logAuthor.setText(currentLog.getUser());
        //holder.dateTime.setText(currentLog.getTimestamp() + "");
        Date currentLogDate = new Date(currentLog.getTimestamp());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        holder.dateTime.setText(simpleDateFormat.format(currentLogDate));

        holder.requestType.setText("Richiesta: " + currentLog.getRequestType());
        holder.requestResult.setText("Risultato: " + currentLog.getOutcome());
        holder.requestOperation.setText("Operazione: " + currentLog.getOperation());



    }

    @Override
    public int getItemCount() {
        return allLogs.size();
    }


    class LogHolder extends RecyclerView.ViewHolder{
        private TextView logAuthor;
        private TextView dateTime;
        private TextView requestType;
        private TextView requestResult;
        private TextView requestOperation;


        public LogHolder(@NonNull View itemView) {
            super(itemView);

            logAuthor = itemView.findViewById(R.id.logs_expanded_from_user);
            dateTime = itemView.findViewById(R.id.logs_expanded_date_time);
            requestType = itemView.findViewById(R.id.logs_request_type);
            requestResult = itemView.findViewById(R.id.logs_request_result);
            requestOperation = itemView.findViewById(R.id.logs_request_operation);
        }
    }

    public void setLogsList(List<Log> logsList){
        this.allLogs = logsList;
        notifyDataSetChanged();
    }
}
