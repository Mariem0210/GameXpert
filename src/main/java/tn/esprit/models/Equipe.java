package tn.esprit.models;

import java.util.Date;

public class Equipe {
    private int ideq, idu;
    private String nom_equipe;
    private Date date_creation;
    public Equipe(){

    }
public Equipe (int ideq, String nom_equipe, Date date_creation, int idu){
        this.ideq = ideq;
        this.nom_equipe = nom_equipe;
        this.date_creation = date_creation;
        this.idu = idu;
}
public Equipe (String nom_equipe, Date date_creation, int idu){
        this.nom_equipe = nom_equipe;
        this.date_creation = date_creation;
        this.idu = idu;
}
public int getIdeq() {
        return ideq;
}
public void setIdeq(int ideq) {
        this.ideq = ideq;
}
public String getNom_equipe() {
        return nom_equipe;
}
public void setNom_equipe(String nom_equipe) {
        this.nom_equipe = nom_equipe;
}
public Date getDate_creation() {
        return date_creation;
}
public void setDate_creation(Date date_creation) {
        this.date_creation = date_creation;
}
public int getIdu() {
        return idu;
}
public void setIdu(int idu) {
        this.idu = idu;
}
public String toString(){
    return "Equipe{" +
            "ideq=" + ideq +
            ", nom_equipe" + nom_equipe + '\'' +
            ", date_creation='" + date_creation + '\'' +
            ", idu='" + idu + '\'' +
            "}\n";
}
}
