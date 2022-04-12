package com.example.imagecollectionmvvm;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ImageCollectionRepository {

    private ImageCollectionDao dao;
    private LiveData<List<ImageCollection>> allImageCollections;

    /**
     * possiamo usare Application per creare la nostra istanza di db
     * */
    public ImageCollectionRepository(Application application){
        ImageCollectionDatabase database = ImageCollectionDatabase.getInstance(application);

        /**
         * solitamente non possiamo chiamare metodi astratti, visto che non hanno un body
         * ma siccome room si occupa dell'implementazione, possiamo
         * */
        dao = database.imageCollectionDao();

        /**
         * preleviamo tutte le collezioni di immagini
         * */
        allImageCollections = dao.getAllImageCollections();

    }

    public void insert(ImageCollection imageCollection){
        new InsertImageCollectionAsyncTask(dao).execute(imageCollection);
    }

    public void update(ImageCollection imageCollection){
        new UpdateImageCollectionAsyncTask(dao).execute(imageCollection);
    }

    public void delete(ImageCollection imageCollection){
        new DeleteImageCollectionAsyncTask(dao).execute(imageCollection);
    }

    public void deleteAllCollections(){
        new DeleteAlltImageCollectionAsyncTask(dao).execute();
    }

    public LiveData<List<ImageCollection>> getAllImageCollections() {
        return allImageCollections;
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
    private static class InsertImageCollectionAsyncTask extends AsyncTask<ImageCollection, Void, Void>{
        // abbiamo bisogno del dao per eseguire delle operazioni sul db
        private ImageCollectionDao dao;
        private InsertImageCollectionAsyncTask(ImageCollectionDao dao){
            this.dao = dao;
        }

        /**
         * siccome passiamo una singola Imagecollection accediamo al primo elemento
         * */
        @Override
        protected Void doInBackground(ImageCollection... imageCollections) {
            dao.insert(imageCollections[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

    private static class UpdateImageCollectionAsyncTask extends AsyncTask<ImageCollection, Void, Void>{
        // abbiamo bisogno del dao per eseguire delle operazioni sul db
        private ImageCollectionDao dao;
        private UpdateImageCollectionAsyncTask(ImageCollectionDao dao){
            this.dao = dao;
        }

        /**
         * siccome passiamo una singola Imagecollection accediamo al primo elemento
         * */
        @Override
        protected Void doInBackground(ImageCollection... imageCollections) {
            dao.update(imageCollections[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

    private static class DeleteImageCollectionAsyncTask extends AsyncTask<ImageCollection, Void, Void>{
        // abbiamo bisogno del dao per eseguire delle operazioni sul db
        private ImageCollectionDao dao;
        private DeleteImageCollectionAsyncTask(ImageCollectionDao dao){
            this.dao = dao;
        }

        /**
         * siccome passiamo una singola Imagecollection accediamo al primo elemento
         * */
        @Override
        protected Void doInBackground(ImageCollection... imageCollections) {
            dao.delete(imageCollections[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

    private static class DeleteAlltImageCollectionAsyncTask extends AsyncTask<Void, Void, Void>{
        // abbiamo bisogno del dao per eseguire delle operazioni sul db
        private ImageCollectionDao dao;
        private DeleteAlltImageCollectionAsyncTask(ImageCollectionDao dao){
            this.dao = dao;
        }



        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }


    }
}
