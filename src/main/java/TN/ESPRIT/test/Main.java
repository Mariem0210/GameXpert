package TN.ESPRIT.test;
import TN.ESPRIT.models.Commande;
import TN.ESPRIT.models.Produit;
import TN.ESPRIT.services.ServiceCommande;
import TN.ESPRIT.services.ServiceProduit;
import TN.ESPRIT.utils.MyDatabase;
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
        Produit p = new Produit(1, "pc_gam" , "i9", 2000, 9, new Date(), "INFORm", "image_produit655");
        //sp.add(p);
        //sp.update(p);
        sp.delete(p);
        System.out.println(sp.getAll());

    }
}