package tn.esprit.models;

import java.time.LocalDate;

public class Certificat {
    private int idc;
    private int idf;
    private int idu;
    private String nomc;
    private String typec;
    private float scorec;
    private String etatc;
    private LocalDate dateExpirationc;

    // Constructors
    public Certificat() {}

    public Certificat(int idc, int idf, int idu, String nomc, String typec, float scorec, String etatc, LocalDate dateExpirationc) {
        this.idc = idc;
        this.idf = idf;
        this.idu = idu;
        this.nomc = nomc;
        this.typec = typec;
        this.scorec = scorec;
        this.etatc = etatc;
        this.dateExpirationc = dateExpirationc;
    }

    public Certificat(int idf, int idu, String nomc, String typec, float scorec, String etatc, LocalDate dateExpirationc) {
        this.idf = idf;
        this.idu = idu;
        this.nomc = nomc;
        this.typec = typec;
        this.scorec = scorec;
        this.etatc = etatc;
        this.dateExpirationc = dateExpirationc;
    }

    // Getters and setters
    public int getIdc() {
        return idc;
    }

    public void setIdc(int idc) {
        this.idc = idc;
    }

    public int getIdf() {
        return idf;
    }

    public void setIdf(int idf) {
        this.idf = idf;
    }

    public int getIdu() {
        return idu;
    }

    public void setIdu(int idu) {
        this.idu = idu;
    }

    public String getNomc() {
        return nomc;
    }

    public void setNomc(String nomc) {
        this.nomc = nomc;
    }

    public String getTypec() {
        return typec;
    }

    public void setTypec(String typec) {
        this.typec = typec;
    }

    public float getScorec() {
        return scorec;
    }

    public void setScorec(float scorec) {
        this.scorec = scorec;
    }

    public String getEtatc() {
        return etatc;
    }

    public void setEtatc(String etatc) {
        this.etatc = etatc;
    }

    public LocalDate getDateExpirationc() {
        return dateExpirationc;
    }

    public void setDateExpirationc(LocalDate dateExpirationc) {
        this.dateExpirationc = dateExpirationc;
    }

    @Override
    public String toString() {
        return "Certificat{" +
                "idc=" + idc +
                ", idf=" + idf +
                ", idu=" + idu +
                ", nomc='" + nomc + '\'' +
                ", typec='" + typec + '\'' +
                ", scorec=" + scorec +
                ", etatc='" + etatc + '\'' +
                ", dateExpirationc=" + dateExpirationc +
                '}';
    }
}
