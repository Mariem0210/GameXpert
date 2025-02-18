package TN.ESPRIT.models;
import java.util.Date;
public class Commande {
    private int Id_commande, Quantite, id, Id_produit;
    private Date Date_commande;
    private float Montant_totale;




    public Commande() {

    }
    public Commande(int Id_commande, Date Date_commande, float Montant_totale, int Quantite, int Id_produit, int id) {
        this.Id_commande = Id_commande;
        this.Date_commande = Date_commande;
        this.Montant_totale = Montant_totale;
        this.Quantite = Quantite;
        this.Id_produit = Id_produit;
        this.id = id;
    }

    public Commande(Date Date_commande, float Montant_totale, int Quantite, int Id_produit, int id) {
        this.Date_commande = Date_commande;
        this.Montant_totale = Montant_totale;
        this.Quantite = Quantite;
        this.Id_produit = Id_produit;
        this.id = id;
    }
    public int getId_commande(){
        return Id_commande;
    }
    public void setId_commande(int Id_commande){
        this.Id_commande = Id_commande;
    }
    public Date getDate_commande(){
        return Date_commande;
    }
    public void setDate_commande(Date Date_commande){
        this.Date_commande = Date_commande;
    }
    public float getMontant_totale(){
        return Montant_totale;
    }
    public void setMontant_totale(float Montant_totale){
        this.Montant_totale = Montant_totale;
    }
    public int getQuantite(){
        return Quantite;
    }
    public void setQuantite(int Quantite){
        this.Quantite = Quantite;
    }
    public int getId_produit(){
        return Id_produit;
    }
    public void setId_produit(int Id_produit){
        this.Id_produit = Id_produit;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }





    @Override
    public String toString() {
        return "Commande{" +
                "Id_commande=" + Id_commande +
                ", Date_commande='" + Date_commande + '\'' +
                ", Montant_totale=" + Montant_totale +
                ", Quantite=" + Quantite +
                ", Id_produit=" + Id_produit +
                ", id=" + id +
                '}';
    }
}