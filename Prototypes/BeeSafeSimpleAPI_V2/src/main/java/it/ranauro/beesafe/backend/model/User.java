package it.ranauro.beesafe.backend.model;

/**
 * Classe che modella l'utente che ha effettuato la richiesta
 */
public class User {
    private String email;
    private String role;
    private String password;
    private String fullName;
    private String age;

    /**
     * Costruttore
     * @param email     L'email dell'utente
     * @param role      il ruolo dell'utente
     * @param password  la password dell'utente
     * @param fullName  il nome completo dell'utente
     * @param age       l'età dell'utente
     */
    public User(String email, String role, String password, String fullName, String age) {
        this.email = email;
        this.role = role;
        this.password = password;
        this.fullName = fullName;
        this.age = age;
    }

    /**
     * costruttore vuoto
     */
    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
