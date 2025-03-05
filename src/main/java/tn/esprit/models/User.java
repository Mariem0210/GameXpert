package tn.esprit.models;

public class User {
    private int idu;        // Ajout de l'ID utilisateur
    private String mailu;
    private String nomu;

    // Constructeur par défaut
    public User() {}

    // Constructeur avec paramètres
    public User(int idu, String nomu, String mailu) {
        this.idu = idu;
        this.nomu = nomu;
        this.mailu = mailu;
    }

    // Getters et Setters
    public int getIdu() {
        return idu;
    }

    public void setIdu(int idu) {
        this.idu = idu;
    }

    public String getMailu() {
        return mailu;
    }

    public void setMailu(String mailu) {
        this.mailu = mailu;
    }

    public String getNomu() {
        return nomu;
    }

    public void setNomu(String nomu) {
        this.nomu = nomu;
    }

    @Override
    public String toString() {
        return (nomu != null) ? nomu : "Utilisateur inconnu"; // Évite les erreurs si nomu est null
    }
}
