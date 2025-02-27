import tn.esprit.Test;
import tn.esprit.models.Commande;
import tn.esprit.models.Produit;
import tn.esprit.services.ServiceCommande;
import tn.esprit.services.ServiceProduit;
import tn.esprit.utils.MyDatabase;
import java.time.LocalDate;
import java.util.Date;
public class Main {
    public static void main(String[] args) {
        //ServiceCommande sc = new ServiceCommande();
        //Commande c = new Commande(2, new Date(), 2, 100000, 1, 1);
        //sc.add(c);
        //sc.update(c);
        //sc.delete(c);
        ServiceProduit sp = new ServiceProduit();
       // Produit p = new Produit(1, "pc_gam" , "i9", 2000, 9, new Date(), "INFORm", "image_produit655");
        //sp.add(p);
        //sp.update(p);
        //sp.delete(p);
        System.out.println(sp.getAll());

    }
}