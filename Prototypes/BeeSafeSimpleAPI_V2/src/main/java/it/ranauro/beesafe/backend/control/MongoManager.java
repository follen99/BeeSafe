package it.ranauro.beesafe.backend.control;

import com.mongodb.client.*;
//import com.sun.org.apache.bcel.internal.Const;
import it.ranauro.beesafe.backend.model.MongoLogin;
import it.ranauro.beesafe.backend.model.Report;
import it.ranauro.beesafe.backend.model.User;
import it.ranauro.beesafe.backend.utils.*;
import it.ranauro.beesafe.backend.utils.exceptions.NotAuthenticatedException;
import it.ranauro.beesafe.backend.utils.exceptions.NotAuthorizatedException;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// per ordinare le collezioni mongo
import static com.mongodb.client.model.Sorts.descending;

/**
 * Classe usata per instaurare una connessione con il server MongoDB
 * ed effettuare delle query
 */
public class MongoManager {
    private static MongoManager INSTANCE;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;


    /**
     * Costruttore privato dell'oggetto
     * @throws FileNotFoundException    Se il file usato per recuperare le credenziali per accedere al server non è stato trovato
     */
    private MongoManager() throws FileNotFoundException {
        /**
         * @Todo aggiornare il modo con cui vengono reperite le credenziali mongo
         */
        mongoClient = MongoClients.create(
                connectionStringBuilder(loadLogin(Constants.MONGO_CREDENTIALS_PATH))
        );

        mongoDatabase = mongoClient.getDatabase("BeeSafe");
        mongoCollection = mongoDatabase.getCollection("Reports");

    }

    /**
     * Usato per recuperare l'istanza dell'oggetto singleton MongoManager
     * Ho strutturato questo oggetto seguendo il pattern singleton in modo da avere un'unica connessione
     * durante tutta la durata dell'interlocuzione. Questo perchè avviare una connessione ogni volta
     * risulterebbe in rallentamenti eccessivi. E' possibile chiudere la connessione con il metodo closeConnection().
     * @return  L'istanza di MongoManager
     * @throws FileNotFoundException    Se il file usato per recuperare le credenziali per accedere al server non è stato trovato
     */
    public static MongoManager getInstance() throws FileNotFoundException {
        if (INSTANCE == null){
            INSTANCE = new MongoManager();
        }
        return INSTANCE;
    }

    /**
     * Usato per creare l'oggetto contenente le credenziali dell'utente usato per connettersi al server MongoDB
     * @param xmlPath   Percorso del file contenente username e password dell'utente mongo
     * @return          un oggetto MongoLogin
     * @throws FileNotFoundException    se il file delle credenziali non è stato trovato al path specificato
     */
    private static MongoLogin loadLogin(String xmlPath) throws FileNotFoundException {
        File file = new File(xmlPath);
        Scanner scanner = new Scanner(file);
        return new MongoLogin(scanner.nextLine(), scanner.nextLine());
    }

    /**
     * Usato per costruire la stringa usata per connettersi al server mongoDB
     * @param login L'oggetto Login da cui si ricavano le informazioni utili a popolare la stringa
     * @return      La stringa usata per connettersi al server
     */
    private static String connectionStringBuilder(MongoLogin login){
        return "mongodb+srv://" + login.getUsername() + ":" + login.getPassword() + "@cpcluster.looze.mongodb.net/test?authSource=admin&replicaSet=atlas-3pe7q7-shard-0&readPreference=primary&appname=MongoDB%20Compass&ssl=true";
    }


    // ################################# database manipulation #################################

    /**
     * Usato per postare un report al database MongoDB
     * @param report    il report (documento) da postare
     * @param uid       l'id dell'utente; univoco, viene usato per identificare l'utente nel db firebase
     * @param digest    il digest inviato dall'utente; viene calcolato a partire dalla password concatenata ad un segreto comune tra server e client
     * @throws NotAuthorizatedException se l'utente che ha effettuato la richiesta è non autenticato
     * @throws NotAuthenticatedException se l'utente che ha effettuato la richiesta non è autorizzato (non è USR)
     */
    public void addReport(Report report, String digest, FirebaseDbHelper firebaseHelper) throws NotAuthorizatedException, NotAuthenticatedException {
        //firebaseHelper = new FirebaseDbHelper(uid);

        firebaseHelper.getUserFromFirebaseDB();

        User retrievedUSer = firebaseHelper.getRetievedUser();
        if (retrievedUSer == null)
            throw new NotAuthenticatedException("User not found");

        String remoteDigest = firebaseHelper.calcRemoteDigest();

        if (remoteDigest.equalsIgnoreCase(digest)){
            // se il client che ah richiesto la POST è USR e non ADM (ADM può vedere ma non postare)
            if (retrievedUSer.getRole().equalsIgnoreCase(Constants.USER)){
                mongoCollection.insertOne(ReportUtils.getDocumentFromReport(report));
            }else{
                throw new NotAuthorizatedException("Devi essere un USR per postare reports.");
            }
        } else  throw new NotAuthenticatedException("Non sei autenticato.");


    }

    /**
     * Usato per recuperare i reports dal database MongoDB
     * se l'utente è USR vengono restituiti tutti i reports
     * se l'utente è ADM vengono restituiti solo i suoi reports
     * @param firebaseHelper   la classe usata per caricare reports e trovare l'utente nel database firebase
     * @param digest    il digest inviato dall'utente; viene calcolato a partire dalla password concatenata ad un segreto comune tra server e client
     * @return          Una lista di Report (varia a seconda dei casi)
     * @throws NotAuthenticatedException    se l'utente non è autenticato
     */
    public List<Report> findAll(String digest, FirebaseDbHelper firebaseHelper) throws NotAuthenticatedException {

        //FirebaseDbHelper firebaseHelper = new FirebaseDbHelper(uid);

        firebaseHelper.getUserFromFirebaseDB();


        User retrievedUSer = firebaseHelper.getRetievedUser();
        if (retrievedUSer == null)
            throw new NotAuthenticatedException("User not found");

        String remoteDigest = firebaseHelper.calcRemoteDigest();

        // potrebbe essere fatto meglio ma per il momento lascio così
        if (remoteDigest.equalsIgnoreCase(digest)){
            // se l'utente è un amministratore
            if (retrievedUSer.getRole().equalsIgnoreCase(Constants.ADMINISTRATOR)){
                FindIterable<Document> allDocuments = mongoCollection.find();
                List<Report> allReports = new ArrayList<>();

                for (Document currentDocument : allDocuments)
                    allReports.add(ReportUtils.getReportFromDocument(currentDocument));

                //firebaseHelper.saveLog(RequestType.GET, Operation.READ_ALL, Outcome.SUCCESS);

                return allReports;
            }
            // se è un utente normale ritorniamo solo i suoi report
            else if (retrievedUSer.getRole().equalsIgnoreCase(Constants.USER)){
                // condizion: uguale a ...
                Bson condition = new Document(Constants.MONGO_CONDITION_EQUAL, retrievedUSer.getEmail());
                // filtro: il campo "email" deve essere uguale a...
                Bson filter = new Document(Constants.FROM_USER_MONGO_FIELD_NAME, condition);

                // cerco con il filtro
                FindIterable<Document> allDocuments = mongoCollection.find(filter);
                List<Report> allReports = new ArrayList<>();

                for (Document currentDocument : allDocuments)
                    allReports.add(ReportUtils.getReportFromDocument(currentDocument));
                firebaseHelper.saveLog(RequestType.GET, Operation.READ_ALL, Outcome.SUCCESS);
                return allReports;
            }

        } else {
            //firebaseHelper.saveLog(RequestType.GET, Operation.READ_ALL, Outcome.NOT_AUTHENTICATED);
            throw new NotAuthenticatedException("You are not authenticated");
        }
        //firebaseHelper.saveLog(RequestType.GET, Operation.READ_ALL, Outcome.NOT_AUTHENTICATED);
        throw new NotAuthenticatedException("You are not authenticated");
    }

    /**
     * Usato per recuperare i reports ordinati per gravità discendente
     * @param firebaseHelper   la classe usata per caricare reports e trovare l'utente nel database firebase
     * @param digest    il digest inviato dall'utente; viene calcolato a partire dalla password concatenata ad un segreto comune tra server e client
     * @return          una lista di Report ordinata per gravità discendente
     * @throws NotAuthenticatedException    se l'utente non è autenticato
     * @throws NotAuthorizatedException     se l'utente non è autorizzato
     */
    public List<Report> orderByGravity(String digest, FirebaseDbHelper firebaseHelper) throws NotAuthenticatedException, NotAuthorizatedException {
        //FirebaseDbHelper firebaseHelper = new FirebaseDbHelper(uid);
        firebaseHelper.getUserFromFirebaseDB();
        User retrievedUSer = firebaseHelper.getRetievedUser();
        if (retrievedUSer == null)
            throw new NotAuthenticatedException("User not found");

        String remoteDigest = firebaseHelper.calcRemoteDigest();

        // se l'utente è autenticato
        if (remoteDigest.equalsIgnoreCase(digest)){
            // se l'utente è un amministratore
            if (retrievedUSer.getRole().equalsIgnoreCase(Constants.ADMINISTRATOR)){
                FindIterable<Document> allDocuments = mongoCollection.find().sort(descending(Constants.GRAVITY_MONGO_FIELD_NAME));   // order DESC
                List<Report> allReports = new ArrayList<>();

                for (Document currentDocument : allDocuments)
                    allReports.add(ReportUtils.getReportFromDocument(currentDocument));

                return allReports;
            }else{
                throw new NotAuthorizatedException(Constants.NOT_AUTHORIZATED_EXCEPTION_MESSAGE);
            }
        }
        throw new NotAuthenticatedException(Constants.NOT_AUTHENTICATED_EXCEPTION_MESSAGE);
    }


    /**
     * Usato per recuperare i reports ordinati per urgenza discendente
     * @param firebaseHelper   la classe usata per caricare reports e trovare l'utente nel database firebase
     * @param digest    il digest inviato dall'utente; viene calcolato a partire dalla password concatenata ad un segreto comune tra server e client
     * @return          una lista di Report ordinata per urgenza discendente
     * @throws NotAuthenticatedException    se l'utente non è autenticato
     * @throws NotAuthorizatedException     se l'utente non è autorizzato
     */
    public List<Report> orderByUrgency(String digest, FirebaseDbHelper firebaseHelper) throws NotAuthorizatedException, NotAuthenticatedException {
        //FirebaseDbHelper firebaseHelper = new FirebaseDbHelper(uid);
        firebaseHelper.getUserFromFirebaseDB();
        User retrievedUSer = firebaseHelper.getRetievedUser();
        if (retrievedUSer == null)
            throw new NotAuthenticatedException("User not found");
        String remoteDigest = firebaseHelper.calcRemoteDigest();

        // se l'utente è autenticato
        if (remoteDigest.equalsIgnoreCase(digest)){
            // se l'utente è amministratore
            if (retrievedUSer.getRole().equalsIgnoreCase(Constants.ADMINISTRATOR)){
                FindIterable<Document> allDocuments = mongoCollection.find().sort(descending(Constants.URGENCY_MONGO_FIELD_NAME));   // order DESC

                List<Report> allReports = new ArrayList<>();

                for (Document currentDocument : allDocuments)
                    allReports.add(ReportUtils.getReportFromDocument(currentDocument));

                return allReports;
            } else {
                throw new NotAuthorizatedException(Constants.NOT_AUTHORIZATED_EXCEPTION_MESSAGE);
            }
        }
        throw new NotAuthenticatedException(Constants.NOT_AUTHENTICATED_EXCEPTION_MESSAGE);
    }

    /**
     * Usato per filtrare i reports per tipo di emergenza
     * @param kindOfProblem Tipo di problema per cui filtrare
     * @param firebaseHelper   la classe usata per caricare reports e trovare l'utente nel database firebase
     * @param digest        il digest inviato dall'utente; viene calcolato a partire dalla password concatenata ad un segreto comune tra server e client
     * @return              una lista di Report filtrata per tipo di emergenza
     * @throws NotAuthorizatedException     se l'utente non è autorizzato
     * @throws NotAuthenticatedException    se l'utente non è autenticato
     */
    public List<Report> filterByKindOfProblem(String kindOfProblem, String digest, FirebaseDbHelper firebaseHelper) throws NotAuthorizatedException, NotAuthenticatedException {
        //FirebaseDbHelper firebaseHelper = new FirebaseDbHelper(uid);
        firebaseHelper.getUserFromFirebaseDB();
        User retrievedUSer = firebaseHelper.getRetievedUser();
        if (retrievedUSer == null)
            throw new NotAuthenticatedException("User not found");
        String remoteDigest = firebaseHelper.calcRemoteDigest();

        // se l'utente è autenticato
        if (remoteDigest.equalsIgnoreCase(digest)) {
            // se l'utente è amministratore
            if (retrievedUSer.getRole().equalsIgnoreCase(Constants.ADMINISTRATOR)) {
                Bson condition = new Document(Constants.MONGO_CONDITION_EQUAL, kindOfProblem);
                Bson filter = new Document(Constants.KIND_OF_PROBLEM_MONGO_FIELD_NAME, condition);

                FindIterable<Document> allDocuments = mongoCollection.find(filter);

                List<Report> allReports = new ArrayList<>();

                for (Document currentDocument : allDocuments) {

                    allReports.add(ReportUtils.getReportFromDocument(currentDocument));
                }
                return allReports;
            } else {
                throw new NotAuthorizatedException(Constants.NOT_AUTHORIZATED_EXCEPTION_MESSAGE);
            }
        }
        throw new NotAuthenticatedException(Constants.NOT_AUTHENTICATED_EXCEPTION_MESSAGE);
    }

    /**
     * Usato per chiudere la connessione mongo.
     * Metodo di backup, non particolarmente usato
     */
    public void closeConnection(){
        if (INSTANCE != null)
            mongoClient.close();
    }
}
