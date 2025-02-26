package tn.esprit.models;

import java.time.LocalDate;

public final class Joueur extends Utilisateur {

    // Constructeur avec ID
    public Joueur(int idu,
                  String nomu,
                  String prenomu,
                  String mailu,
                  String mdpu,
                  int numtelu,
                  String userType, LocalDate datenaissanceu, LocalDate dateinscriu,String photo_de_profile) {
        super(idu, nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu,photo_de_profile);
        this.setTypeu(UserType.JOUEUR.toString());
    }

    // Constructeur sans ID pour la création
    public Joueur(String nomu,
                  String prenomu,
                  String mailu,
                  String mdpu,
                  int numtelu,
                  LocalDate datenaissanceu,
                  String photo_de_profile) {
        super(nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu,photo_de_profile);
        this.setTypeu(UserType.JOUEUR.toString());
    }

    public Joueur(int idu, String photoDeProfile, String nomu, String prenomu, String mailu, String mdpu, int numtelu, LocalDate dateinscriu, LocalDate datenaissanceu) {
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "ID=" + getIdu() +
                ", Nom='" + getNomu() + '\'' +
                ", Prénom='" + getPrenomu() + '\'' +
                ", Email='" + getMailu() + '\'' +
                ", Téléphone=" + getNumtelu() +
                ", Naissance=" + getDatenaissanceu() +
                ", Inscription=" + getDateinscriu() +
                '}';
    }
}