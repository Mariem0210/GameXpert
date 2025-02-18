package TN.ESPRIT.services;
import TN.ESPRIT.interfaces.IService;
import TN.ESPRIT.models.Commande;
import TN.ESPRIT.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommande implements IService<Commande>{
    private Connection cnx;
    public ServiceCommande() {
        cnx = MyDatabase.getInstance().getCnx();
    }
    @Override
    public void add(Commande commande){
        String qry = "INSERT INTO commande(`Date_commande`, `Montant_totale`, `Quantite`, `Id_produit`, `id`) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDate(1, new java.sql.Date(commande.getDate_commande().getTime()));
            pstm.setFloat(2, commande.getMontant_totale());
            pstm.setInt(3, commande.getQuantite());
            pstm.setInt(4, commande.getId_produit());
            pstm.setInt(5, commande.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<Commande> getAll() {
        List<Commande> commandes = new ArrayList<>();
        String qry = "SELECT * FROM commande";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Commande c = new Commande();
                c.setId_commande(rs.getInt("id_commande"));
                c.setDate_commande(rs.getDate("date_commande"));
                c.setMontant_totale(rs.getFloat("Montant_totale"));
                c.setQuantite(rs.getInt("Quantite"));
                c.setId_produit(rs.getInt("Id_produit"));
                c.setId(rs.getInt("id"));


                commandes.add(c);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return commandes;

    }
    @Override
    public void update(Commande commande) {
        String qry = "UPDATE commande SET `Date_commande`=?, `Montant_totale`=?, `Quantite`=?, `Id_produit`=?, `id`=? WHERE `Id_commande`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDate(1, new java.sql.Date(commande.getDate_commande().getTime()));
            pstm.setFloat(2, commande.getMontant_totale());
            pstm.setInt(3, commande.getQuantite());
            pstm.setInt(4, commande.getId_produit());
            pstm.setInt(5, commande.getId());
            pstm.setInt(6, commande.getId_commande()); // Correction ici

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la commande : " + e.getMessage());
        }
    }

    @Override
    public void delete(Commande commande) {
        String qry = "DELETE FROM commande WHERE `Id_commande`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, commande.getId_commande());
            pstm.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


}