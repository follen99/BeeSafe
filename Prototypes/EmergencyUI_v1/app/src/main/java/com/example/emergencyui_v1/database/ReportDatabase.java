package com.example.emergencyui_v1.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.emergencyui_v1.Report;
import com.example.emergencyui_v1.dao.ReportDao;

@Database(entities = {Report.class}, version = 1)
public abstract class ReportDatabase extends RoomDatabase {
    /**
     * Creiamo questa variabile perchè dobbiamo trasformare questa classe in un singleton
     * */
    private static ReportDatabase reportDatabaseInstance;

    /**
     * usiamo questo metodo per accedere al nostro dao
     * è astratto perchè non forniamo un body, room si occupa del code
     * */
    public abstract ReportDao reportDao();

    public static synchronized ReportDatabase getInstance(Context context){
        if (reportDatabaseInstance == null){
            reportDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    ReportDatabase.class,
                    "report_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return reportDatabaseInstance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // popolazione del database successivamente
        }
    };


    /**
     * un AsyncTask per popolare il database
     * */
    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private ReportDao dao;
        // prendiamo un'istanza del database per popolarlo
        private PopulateDBAsyncTask(ReportDatabase database){
            dao = database.reportDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // inserisci nel database qui
            //dao.insert();
            return null;
        }
    }
}
