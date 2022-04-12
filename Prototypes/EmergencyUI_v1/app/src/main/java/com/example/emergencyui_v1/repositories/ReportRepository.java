package com.example.emergencyui_v1.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.emergencyui_v1.Report;
import com.example.emergencyui_v1.dao.ReportDao;
import com.example.emergencyui_v1.database.ReportDatabase;

import java.util.List;

public class ReportRepository {

    private ReportDao dao;
    private LiveData<List<Report>> allReports;

    public ReportRepository(Application application){
        // istanza singleton del db
        ReportDatabase database = ReportDatabase.getInstance(application);

        /**
         * solitamente non possiamo chiamare metodi astratti, visto che non hanno un body
         * ma siccome room si occupa dell'implementazione, possiamo
         * */
        dao = database.reportDao();

        /**
         * preleviamo tutti i report dal db locale
         * */
        allReports = dao.getAllReports();
    }

    public void insert(Report report){
        new InsertReportAsyncTask(dao).execute(report);
    }

    public void update(Report report){
        new UpdateReportAsyncTask(dao).execute(report);
    }

    public void delete(Report report){
        new DeleteReportAsyncTask(dao).execute(report);
    }

    public void deleteAllCollections(){
        new DeleteAllReportAsyncTask(dao).execute();
    }

    public LiveData<List<Report>> getAllImageCollections() {
        return allReports;
    }

    /**
     * La classe va posta statica in modo che non abbia reference alla repository
     * questo potrebbe causare un memory leak
     *
     * ARGOMENTI DI ASYNCTASK
     * ImageCollection -> passiamo il nostro oggetto
     * Void -> non abbiamo bisogno di aggiornamenti di progresso
     * Void -> non abbiamo bisogno di un ritorno
     *
     *
     * Abbiamo una classe asynctask per ogni operazione. questo potrebbe essere evitato
     * ma per il momento non implemento un meccanismo troppo complicato, keep it simple
     */
    private static class InsertReportAsyncTask extends AsyncTask<Report, Void, Void> {
        // abbiamo bisogno del dao per eseguire delle operazioni sul db
        private ReportDao dao;
        private InsertReportAsyncTask(ReportDao dao){
            this.dao = dao;
        }

        /**
         * siccome passiamo un singolo report preleviamo il primo
         * */
        @Override
        protected Void doInBackground(Report... reports) {
            dao.insert(reports[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

    private static class UpdateReportAsyncTask extends AsyncTask<Report, Void, Void> {
        // abbiamo bisogno del dao per eseguire delle operazioni sul db
        private ReportDao dao;
        private UpdateReportAsyncTask(ReportDao dao){
            this.dao = dao;
        }

        /**
         * siccome passiamo un singolo report preleviamo il primo
         * */
        @Override
        protected Void doInBackground(Report... reports) {
            dao.update(reports[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

    private static class DeleteReportAsyncTask extends AsyncTask<Report, Void, Void> {
        // abbiamo bisogno del dao per eseguire delle operazioni sul db
        private ReportDao dao;
        private DeleteReportAsyncTask(ReportDao dao){
            this.dao = dao;
        }

        /**
         * siccome passiamo un singolo report preleviamo il primo
         * */
        @Override
        protected Void doInBackground(Report... reports) {
            dao.delete(reports[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

    private static class DeleteAllReportAsyncTask extends AsyncTask<Void, Void, Void> {
        // abbiamo bisogno del dao per eseguire delle operazioni sul db
        private ReportDao dao;
        private DeleteAllReportAsyncTask(ReportDao dao){
            this.dao = dao;
        }

        /**
         * siccome passiamo un singolo report preleviamo il primo
         * */
        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }
}
