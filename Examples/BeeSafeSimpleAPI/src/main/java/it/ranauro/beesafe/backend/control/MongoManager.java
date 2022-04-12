package it.ranauro.beesafe.backend.control;

import com.mongodb.client.*;
import it.ranauro.beesafe.backend.model.MongoLogin;
import it.ranauro.beesafe.backend.model.Report;
import it.ranauro.beesafe.backend.utils.ReportUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public final class MongoManager {
    private static MongoManager INSTANCE;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;



    private MongoManager() throws FileNotFoundException {
        // temporaneo
        mongoClient = MongoClients.create(
                connectionStringBuilder(loadLogin("C:/Users/giuli/Desktop/credentials.txt"))
        );

        mongoDatabase = mongoClient.getDatabase("BeeSafe");
        mongoCollection = mongoDatabase.getCollection("Reports");

    }
    /**
     * metodo singleton
     * */
    public static MongoManager getInstance() throws FileNotFoundException {
        if (INSTANCE == null){
            INSTANCE = new MongoManager();
        }
        return INSTANCE;
    }
    private static MongoLogin loadLogin(String xmlPath) throws FileNotFoundException {
        File file = new File(xmlPath);
        Scanner scanner = new Scanner(file);
        return new MongoLogin(scanner.nextLine(), scanner.nextLine());
    }
    private static String connectionStringBuilder(MongoLogin login){
        return "mongodb+srv://" + login.getUsername() + ":" + login.getPassword() + "@cpcluster.looze.mongodb.net/test?authSource=admin&replicaSet=atlas-3pe7q7-shard-0&readPreference=primary&appname=MongoDB%20Compass&ssl=true";
    }


    // ################################# database manipulation #################################
    public void addReport(Report report){
        mongoCollection.insertOne(ReportUtils.getDocumentFromReport(report));
    }

    public List<Report> findAll(){
        FindIterable<Document> allDocuments = mongoCollection.find();
        List<Report> allReports = new ArrayList<>();

        for (Document currentDocument : allDocuments)
            allReports.add(ReportUtils.getReportFromDocument(currentDocument));

        return allReports;
    }

    public List<Report> orderByGravity(){

        FindIterable<Document> allDocuments = mongoCollection.find().sort(descending("gravity"));   // order DESC

        List<Report> allReports = new ArrayList<>();

        for (Document currentDocument : allDocuments)
            allReports.add(ReportUtils.getReportFromDocument(currentDocument));

        return allReports;
    }

    public List<Report> orderByUrgency() {
        FindIterable<Document> allDocuments = mongoCollection.find().sort(descending("urgency"));   // order DESC

        List<Report> allReports = new ArrayList<>();

        for (Document currentDocument : allDocuments)
            allReports.add(ReportUtils.getReportFromDocument(currentDocument));

        return allReports;
    }

    public List<Report> filterByKindOfProblem(String kindOfProblem) {
        Bson condition = new Document("$eq", kindOfProblem);
        Bson filter = new Document("kindOfProblem", condition);

        FindIterable<Document> allDocuments = mongoCollection.find(filter);

        List<Report> allReports = new ArrayList<>();

        for (Document currentDocument : allDocuments) {

            allReports.add(ReportUtils.getReportFromDocument(currentDocument));
        }
        return allReports;
    }

    public void closeConnection(){
        if (INSTANCE != null)
            mongoClient.close();
    }
}
