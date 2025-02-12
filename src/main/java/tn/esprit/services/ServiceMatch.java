package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Match;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceMatch implements IService<Match> {
    private Connection cnx;

    // Constructeur
    public ServiceMatch() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Ajouter un match
    @Override
    public void add(Match match) {
        String qry = "INSERT INTO `match`(`idt`, `equipe1`, `equipe2`, `date_debutm`, `status`, `score`) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, match.getIdt());  // ID du tournoi auquel le match appartient
            pstm.setString(2, match.getEquipe1());
            pstm.setString(3, match.getEquipe2());
            pstm.setDate(4, Date.valueOf(match.getDate_debutm()));  // Date de début du match
            pstm.setString(5, match.getStatus());
            pstm.setString(6, match.getScore());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Récupérer tous les matchs
    @Override
    public List<Match> getAll() {
        List<Match> match = new ArrayList<>();
        String qry = "SELECT * FROM `match`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Match m = new Match();
                m.setIdm(rs.getInt("idm"));
                m.setIdt(rs.getInt("idt"));
                m.setEquipe1(rs.getString("equipe1"));
                m.setEquipe2(rs.getString("equipe2"));
                m.setDate_debutm(rs.getDate("date_debutm").toLocalDate());
                m.setStatus(rs.getString("status"));
                m.setScore(rs.getString("score"));

                match.add(m);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return match;
    }

    // Mettre à jour un match
    @Override
    public void update(Match match) {
        String qry = "UPDATE `match` SET `idt`=?, `equipe1`=?, `equipe2`=?, `date_debutm`=?, `status`=?, `score`=? WHERE `idm`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, match.getIdt());  // ID du tournoi
            pstm.setString(2, match.getEquipe1());
            pstm.setString(3, match.getEquipe2());
            pstm.setDate(4, Date.valueOf(match.getDate_debutm()));  // Date du match
            pstm.setString(5, match.getStatus());
            pstm.setString(6, match.getScore());
            pstm.setInt(7, match.getIdm());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Supprimer un match
    @Override
    public void delete(Match match) {
        String qry = "DELETE FROM `match` WHERE `idm`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, match.getIdm());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
