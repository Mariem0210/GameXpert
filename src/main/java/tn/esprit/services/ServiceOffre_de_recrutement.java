package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Offre_de_recrutement;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffre_de_recrutement implements IService<Offre_de_recrutement> {
    private Connection cnx;

    // Constructeur
    public ServiceOffre_de_recrutement() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Ajouter un match
    @Override
    public void add(Offre_de_recrutement offre) {
        String qry = "INSERT INTO offre_de_rectrutement(`idu`, poste_recherche, niveu_requis, salaire_propose, status, contrat) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, offre.getIdo());
            pstm.setString(2, offre.getPoste_recherche());
            pstm.setString(3, offre.getNiveu_requis());
            pstm.setFloat(4, offre.getSalaire_propose());
            pstm.setString(5, offre.getStatus());
            pstm.setString(6, offre.getContrat());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Récupérer tous les matchs
    @Override
    public List<Offre_de_recrutement> getAll() {
        List<Offre_de_recrutement> offre = new ArrayList<>();
        String qry = "SELECT * FROM offre_de_rectrutement";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Offre_de_recrutement o = new Offre_de_recrutement();
                o.setIdo(rs.getInt("ido"));
                o.setPoste_recherche(rs.getString("poste_recherche"));
                o.setNiveu_requis(rs.getString("niveu_requis"));
                o.setSalaire_propose(rs.getFloat("salaire_propose"));
                o.setStatus(rs.getString("status"));
                o.setContrat(rs.getString("contrat"));


                offre.add(o);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return offre;
    }

    // Mettre à jour un match
    @Override
    public void update(Offre_de_recrutement offre) {
        String qry = "UPDATE `offre_de_rectrutement` SET `idu`=?, `poste_recherche`=?, `niveu_requis`=?, `salaire_propose`=?, `status`=?, `contrat`=? WHERE `ido`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, offre.getIdu());
            pstm.setString(2, offre.getPoste_recherche());
            pstm.setString(3, offre.getNiveu_requis());
            pstm.setFloat(4, offre.getSalaire_propose());
            pstm.setString(5, offre.getStatus());
            pstm.setString(6, offre.getContrat());
            pstm.setInt(7, offre.getIdo()); // Fix: Added missing parameter

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    // Supprimer un match
    @Override
    public void delete(Offre_de_recrutement offre) {
        String qry = "DELETE FROM offre_de_rectrutement WHERE `ido`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, offre.getIdo());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}