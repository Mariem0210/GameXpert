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
                  LocalDate datenaissanceu) {
        super(idu, nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu);
        this.setTypeu(UserType.JOUEUR.toString());
    }

    // Constructeur sans ID pour la création
    public Joueur(String nomu,
                  String prenomu,
                  String mailu,
                  String mdpu,
                  int numtelu,
                  LocalDate datenaissanceu) {
        super(nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu);
        this.setTypeu(UserType.JOUEUR.toString());
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