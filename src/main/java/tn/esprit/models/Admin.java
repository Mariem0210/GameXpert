package tn.esprit.models;

import java.time.LocalDate;

public final class Admin extends Utilisateur {

    // Constructeur avec ID
    public Admin(int idu, String nomu, String prenomu, String mailu,
                 String mdpu, int numtelu, LocalDate datenaissanceu) {
        super(idu, nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu);
        this.setTypeu(UserType.ADMIN.toString());
    }

    // Constructeur sans ID
    public Admin(String nomu, String prenomu, String mailu,
                 String mdpu, int numtelu, LocalDate datenaissanceu) {
        super(nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu);
        this.setTypeu(UserType.ADMIN.toString());
    }

    @Override
    public String toString() {
        return "Admin{" +
                "idu=" + getIdu() +
                ", nomu='" + getNomu() + '\'' +
                ", prenomu='" + getPrenomu() + '\'' +
                ", mailu='" + getMailu() + '\'' +
                ", mdpu='" + getMdpu() + '\'' +
                ", typeu='" + getTypeu() + '\'' +
                ", numtelu=" + getNumtelu() +
                ", datenaissanceu=" + getDatenaissanceu() +
                ", dateinscriu=" + getDateinscriu() +
                '}';
    }
}