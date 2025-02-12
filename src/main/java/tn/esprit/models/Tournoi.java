package tn.esprit.models;

import java.time.LocalDate;


public class Tournoi {

    private int idt;
    private String nomt;
    private String descriptiont;
    private LocalDate date_debutt;
    private LocalDate date_fint;
    private int nbr_equipes;
    private float prixt;
    private String statutt;

    public Tournoi() {
    }

    public Tournoi(int idt, String nomt, String descriptiont, LocalDate date_debutt, LocalDate date_fint, int nbr_equipes, float prixt, String statutt) {
        this.idt = idt;
        this.nomt = nomt;
        this.descriptiont = descriptiont;
        this.date_debutt = date_debutt;
        this.date_fint = date_fint;
        this.nbr_equipes = nbr_equipes;
        this.prixt = prixt;
        this.statutt = statutt;
    }
    public Tournoi( String nomt, String descriptiont, LocalDate date_debutt, LocalDate date_fint, int nbr_equipes, float prixt, String statutt) {
        this.nomt = nomt;
        this.descriptiont = descriptiont;
        this.date_debutt = date_debutt;
        this.date_fint = date_fint;
        this.nbr_equipes = nbr_equipes;
        this.prixt = prixt;
        this.statutt = statutt;
    }
    public int getIdt() {
        return idt;
    }

    public void setIdt(int idt) {
        this.idt = idt;
    }

    public String getNomt() {
        return nomt;
    }

    public void setNomt(String nomt) {
        this.nomt = nomt;
    }

    public String getDescriptiont() {
        return descriptiont;
    }

    public void setDescriptiont(String descriptiont) {
        this.descriptiont = descriptiont;
    }

    public LocalDate getDate_debutt() {
        return date_debutt;
    }

    public void setDate_debutt(LocalDate date_debutt) {
        this.date_debutt = date_debutt;
    }

    public LocalDate getDate_fint() {
        return date_fint;
    }

    public void setDate_fint(LocalDate date_fint) {
        this.date_fint = date_fint;
    }

    public int getNbr_equipes() {
        return nbr_equipes;
    }

    public void setNbr_equipes(int nbr_equipes) {
        this.nbr_equipes = nbr_equipes;
    }

    public float getPrixt() {
        return prixt;
    }

    public void setPrixt(float prixt) {
        this.prixt = prixt;
    }

    public String getStatutt() {
        return statutt;
    }

    public void setStatutt(String statutt) {
        this.statutt = statutt;
    }

    @Override
    public String toString() {
        return "Tournoi{" +
                "idt=" + idt +
                ", nomt='" + nomt + '\'' +
                ", descriptiont='" + descriptiont + '\'' +
                ", date_debutt=" + date_debutt +
                ", date_fint=" + date_fint +
                ", nbr_equipes=" + nbr_equipes +
                ", prixt=" + prixt +
                ", statutt='" + statutt + '\'' +
                '}';
    }
}
