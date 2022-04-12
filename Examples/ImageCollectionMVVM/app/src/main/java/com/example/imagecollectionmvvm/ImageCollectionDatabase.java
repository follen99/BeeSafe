package com.example.imagecollectionmvvm;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {ImageCollection.class}, version = 2)
public abstract class ImageCollectionDatabase extends RoomDatabase {

    /**
     * Creiamo questa variabile perchè dobbiamo trasformare questa classe in un singleton
     * */
    private static ImageCollectionDatabase instance;

    /**
     * usiamo questo metodo per accedere al nostro dao
     * è astratto perchè non forniamo un body, room si occupa del code
     * */
    public abstract ImageCollectionDao imageCollectionDao();

    // altro singleton
    // un solo thread può accedere all'oggetto nello stesso momento
    public static synchronized ImageCollectionDatabase getInstance(Context context){

        /**
         *
         * */
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ImageCollectionDatabase.class,
                    "image_collection_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback) // quando eseguiamo onCreate aggiungiamo la callback per popolare il database
                    .build(); //quando incrementiamo la versione del db dobbiamo dire a room come migrare al nuovo schema

        }
        /**
         * se l'istanza è già stata istanziata ritorniamo quella, altrimenti la creiamo
         * */
        return instance;
    }


    // popoliamo il database "a manella"
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // popoliamo il db con un asynctask
            new PopulateDBAsyncTask(instance).execute();
        }
    };


    /**
     * un AsyncTask per popolare il database
     * */
    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void>{
        private ImageCollectionDao dao;
        // prendiamo un'istanza del database per popolarlo
        private PopulateDBAsyncTask(ImageCollectionDatabase database){
            dao = database.imageCollectionDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            dao.insert(new ImageCollection("Titolo 1", "path/to/image/1",thisIsALongString,1));
            dao.insert(new ImageCollection("Titolo 2", "path/to/image/2","Description 2",2));
            dao.insert(new ImageCollection("Titolo 3", "path/to/image/3","Description 3", 3));


            return null;
        }

        String thisIsALongString = "ciao come va\n" +
                "oggi tutto bene aooo\n" +
                "io me ne vaodo\n" +
                "ciao ciao\n" +
                "fesso";
    }


    /*
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `image_collection` (`id` INTEGER, "
                    + "`name` TEXT," +
                    "`image_path`, TEXT," +
                    "`description`, TEXT," +
                    "`priority`, INTEGER," +
                    " PRIMARY KEY(`id`))");
        }
    };*/

}
