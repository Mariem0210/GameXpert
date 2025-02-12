package tn.esprit.models;

import java.time.LocalDate;

public class Certificat {
    private int idc;         // Identifiant du certificat
    private int idu;         // Identifiant de l'utilisateur
    private int idf;         // Identifiant de la formation
    private String validitec; // Validité du certificat (sous forme de String)
    private LocalDate datec;  // Date d'émission du certificat
    private String description;     // Description du certificat

    // Constructeur par défaut
    public Certificat() {
    }

    // Constructeur avec tous les paramètres
    public Certificat(int idc, int idu, int idf, String validitec, LocalDate datec, String description) {
        this.idc = idc;
        this.idu = idu;
        this.idf = idf;
        this.validitec = validitec;
        this.datec = datec;
        this.description = description;
    }

    // Constructeur sans idc (lors de l'ajout, l'ID sera généré automatiquement)
    public Certificat(int idu, int idf, String validitec, LocalDate datec, String description) {
        this.idu = idu;
        this.idf = idf;
        this.validitec = validitec;
        this.datec = datec;
        this.description = description;
    }

    // Getters et Setters
    public int getIdc() {
        return idc;
    }

    public void setIdc(int idc) {
        this.idc = idc;
    }

    public int getIdu() {
        return idu;
    }

    public void setIdu(int idu) {
        this.idu = idu;
    }

    public int getIdf() {
        return idf;
    }

    public void setIdf(int idf) {
        this.idf = idf;
    }

    public String getValiditec() {
        return validitec;
    }

    public void setValiditec(String validitec) {
        this.validitec = validitec;
    }

    public LocalDate getDatec() {
        return datec;
    }

    public void setDatec(LocalDate datec) {
        this.datec = datec;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Certificat{" +
                "idc=" + idc +
                ", idu=" + idu +
                ", idf=" + idf +
                ", validitec='" + validitec + '\'' +
                ", datec=" + datec +
                ", description='" + description + '\'' +
                '}';
    }
}
