package com.example.beesafe.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.beesafe.model.Report;
import com.example.beesafe.repositories.ReportRepository;

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
        allReports = repository.getAllReports();
    }

    /**
     * usato per inserire un report
     * @param report il report da inserire
     */
    public void insert(Report report){
        repository.insert(report);
    }

    /**
     * usato per aggiornare un report
     * @param report il report da aggiornare
     */
    @Deprecated
    public void update(Report report){
        repository.update(report);
    }

    /**
     * usato per eliminare un report
     * @param report il report da eliminare
     */
    @Deprecated
    public void delete(Report report){
        repository.delete(report);
    }

    /**
     * usato per eliminare tutti i reports
     */
    @Deprecated
    public void deleteAllReports(){
        repository.deleteAllReports();
    }

    /**
     * usato per recuperare tutti i reports
     * @return tutti i reports
     */
    public LiveData<List<Report>> getAllReports(){
        return allReports;
    }
}
