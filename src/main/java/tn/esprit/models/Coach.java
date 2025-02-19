package tn.esprit.models;

import java.time.LocalDate;

public final class Coach extends Utilisateur {

    // Constructeur avec ID
    public Coach(int idu,
                 String nomu,
                 String prenomu,
                 String mailu,
                 String mdpu,
                 int numtelu,
                 LocalDate datenaissanceu) {
        super(idu, nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu);
        this.setTypeu(UserType.COACH.toString());
    }

    // Constructeur sans ID pour la création
    public Coach(String nomu,
                 String prenomu,
                 String mailu,
                 String mdpu,
                 int numtelu,
                 LocalDate datenaissanceu) {
        super(nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu);
        this.setTypeu(UserType.COACH.toString());
    }

    @Override
    public String toString() {
        return "Coach{" +
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
