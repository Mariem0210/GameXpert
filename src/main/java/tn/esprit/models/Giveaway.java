package tn.esprit.models;

import java.time.LocalDate;

public class Giveaway {
    private int idg;
    private int idu;
    private String titreg;
    private String descg;
    private LocalDate datedg;
    private LocalDate datefg;
    private String statusg;

    // Constructeurs
    public Giveaway() {
    }

    public Giveaway(int idg, int idu, String titreg, String descg, LocalDate datedg, LocalDate datefg, String statusg) {
        this.idg = idg;
        this.idu = idu;
        this.titreg = titreg;
        this.descg = descg;
        this.datedg = datedg;
        this.datefg = datefg;
        this.statusg = statusg;
    }

    public Giveaway(int idu, String titreg, String descg, LocalDate datedg, LocalDate datefg, String statusg) {
        this.idu = idu;
        this.titreg = titreg;
        this.descg = descg;
        this.datedg = datedg;
        this.datefg = datefg;
        this.statusg = statusg;
    }

    // Getters et Setters
    public int getIdg() {
        return idg;
    }

    public void setIdg(int idg) {
        this.idg = idg;
    }

    public int getIdu() {
        return idu;
    }

    public void setIdu(int idu) {
        this.idu = idu;
    }

    public String getTitreg() {
        return titreg;
    }

    public void setTitreg(String titreg) {
        this.titreg = titreg;
    }

    public String getDescg() {
        return descg;
    }

    public void setDescg(String descg) {
        this.descg = descg;
    }

    public LocalDate getDatedg() {
        return datedg;
    }

    public void setDatedg(LocalDate datedg) {
        this.datedg = datedg;
    }

    public LocalDate getDatefg() {
        return datefg;
    }

    public void setDatefg(LocalDate datefg) {
        this.datefg = datefg;
    }

    public String getStatusg() {
        return statusg;
    }

    public void setStatusg(String statusg) {
        this.statusg = statusg;
    }

    @Override
    public String toString() {
        return "Giveaway{" +
                "idg=" + idg +
                ", idu=" + idu +
                ", titreg='" + titreg + '\'' +
                ", descg='" + descg + '\'' +
                ", datedg=" + datedg +
                ", datefg=" + datefg +
                ", statusg='" + statusg + '\'' +
                '}';
    }
}
