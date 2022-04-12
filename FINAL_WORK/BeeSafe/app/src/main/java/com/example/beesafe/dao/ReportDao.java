package com.example.beesafe.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.beesafe.model.Report;

import java.util.List;

@Dao
public interface ReportDao {
    @Insert
    public void insert(Report report);

    @Update
    public void update(Report report);

    @Delete
    public void delete(Report report);

    @Query("DELETE FROM report")
    public void deleteAll();

    @Query("SELECT* FROM report ORDER BY urgency DESC")
    public LiveData<List<Report>> getAllReports();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllReports(List<Report> reports);
}
