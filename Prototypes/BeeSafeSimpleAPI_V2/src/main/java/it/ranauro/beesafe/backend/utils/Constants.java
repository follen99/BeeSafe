package it.ranauro.beesafe.backend.utils;

public final class Constants {
    /**
     * @Todo aggiornare il modo con cui vengono reperite le credenziali mongo
     */
    public static final String MONGO_CREDENTIALS_PATH = "C:/Users/giuli/Desktop/credentials.txt";

    /**
     * Stringa usata come parametro per indicare il campo del report
     */
    public static final String URGENCY_MONGO_FIELD_NAME = "urgency";

    /**
     * Stringa usata come parametro per indicare il campo del report
     */
    public static final String GRAVITY_MONGO_FIELD_NAME = "gravity";

    /**
     * Stringa usata come parametro per indicare il campo del report
     */
    public static final String KIND_OF_PROBLEM_MONGO_FIELD_NAME = "kindOfProblem";

    /**
     * Stringa usata come parametro per indicare il campo del report
     */
    public static final String FROM_USER_MONGO_FIELD_NAME = "fromUser";

    /**
     * Stringa usata come parametro per indicare la condizione di uguaglianza
     */
    public static final String MONGO_CONDITION_EQUAL = "$eq";






    /**
     * una semplice proof of concept, il secret non dovrebbe essere tenuto in questo modo.
     * */
    public static final String SECRET = "BernardiMarkMe30L";

    /**
     * Ruolo di Utente base
     */
    public static final String USER = "USR";

    /**
     * Ruolo di Amministratore
     */
    public static final String ADMINISTRATOR = "ADM";

    /**
     * Stringa usata come messaggio nelle eccezioni di autorizzazione
     */
    public static final String NOT_AUTHORIZATED_EXCEPTION_MESSAGE = "Non sei autorizzato";

    /**
     * Stringa usata come messaggio nelle eccezioni di autenticazione
     */
    public static final String NOT_AUTHENTICATED_EXCEPTION_MESSAGE = "Non sei autenticato";


}
