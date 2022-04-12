package com.example.imagecollectionmvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class ImageCollectionViewModel extends AndroidViewModel {
    private ImageCollectionRepository repository;
    private LiveData<List<ImageCollection>> allImageCollections;

    /**
     * NElla androidviewmodel ci viene passato Application nel costruttore,
     * che possiamo usare per ricavaere il context quando ne abbiamo bisogno
     * */
    public ImageCollectionViewModel(@NonNull Application application) {
        super(application);

        repository = new ImageCollectionRepository(application);
        allImageCollections = repository.getAllImageCollections();

    }

    public void insert(ImageCollection collection){
        repository.insert(collection);
    }

    public void update(ImageCollection collection){
        repository.update(collection);
    }

    public void delete(ImageCollection collection){
        repository.delete(collection);
    }

    public void deleteAllCollections(){
        repository.deleteAllCollections();
    }

    public LiveData<List<ImageCollection>> getAllImageCollections(){
        return allImageCollections;
    }
}
