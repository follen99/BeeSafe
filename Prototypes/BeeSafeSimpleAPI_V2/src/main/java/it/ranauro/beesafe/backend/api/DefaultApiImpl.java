package it.ranauro.beesafe.backend.api;

import it.ranauro.beesafe.backend.control.FirebaseDbHelper;
import it.ranauro.beesafe.backend.control.MongoManager;
import it.ranauro.beesafe.backend.model.Report;

import it.ranauro.beesafe.backend.ApiResponse;

import io.vertx.core.Future;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import it.ranauro.beesafe.backend.utils.Operation;
import it.ranauro.beesafe.backend.utils.Outcome;
import it.ranauro.beesafe.backend.utils.RequestType;
import it.ranauro.beesafe.backend.utils.exceptions.NotAuthenticatedException;
import it.ranauro.beesafe.backend.utils.exceptions.NotAuthorizatedException;

import java.io.FileNotFoundException;
import java.util.List;

// Implement this class

/**
 * Classe contenente tutte le implementazioni dei metodi HTTP previsti
 */
public class DefaultApiImpl implements DefaultApi {

    /**
     * Usato per postare un nuovo report dal client al database mongo passando per il server REST
     * @param uid       l'id dell'utente; univoco, viene usato per identificare l'utente nel db firebase
     * @param digest    il digest inviato dall'utente; viene calcolato a partire dalla password concatenata ad un segreto comune tra server e client
     * @param report    il report da postare; viene inviato dall'utente al server REST per essere postato in MongoDB
     * @return          il report appena postato, se tutto va bene
     */
    public Future<ApiResponse<Report>> addReport(String uid, String digest, Report report) {
        FirebaseDbHelper helper = new FirebaseDbHelper(uid);
        try {

            // se manca uno dei parametri ritorniamo 400
            if (uid == null || digest == null || report == null) {
                helper.saveLog(RequestType.POST, Operation.ADD_REPORT, Outcome.BAD_REQUEST);
                return Future.failedFuture(new HttpStatusException(400));
            }

            // recuperiamo l'istanza singleton di mongomanager
            // il manager ci permette di effettuare operazioni sul db remoto mongo
            MongoManager manager = MongoManager.getInstance();

            // aggiungiamo un report al db
            manager.addReport(report,digest, helper);

            helper.saveLog(RequestType.POST, Operation.ADD_REPORT, Outcome.SUCCESS);

            // se non vengono lanciate eccezioni durante il push, ritorniamo il report inviato
            ApiResponse<Report> response = new ApiResponse<>(report);
            return Future.succeededFuture(response);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            helper.saveLog(RequestType.POST, Operation.ADD_REPORT, Outcome.GENERIC_ERROR);
        } catch (NotAuthorizatedException e) {
            e.printStackTrace();
            helper.saveLog(RequestType.POST, Operation.ADD_REPORT, Outcome.NOT_AUTHORIZATED);
            return Future.failedFuture(new HttpStatusException(403));
        } catch (NotAuthenticatedException e) {
            helper.saveLog(RequestType.POST, Operation.ADD_REPORT, Outcome.NOT_AUTHENTICATED);
            return Future.failedFuture(new HttpStatusException(401));
        }
        helper.saveLog(RequestType.POST, Operation.ADD_REPORT, Outcome.INTERNAL_SERVER_ERROR);
        return Future.failedFuture(new HttpStatusException(500));
    }

    /**
     * Usato per ordinare i reports per gravità
     * Questo metodo può essere invocato solo da un admin
     * @param digest    il digest inviato dall'utente; viene calcolato a partire dalla password concatenata ad un segreto comune tra server e client
     * @param uid       l'id dell'utente; univoco, viene usato per identificare l'utente nel db firebase
     * @return          Una lista di tutti i reports ordinati per gravità discendente
     */
    public Future<ApiResponse<List<Report>>> filterReportsByGravity(String digest, String uid) {
        FirebaseDbHelper helper = new FirebaseDbHelper(uid);

        try {
            MongoManager manager = MongoManager.getInstance();
            ApiResponse<List<Report>> response = new ApiResponse<>(manager.orderByGravity(digest, helper));

            helper.saveLog(RequestType.GET, Operation.READ_ALL_ORDER_BY_GRAVITY, Outcome.SUCCESS);
            return Future.succeededFuture(response);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            helper.saveLog(RequestType.GET, Operation.READ_ALL_ORDER_BY_GRAVITY, Outcome.GENERIC_ERROR);
        } catch (NotAuthenticatedException e) {
            e.printStackTrace();
            helper.saveLog(RequestType.GET, Operation.READ_ALL_ORDER_BY_GRAVITY, Outcome.NOT_AUTHENTICATED);
            return Future.failedFuture(new HttpStatusException(401));
        } catch (NotAuthorizatedException e) {
            e.printStackTrace();
            helper.saveLog(RequestType.GET, Operation.READ_ALL_ORDER_BY_GRAVITY, Outcome.NOT_AUTHORIZATED);
            return Future.failedFuture(new HttpStatusException(403));
        }
        helper.saveLog(RequestType.GET, Operation.READ_ALL_ORDER_BY_GRAVITY, Outcome.INTERNAL_SERVER_ERROR);
        return Future.failedFuture(new HttpStatusException(500));
    }

    /**
     * Usato per ordinare i reports per urgenza
     * Questo metodo può essere invocato solo da un admin
     * @param digest    il digest inviato dall'utente; viene calcolato a partire dalla password concatenata ad un segreto comune tra server e client
     * @param uid       l'id dell'utente; univoco, viene usato per identificare l'utente nel db firebase
     * @return          Una lista di tutti i reports ordinati per urgenza discendente
     */
    public Future<ApiResponse<List<Report>>> filterReportsByUrgency(String digest, String uid) {
        FirebaseDbHelper helper = new FirebaseDbHelper(uid);

        try {
            MongoManager manager = MongoManager.getInstance();

            ApiResponse<List<Report>> response = new ApiResponse<>(manager.orderByUrgency(digest,helper));

            helper.saveLog(RequestType.GET, Operation.READ_ALL_ORDER_BY_URGENCY, Outcome.SUCCESS);
            return Future.succeededFuture(response);
        } catch (FileNotFoundException e) {
            helper.saveLog(RequestType.GET, Operation.READ_ALL_ORDER_BY_URGENCY, Outcome.GENERIC_ERROR);
            e.printStackTrace();
        } catch (NotAuthenticatedException e) {
            helper.saveLog(RequestType.GET, Operation.READ_ALL_ORDER_BY_URGENCY, Outcome.NOT_AUTHENTICATED);
            e.printStackTrace();
            return Future.failedFuture(new HttpStatusException(401));
        } catch (NotAuthorizatedException e) {
            helper.saveLog(RequestType.GET, Operation.READ_ALL_ORDER_BY_URGENCY, Outcome.NOT_AUTHORIZATED);
            e.printStackTrace();
            return Future.failedFuture(new HttpStatusException(403));
        }
        helper.saveLog(RequestType.GET, Operation.READ_ALL_ORDER_BY_URGENCY, Outcome.INTERNAL_SERVER_ERROR);
        return Future.failedFuture(new HttpStatusException(500));
    }

    /**
     * Usato per filtrare i reports per tipo di emergenza
     * Questo metodo può essere invocato solo da un admin
     * @param digest    il digest inviato dall'utente; viene calcolato a partire dalla password concatenata ad un segreto comune tra server e client
     * @param uid       l'id dell'utente; univoco, viene usato per identificare l'utente nel db firebase
     * @return          Una lista di tutti i reports filtrati per tipo di emergenza
     */
    public Future<ApiResponse<List<Report>>> findReportByKindOfProblem(String digest, String uid, String kindOfProblem) {
        FirebaseDbHelper helper = new FirebaseDbHelper(uid);

        try {
            MongoManager manager = MongoManager.getInstance();
            ApiResponse<List<Report>> response = new ApiResponse<>(manager.filterByKindOfProblem(kindOfProblem, digest, helper));

            helper.saveLog(RequestType.GET, Operation.FILTER_BY, Outcome.SUCCESS);
            return Future.succeededFuture(response);
        } catch (FileNotFoundException e) {
            helper.saveLog(RequestType.GET, Operation.FILTER_BY, Outcome.GENERIC_ERROR);
            e.printStackTrace();
        } catch (NotAuthenticatedException e) {
            helper.saveLog(RequestType.GET, Operation.FILTER_BY, Outcome.NOT_AUTHENTICATED);
            e.printStackTrace();
            return Future.failedFuture(new HttpStatusException(401));
        } catch (NotAuthorizatedException e) {
            helper.saveLog(RequestType.GET, Operation.FILTER_BY, Outcome.NOT_AUTHORIZATED);
            e.printStackTrace();
            return Future.failedFuture(new HttpStatusException(403));
        }
        helper.saveLog(RequestType.GET, Operation.FILTER_BY, Outcome.INTERNAL_SERVER_ERROR);
        return Future.failedFuture(new HttpStatusException(500));
    }

    /**
     * Usato per reperire i reports dal database mongo passando per il server REST.
     * Se l'utente che ha effettuato la richiesta è USR riceverà solo i suoi reports
     * Se l'utente che ha effettuato la richiesta è ADM riceverà tutti i reports.
     * @param uid       l'id dell'utente; univoco, viene usato per identificare l'utente nel db firebase
     * @param digest        il digest inviato dall'utente; viene calcolato a partire dalla password concatenata ad un segreto comune tra server e client
     * @return          una lista di Report contenente tutti i reports trovati nel db (varia a seconda dei casi)
     */
    public Future<ApiResponse<List<Report>>> findReports(String uid, String digest) {
        FirebaseDbHelper helper = new FirebaseDbHelper(uid);
        try {
            MongoManager manager = MongoManager.getInstance();

            ApiResponse<List<Report>> response = new ApiResponse<>(manager.findAll(digest, helper));

            helper.saveLog(RequestType.GET, Operation.READ_ALL, Outcome.SUCCESS);
            return Future.succeededFuture(response);
        } catch (FileNotFoundException e) {
            helper.saveLog(RequestType.GET, Operation.READ_ALL, Outcome.GENERIC_ERROR);
            return Future.failedFuture(new HttpStatusException(404));
        } catch (NotAuthenticatedException e) {
            // non autenticato
            helper.saveLog(RequestType.GET, Operation.READ_ALL, Outcome.NOT_AUTHENTICATED);
            return Future.failedFuture(new HttpStatusException(401));
        }
    }
}
