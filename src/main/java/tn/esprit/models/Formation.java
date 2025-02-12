package tn.esprit.models;

import java.time.LocalDate;

public class Formation {
    private int idf, capacitef, idu;
    private String nomf, descriptionf, niveauf;
    private LocalDate dateDebutf, dateFinf;
    private double prixf;

    public Formation() {
    }

    public Formation(int idf, String nomf, String descriptionf, String niveauf, LocalDate dateDebutf, LocalDate dateFinf, int capacitef, double prixf, int idu) {
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

    public Formation(String nomf, String descriptionf, String niveauf, LocalDate dateDebutf, LocalDate dateFinf, int capacitef, double prixf, int idu) {
        this.nomf = nomf;
        this.descriptionf = descriptionf;
        this.niveauf = niveauf;
        this.dateDebutf = dateDebutf;
        this.dateFinf = dateFinf;
        this.capacitef = capacitef;
        this.prixf = prixf;
        this.idu = idu;
    }

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

    public double getPrixf() {
        return prixf;
    }

    public void setPrixf(double prixf) {
        this.prixf = prixf;
    }

    public int getIdu() {
        return idu;
    }

    public void setIdu(int idu) {
        this.idu = idu;
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
                "}\n";
    }
}
