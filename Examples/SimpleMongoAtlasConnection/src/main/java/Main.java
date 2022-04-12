
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.json.JsonObject;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        MongoClient client = MongoClients.create(
                connectionStringBuilder(
                        loadLogin("C:/Users/giuli/Desktop/credentials.txt")
                ));

        MongoDatabase database = client.getDatabase("BeeSafe");

        MongoCollection collection = database.getCollection("Reports");


        /*
        //org.bson
        Document document = new Document()
            .append("testKey", "testValue");

        collection.insertOne(document);

        FindIterable<Document> allDocs = collection.find();

        for (Document doc : allDocs)
            System.out.println(doc.toJson());

*/
        Report report = new Report(15.2312, "Descrizione report max 100 caratteri", 1, 16.213, 3402884609L, 3,
                "/path/to/image", "via dei mulini", "25/04/32 29:00", 1, "violenza", "peppe99");

        /*
        report.setDescription("Descrizione report max 100 caratteri");
        report.setDateTime("25/04/32 29:00");
        report.setFromUser("peppe");
        report.setGravity(1);
        report.setId(1);
        report.setImagePath("/path/to/image");
        report.setLatitude(15.2312);
        report.setLongitude(16.213);
        report.setPhoneNumber(3402884609L);
        report.setKindOfProblem("violenza");
        */

        Document document = new Document()
                .append("longitude",15.2312)
                .append("description", "Descrizione report max 100 caratteri")
                .append("gravity", 1)
                .append("latitude", 16.213)
                .append("phone_number", 3402884609L)
                .append("urgency", 3)
                .append("image_path", "/path/to/image")
                .append("place_name", "via dei mulini")
                .append("date_time", "25/04/32 29:00")
                .append("id", 1)
                .append("kind_of_problem", "violenza")
                .append("from_user", "peppe99");

        System.out.println(document.toJson());
        collection.insertOne(document);

    }

    private static Login loadLogin(String xmlPath) throws FileNotFoundException {
        File file = new File(xmlPath);
        Scanner scanner = new Scanner(file);
        return new Login(scanner.nextLine(), scanner.nextLine());
    }
    private static String connectionStringBuilder(Login login){
        return "mongodb+srv://" + login.getUsername() + ":" + login.getPassword() + "@cpcluster.looze.mongodb.net/test?authSource=admin&replicaSet=atlas-3pe7q7-shard-0&readPreference=primary&appname=MongoDB%20Compass&ssl=true";
    }
}
