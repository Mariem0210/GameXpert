package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Tournoi;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTournoi implements IService<Tournoi> {
    private Connection cnx;

    public ServiceTournoi() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Tournoi tournoi) {
        String qry = "INSERT INTO `tournois`(`nomt`, `descriptiont`, `date_debutt`, `date_fint`, `nbr_equipes`, `prixt`, `statutt`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, tournoi.getNomt());
            pstm.setString(2, tournoi.getDescriptiont());
            pstm.setDate(3, Date.valueOf(tournoi.getDate_debutt()));
            pstm.setDate(4, Date.valueOf(tournoi.getDate_fint()));
            pstm.setInt(5, tournoi.getNbr_equipes());
            pstm.setFloat(6, tournoi.getPrixt());
            pstm.setString(7, tournoi.getStatutt());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Tournoi> getAll() {
        List<Tournoi> tournois = new ArrayList<>();
        String qry = "SELECT * FROM `tournois`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Tournoi t = new Tournoi();
                t.setIdt(rs.getInt("idt"));
                t.setNomt(rs.getString("nomt"));
                t.setDescriptiont(rs.getString("descriptiont"));
                t.setDate_debutt(rs.getDate("date_debutt").toLocalDate());
                t.setDate_fint(rs.getDate("date_fint").toLocalDate());
                t.setNbr_equipes(rs.getInt("nbr_equipes"));
                t.setPrixt(rs.getFloat("prixt"));
                t.setStatutt(rs.getString("statutt"));

                tournois.add(t);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return tournois;
    }

    @Override
    public void update(Tournoi tournoi) {
        String qry = "UPDATE `tournois` SET `nomt`=?, `descriptiont`=?, `date_debutt`=?, `date_fint`=?, `nbr_equipes`=?, `prixt`=?, `statutt`=? WHERE `idt`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, tournoi.getNomt());
            pstm.setString(2, tournoi.getDescriptiont());
            pstm.setDate(3, Date.valueOf(tournoi.getDate_debutt()));
            pstm.setDate(4, Date.valueOf(tournoi.getDate_fint()));
            pstm.setInt(5, tournoi.getNbr_equipes());
            pstm.setFloat(6, tournoi.getPrixt());
            pstm.setString(7, tournoi.getStatutt());
            pstm.setInt(8, tournoi.getIdt());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Tournoi tournoi) {
        String qry = "DELETE FROM `tournois` WHERE `idt`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, tournoi.getIdt());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
