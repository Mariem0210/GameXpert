package tn.esprit.models;

public class Offre_de_recrutement {
    private int ido, idu;
    private String poste_recherche, niveu_requis, status, contrat;
    private float salaire_propose;

    public Offre_de_recrutement() {
    }

    public Offre_de_recrutement(int ido, int idu, String poste_recherche, String niveu_requis, float salaire_propose, String status, String contrat) {
        this.ido = ido;
        this.idu = idu;
        this.poste_recherche = poste_recherche;
        this.niveu_requis = niveu_requis;
        this.salaire_propose = salaire_propose;
        this.status = status;
        this.contrat = contrat;

    }

    public Offre_de_recrutement(int idu ,String poste_recherche, String niveu_requis, float salaire_propose, String status, String contrat) {
        this.idu = idu;
        this.poste_recherche = poste_recherche;
        this.niveu_requis = niveu_requis;
        this.salaire_propose = salaire_propose;
        this.status = status;
        this.contrat = contrat;

    }

    public int getIdo() {
        return ido;
    }

    public void setIdo(int ido) {
        this.ido = ido;
    }

    public int getIdu() {
        return idu;
    }

    public void setIdu(int idu) {
        this.idu = idu;
    }

    public String getPoste_recherche() {
        return poste_recherche;
    }

    public void setPoste_recherche(String poste_recherche) {
        this.poste_recherche = poste_recherche;
    }

    public String getNiveu_requis() {
        return niveu_requis;
    }

    public void setNiveu_requis(String niveu_requis) {
        this.niveu_requis = niveu_requis;
    }

    public float getSalaire_propose() {
        return salaire_propose;
    }

    public void setSalaire_propose(float salaire_propose) {
        this.salaire_propose = salaire_propose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContrat() {
        return contrat;
    }

    public void setContrat(String contrat) {
        this.contrat = contrat;
    }
    @Override
    public String toString() {
        return "Offre_de_recrutement{" +
                "ido=" + ido +
                ", idu=" + idu + '\'' +
                ", poste_recherche='" + poste_recherche + '\'' +
                ", niveau_requis='" + niveu_requis + '\'' +
                ", salaire_propose=" + salaire_propose + '\'' +
                ", status='" + status + '\'' +
                ", contrat='" + contrat + '\'' +
                "}\n";
    }
}



