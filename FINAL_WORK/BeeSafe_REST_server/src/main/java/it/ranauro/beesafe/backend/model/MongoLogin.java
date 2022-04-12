package it.ranauro.beesafe.backend.model;

/**
 * classe usata per modellare il login dell'utente mongo
 */
public class MongoLogin {
    private String username;
    private String password;

    /**
     * costruttore
     * @param username l'username
     * @param password la password
     */
    public MongoLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * costruttore vuoto
     */
    public MongoLogin() {
    }

    /**
     * Ritorna l'username dell'utente
     * @return l'username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Imposta l'username dell'utente
     * @param username l'username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Ritorna la password dell'utente in chiaro
     * @return la password in chiaro
     */
    public String getPassword() {
        return password;
    }

    /**
     * imposta la password dell'utente in chiaro
     * @param password la password in chiaro
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
