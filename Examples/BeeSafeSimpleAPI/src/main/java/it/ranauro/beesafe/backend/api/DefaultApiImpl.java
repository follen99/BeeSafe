package it.ranauro.beesafe.backend.api;

import it.ranauro.beesafe.backend.control.MongoManager;
import it.ranauro.beesafe.backend.control.ShowFirebaseDbChanges;
import it.ranauro.beesafe.backend.model.Report;

import it.ranauro.beesafe.backend.ApiResponse;

import io.vertx.core.Future;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import org.bson.Document;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Implement this class

public class DefaultApiImpl implements DefaultApi {

    public Future<ApiResponse<Void>> addReport(Report report) {
        try {
            MongoManager manager = MongoManager.getInstance();
            manager.addReport(report);

            ApiResponse<Void> response = new ApiResponse<>();

            return Future.succeededFuture(response);
        } catch (FileNotFoundException e) {
            return Future.failedFuture(new HttpStatusException(500));
        }
    }

    public Future<ApiResponse<List<Report>>> filterReportsByGravity() {
        try {
            MongoManager manager = MongoManager.getInstance();

            ApiResponse<List<Report>> response = new ApiResponse<>(manager.orderByGravity());
            return Future.succeededFuture(response);
        } catch (FileNotFoundException e) {
            return Future.failedFuture(new HttpStatusException(500));
        }
    }

    public Future<ApiResponse<List<Report>>> filterReportsByUrgency() {
        try {
            MongoManager manager = MongoManager.getInstance();

            ApiResponse<List<Report>> response = new ApiResponse<>(manager.orderByUrgency());
            return Future.succeededFuture(response);
        } catch (FileNotFoundException e) {
            return Future.failedFuture(new HttpStatusException(500));
        }
    }

    public Future<ApiResponse<List<Report>>> findReportByKindOfProblem(String kindOfProblem) {
        try {
            MongoManager manager = MongoManager.getInstance();

            ApiResponse<List<Report>> response = new ApiResponse<>(manager.filterByKindOfProblem(kindOfProblem));
            return Future.succeededFuture(response);
        } catch (FileNotFoundException e) {
            return Future.failedFuture(new HttpStatusException(500));
        }
    }

    public Future<ApiResponse<List<Report>>> findReports() {
        try {
            MongoManager manager = MongoManager.getInstance();

            ApiResponse<List<Report>> response = new ApiResponse<>(manager.findAll());

            // l'UID verrà passato tramite richiesta HTTP
            String uid = "qCou2nf9I8UcEsju9yAMBDu8Ihv2";
            Thread thread = new Thread(new ShowFirebaseDbChanges(uid));
            thread.start();

            return Future.succeededFuture(response);
        } catch (FileNotFoundException e) {
            return Future.failedFuture(new HttpStatusException(500));
        }
    }

}
