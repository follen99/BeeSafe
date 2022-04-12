package com.example.imagecollectionmvvm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;;

import java.util.List;

@Dao
public interface ImageCollectionDao {

    @Insert
    public void insert(ImageCollection imageCollection);

    @Update
    public void update(ImageCollection imageCollection);

    @Delete
    public void delete(ImageCollection imageCollection);

    @Query("DELETE FROM image_collection")
    public void deleteAll();

    @Query("SELECT* FROM image_collection ORDER BY priority DESC")
    public LiveData<List<ImageCollection>> getAllImageCollections();
}
