package tn.esprit.models;

import java.util.Date;

public class Transfert {
    private int idtr, idu, montantt;
    private String ancienne_equipe, nouvelle_equipe;
    private Date datet;

    public Transfert() {
    }
    public Transfert(int idtr, int idu, String ancienne_equipe, String nouvelle_equipe, int montantt, Date datet) {
        this.idtr = idtr;
        this.idu = idu;
        this.ancienne_equipe = ancienne_equipe;
        this.nouvelle_equipe = nouvelle_equipe;
        this.montantt = montantt;
        this.datet = datet;
    }
    public Transfert(int idu ,String ancienne_equipe, String nouvelle_equipe, int montantt, Date datet) {
        this.idu = idu;
        this.ancienne_equipe = ancienne_equipe;
        this.nouvelle_equipe = nouvelle_equipe;
        this.montantt = montantt;
        this.datet = datet;
    }
    public int getIdtr() {
        return idtr;
    }

    public void setIdtr(int idtr) {
        this.idtr = idtr;
    }
    public int getIdu() {
        return idu;
    }
    public void setIdu(int idu) {
        this.idu = idu;
    }
    public String getAncienne_equipe() {
        return ancienne_equipe;
    }
    public void setAncienne_equipe(String ancienne_equipe) {
        this.ancienne_equipe = ancienne_equipe;
    }
    public String getNouvelle_equipe() {
        return nouvelle_equipe;
    }
    public void setNouvelle_equipe(String nouvelle_equipe) {
        this.nouvelle_equipe = nouvelle_equipe;
    }
    public int getMontantt() {
        return montantt;
    }
    public void setMontantt(int montantt) {
        this.montantt = montantt;
    }
    public Date getDatet() {
        return datet;
    }
    public void setDatet(Date datet) {
        this.datet = datet;
    }
    @Override
    public String toString() {
        return "Transfert{" +
                "idtr=" + idtr +
                ", idu=" + idu + '\'' +
                ", ancienne_equipe='" + ancienne_equipe + '\'' +
                ", nouvelle_equipe='" + nouvelle_equipe + '\'' +
                ", montantt=" + montantt + '\'' +
                ", Datet='" + datet + '\'' +
                "}\n";
    }

}
