package it.ranauro.beesafe.backend.api;

import it.ranauro.beesafe.backend.model.Report;

import it.ranauro.beesafe.backend.ApiResponse;

import io.vertx.core.Future;

import java.util.List;

public interface DefaultApi  {
    Future<ApiResponse<Void>> addReport(Report report);
    Future<ApiResponse<List<Report>>> filterReportsByGravity();
    Future<ApiResponse<List<Report>>> filterReportsByUrgency();
    Future<ApiResponse<List<Report>>> findReportByKindOfProblem(String kindOfProblem);
    Future<ApiResponse<List<Report>>> findReports();
}
