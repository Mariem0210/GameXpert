package tn.esprit.models;

import java.time.LocalDate;

public class Certificat {
    private int idc;
    private int idu;
    private int idf;
    private String validitec;
    private LocalDate datec;
    private String description;


    public Certificat() {
    }


    public Certificat(int idc, int idu, int idf, String validitec, LocalDate datec, String description) {
        this.idc = idc;
        this.idu = idu;
        this.idf = idf;
        this.validitec = validitec;
        this.datec = datec;
        this.description = description;
    }


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
