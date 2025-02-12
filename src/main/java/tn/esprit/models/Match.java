package tn.esprit.models;

import java.time.LocalDate;

public class Match {

    private int idm;  // ID du match
    private int idt;  // ID du tournoi
    private String equipe1;  // Nom de la première équipe
    private String equipe2;  // Nom de la deuxième équipe
    private LocalDate date_debutm;  // Date du début du match
    private String status;  // Statut du match
    private String score;  // Score du match

    // Constructeur par défaut
    public Match() {
    }

    // Constructeur avec tous les attributs
    public Match(int idm, int idt, String equipe1, String equipe2, LocalDate date_debutm, String status, String score) {
        this.idm = idm;
        this.idt = idt;
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
        this.date_debutm = date_debutm;
        this.status = status;
        this.score = score;
    }

    // Constructeur sans ID (par exemple, pour ajouter un match sans spécifier un ID)
    public Match(int idt, String equipe1, String equipe2, LocalDate date_debutm, String status, String score) {
        this.idt = idt;
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
        this.date_debutm = date_debutm;
        this.status = status;
        this.score = score;
    }

    // Getters et setters
    public int getIdm() {
        return idm;
    }

    public void setIdm(int idm) {
        this.idm = idm;
    }

    public int getIdt() {
        return idt;
    }

    public void setIdt(int idt) {
        this.idt = idt;
    }

    public String getEquipe1() {
        return equipe1;
    }

    public void setEquipe1(String equipe1) {
        this.equipe1 = equipe1;
    }

    public String getEquipe2() {
        return equipe2;
    }

    public void setEquipe2(String equipe2) {
        this.equipe2 = equipe2;
    }

    public LocalDate getDate_debutm() {
        return date_debutm;
    }

    public void setDate_debutm(LocalDate date_debutm) {
        this.date_debutm = date_debutm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    // Méthode toString pour afficher les informations du match
    @Override
    public String toString() {
        return "Match{" +
                "idm=" + idm +
                ", idt=" + idt +
                ", equipe1='" + equipe1 + '\'' +
                ", equipe2='" + equipe2 + '\'' +
                ", date_debutm=" + date_debutm +
                ", status='" + status + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
