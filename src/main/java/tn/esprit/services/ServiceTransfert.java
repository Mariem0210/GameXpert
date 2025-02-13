package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Offre_de_recrutement;
import tn.esprit.models.Transfert;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTransfert implements IService<Transfert> {
    private Connection cnx;

    // Constructeur
    public ServiceTransfert() {
        cnx = MyDatabase.getInstance().getCnx();

    }

    @Override
    public void add(Transfert transfert) {
        String qry = "INSERT INTO transfert(`idu`, ancienne_equipe, nouvelle_equipe, montantt, datet ) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, transfert.getIdtr());
            pstm.setString(2, transfert.getAncienne_equipe());
            pstm.setString(3, transfert.getNouvelle_equipe());
            pstm.setInt(4, transfert.getMontantt());
            pstm.setDate(5, new java.sql.Date(transfert.getDatet().getTime()));

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Récupérer tous les matchs
    @Override
    public List<Transfert> getAll() {
        List<Transfert> transferts = new ArrayList<>();
        String qry = "SELECT * FROM transfert";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Transfert t = new Transfert();
                t.setIdtr(rs.getInt("idtr"));
                t.setAncienne_equipe(rs.getString("ancienne_equipe"));
                t.setNouvelle_equipe(rs.getString("nouvelle_equipe"));
                t.setMontantt(rs.getInt("Montantt"));
                t.setDatet(rs.getDate("datet"));


                transferts.add(t);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return transferts;
    }

    @Override
    public void update(Transfert transfert) {
        String qry = "UPDATE transfert SET `idu`=?, `ancienne_equipe`=?, `nouvelle_equipe`=?, `montantt`=?, `datet`=? WHERE `idtr`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, transfert.getIdu());
            pstm.setString(2, transfert.getAncienne_equipe());
            pstm.setString(3, transfert.getNouvelle_equipe());
            pstm.setInt(4, transfert.getMontantt());
            pstm.setDate(5, new java.sql.Date(transfert.getDatet().getTime()));
            pstm.setInt(6, transfert.getIdtr()); // Missing parameter

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void delete(Transfert transfert) {
        String qry = "DELETE FROM transfert WHERE `idtr`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, transfert.getIdtr());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
