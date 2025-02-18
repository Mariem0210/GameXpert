package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Tournoi;
import tn.esprit.services.ServiceTournoi;

import java.time.LocalDate;
public class GestionTournoi {
    @FXML
    private TextField tfIdt;
    @FXML
    private TextField tfNomt;
    @FXML
    private TextField tfDescriptiont;
    @FXML
    private DatePicker dpDateDebutt;
    @FXML
    private DatePicker dpDateFint;
    @FXML
    private TextField tfNbrEquipes;
    @FXML
    private TextField tfPrixt;
    @FXML
    private TextField tfStatutt;

    IService<Tournoi> st = new ServiceTournoi();
    @FXML
    private Label lbTournois;

    @FXML
    public void ajouterTournoi(ActionEvent actionEvent) {
        try {
            Tournoi t = new Tournoi();
            //t.setIdt(Integer.parseInt(tfIdt.getText()));
            t.setNomt(tfNomt.getText());
            t.setDescriptiont(tfDescriptiont.getText());
            LocalDate dateDebut = dpDateDebutt.getValue();
            LocalDate dateFin = dpDateFint.getValue();

            if (dateDebut != null && dateFin != null) {
                t.setDate_debutt(dateDebut);
                t.setDate_fint(dateFin);
            }

            t.setNbr_equipes(Integer.parseInt(tfNbrEquipes.getText()));
            t.setPrixt(Float.parseFloat(tfPrixt.getText()));
            t.setStatutt(tfStatutt.getText());

            st.add(t);
        } catch (NumberFormatException e) {
            lbTournois.setText("Erreur : Vérifiez vos entrées.");
        }
    }

    public void afficherTournois(ActionEvent actionEvent) {
        StringBuilder sb = new StringBuilder();
        for (Tournoi t : st.getAll()) {
            sb//.append("ID: ").append(t.getIdt())
                    .append(" Nom: ").append(t.getNomt())
                    .append(", Description: ").append(t.getDescriptiont())
                    .append(", Début: ").append(t.getDate_debutt())
                    .append(", Fin: ").append(t.getDate_fint())
                    .append(", Équipes: ").append(t.getNbr_equipes())
                    .append(", Prix: ").append(t.getPrixt())
                    .append(", Statut: ").append(t.getStatutt())
                    .append("\n-----------------------------------\n");
        }
        lbTournois.setText(sb.toString());
    }}
