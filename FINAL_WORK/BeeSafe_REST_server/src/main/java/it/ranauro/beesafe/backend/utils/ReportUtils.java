package it.ranauro.beesafe.backend.utils;

import it.ranauro.beesafe.backend.model.Report;
import org.bson.Document;

public class ReportUtils {
    public static Report getReportFromDocument(Document document){
        return new Report(
                document.getDouble("longitude"),
                document.getString("description"),
                document.getInteger("gravity"),
                document.getDouble("latitude"),
                document.getLong("phoneNumber"),
                document.getInteger("urgency"),
                document.getString("imagePath"),
                document.getString("placeName"),
                document.getString("dateTime"),
                document.getInteger("id"),
                document.getString("kindOfProblem"),
                document.getString("fromUser")
        );
    }

    public static Document getDocumentFromReport(Report report){
        return new Document()
                .append("longitude", report.getLongitude())
                .append("description", report.getDescription())
                .append("gravity", report.getGravity())
                .append("latitude", report.getLatitude())
                .append("phoneNumber", report.getPhoneNumber())
                .append("urgency", report.getUrgency())
                .append("imagePath", report.getImagePath())
                .append("placeName", report.getPlaceName())
                .append("dateTime", report.getDateTime())
                .append("id", report.getId())
                .append("kindOfProblem", report.getKindOfProblem())
                .append("fromUser", report.getFromUser());
    }
}
