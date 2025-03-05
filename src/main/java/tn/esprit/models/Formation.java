package tn.esprit.models;

import java.sql.Date;
import java.time.LocalDate;

public class Formation {
    private int idf;
    private String nomf;
    private String descriptionf;
    private String niveauf;
    private LocalDate dateDebutf;
    private LocalDate dateFinf;
    private int capacitef;
    private float prixf;
    private int idu;
    private String coachName;

    // Constructors
    public Formation() {}

    public Formation(int idf, String nomf, String descriptionf, String niveauf, LocalDate dateDebutf, LocalDate dateFinf, int capacitef, float prixf, int idu) {
        this.idf = idf;
        this.nomf = nomf;
        this.descriptionf = descriptionf;
        this.niveauf = niveauf;
        this.dateDebutf = dateDebutf;
        this.dateFinf = dateFinf;
        this.capacitef = capacitef;
        this.prixf = prixf;
        this.idu = idu;
    }

    public Formation(String nomf, String descriptionf, String niveauf, LocalDate dateDebutf, LocalDate dateFinf, int capacitef, float prixf, int idu) {
    }




    // Getters and setters
    public int getIdf() {
        return idf;
    }

    public void setIdf(int idf) {
        this.idf = idf;
    }

    public String getNomf() {
        return nomf;
    }

    public void setNomf(String nomf) {
        this.nomf = nomf;
    }

    public String getDescriptionf() {
        return descriptionf;
    }

    public void setDescriptionf(String descriptionf) {
        this.descriptionf = descriptionf;
    }

    public String getNiveauf() {
        return niveauf;
    }

    public void setNiveauf(String niveauf) {
        this.niveauf = niveauf;
    }

    public LocalDate getDateDebutf() {
        return dateDebutf;
    }

    public void setDateDebutf(LocalDate dateDebutf) {
        this.dateDebutf = dateDebutf;
    }

    public LocalDate getDateFinf() {
        return dateFinf;
    }

    public void setDateFinf(LocalDate dateFinf) {
        this.dateFinf = dateFinf;
    }

    public int getCapacitef() {
        return capacitef;
    }

    public void setCapacitef(int capacitef) {
        this.capacitef = capacitef;
    }

    public float getPrixf() {
        return prixf;
    }

    public void setPrixf(float prixf) {
        this.prixf = prixf;
    }

    public int getIdu() {
        return idu;
    }

    public void setIdu(int idu) {
        this.idu = idu;
    }
    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "idf=" + idf +
                ", nomf='" + nomf + '\'' +
                ", descriptionf='" + descriptionf + '\'' +
                ", niveauf='" + niveauf + '\'' +
                ", dateDebutf=" + dateDebutf +
                ", dateFinf=" + dateFinf +
                ", capacitef=" + capacitef +
                ", prixf=" + prixf +
                ", idu=" + idu +
                '}';
    }
}
