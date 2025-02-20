package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Offre_de_recrutement;
import tn.esprit.models.Transfert;
import tn.esprit.models.Equipe;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEquipe implements IService<Equipe> {
    private Connection cnx;

    // Constructeur
    public ServiceEquipe() {
        cnx = MyDatabase.getInstance().getCnx();
    }
    @Override
    public void add(Equipe equipe) {
        String qry = "INSERT INTO equipe(nom_equipe, date_creation, idu) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, equipe.getNom_equipe()); // Insérer le nom de l'équipe
            pstm.setDate(2, new java.sql.Date(equipe.getDate_creation().getTime())); // Insérer la date de création
            pstm.setInt(3, equipe.getIdu()); // Insérer l'ID de l'utilisateur (capitaine ou responsable)

            pstm.executeUpdate();
            System.out.println("Équipe ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }


    @Override
    public List<Equipe> getAll() {
        List<Equipe> equipes = new ArrayList<>();
        String qry = "SELECT * FROM equipe";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Equipe e = new Equipe();
                e.setIdeq(rs.getInt("ideq"));
                e.setNom_equipe(rs.getString("nom_equipe"));
                e.setDate_creation(rs.getDate("date_creation"));
                e.setIdu(rs.getInt("idu"));

                equipes.add(e);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return equipes;
    }
    @Override
    public void update(Equipe equipe) {
        String qry = "UPDATE equipe SET `nom_equipe`=?, `date_creation`=?, `idu`=? WHERE `ideq`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, equipe.getNom_equipe());
            pstm.setDate(2, new java.sql.Date(equipe.getDate_creation().getTime()));
            pstm.setInt(3, equipe.getIdu());
            pstm.setInt(4, equipe.getIdeq());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void delete(Equipe equipe) {
        String qry = "DELETE FROM equipe WHERE `ideq`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, equipe.getIdeq());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
