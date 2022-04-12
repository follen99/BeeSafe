package it.ranauro.beesafe.backend.api;

import it.ranauro.beesafe.backend.model.Report;

import it.ranauro.beesafe.backend.ApiResponse;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

public interface DefaultApi  {
    Future<ApiResponse<Report>> addReport(String uid, String digest, Report report);
    Future<ApiResponse<List<Report>>> filterReportsByGravity(String digest, String uid);
    Future<ApiResponse<List<Report>>> filterReportsByUrgency(String digest, String uid);
    Future<ApiResponse<List<Report>>> findReportByKindOfProblem(String digest, String uid, String kindOfProblem);
    Future<ApiResponse<List<Report>>> findReports(String uid, String digest);
}
