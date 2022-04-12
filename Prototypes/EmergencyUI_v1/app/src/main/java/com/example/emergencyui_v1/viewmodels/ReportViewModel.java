package com.example.emergencyui_v1.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.emergencyui_v1.Report;
import com.example.emergencyui_v1.repositories.ReportRepository;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {
    private ReportRepository repository;
    private LiveData<List<Report>> allReports;

    /**
     * NElla androidviewmodel ci viene passato Application nel costruttore,
     * che possiamo usare per ricavaere il context quando ne abbiamo bisogno
     * */
    public ReportViewModel(@NonNull Application application) {
        super(application);

        repository = new ReportRepository(application);
        allReports = repository.getAllImageCollections();
    }

    public void insert(Report report){
        repository.insert(report);
    }

    public void update(Report report){
        repository.update(report);
    }

    public void delete(Report report){
        repository.delete(report);
    }

    public void deleteAllReports(){
        repository.deleteAllCollections();
    }

    public LiveData<List<Report>> getAllReports(){
        return allReports;
    }
}
