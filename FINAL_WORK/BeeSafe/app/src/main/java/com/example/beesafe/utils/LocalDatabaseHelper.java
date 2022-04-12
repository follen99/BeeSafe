package com.example.beesafe.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.beesafe.database.ReportDatabase;
import com.example.beesafe.model.Report;

import java.util.List;

public class LocalDatabaseHelper {
    private Context context;

    /**
     * costruttore
     * @param context il context del chiamante, usato per visualizzare Toast
     */
    public LocalDatabaseHelper(Context context) {
        this.context = context;
    }

    /**
     * usato per salvare un singolo report
     * @param report il report da salvare
     */
    public void saveReportAsync(Report report){
        /**
         * classe usata per eseguire del codice su un thread secondario
         */
        class SaveReportAsyncTask extends AsyncTask<Report, Void, Void>{

            /**
             * salva un report nel database
             * @param reports il report
             * @return
             */
            @Override
            protected Void doInBackground(Report... reports) {
                ReportDatabase.getInstance(context.getApplicationContext())
                        .reportDao().insert(report);
                return null;
            }

            /**
             * eseguito dopo quando il metodo run ha completato
             * @param unused void
             */
            @Override
            protected void onPostExecute(Void unused) {
                //Toast.makeText(context, "Report salvato.", Toast.LENGTH_SHORT).show();
            }
        }
        new SaveReportAsyncTask().execute();
    }

    /**
     * usato per salvare i reports
     * @param reports i reports da salvare
     */
    public void saveReportAsync(List<Report> reports){
        /**
         * classe usata per eseguire del codice su un thread secondario
         */
        class SaveReportsAsynctask extends AsyncTask<List<Report>, Void, Void>{

            /**
             * metodo eseguito in background
             * @param lists i reports
             * @return void
             */
            @Override
            protected Void doInBackground(List<Report>... lists) {
                ReportDatabase.getInstance(context.getApplicationContext())
                        .reportDao().insertAllReports(reports);
                return null;
            }

            /**
             * eseguito dopo quando il metodo run ha completato
             * @param unused void
             */
            @Override
            protected void onPostExecute(Void unused) {
                //Toast.makeText(context, "Reports salvati.", Toast.LENGTH_SHORT).show();
            }
        }
        new SaveReportsAsynctask().execute();
    }

    @Deprecated
    public void getAllReports(){ }
}
