package tn.esprit.models;

import java.time.LocalDate;

public class Utilisateur {
    private int idu, numtelu;
    private String nomu, prenomu, mailu, mdpu, typeu;
    private LocalDate datenaissanceu, dateinscriu;

    // Constructeur
    public Utilisateur() {

    }
    public Utilisateur(int idu, int numtelu, String nomu, String prenomu, String mailu, String mdpu, String typeu, LocalDate datenaissanceu, LocalDate dateinscriu) {
        this.idu = idu;
        this.numtelu = numtelu;
        this.nomu = nomu;
        this.prenomu = prenomu;
        this.mailu = mailu;
        this.mdpu = mdpu;
        this.typeu = typeu;
        this.datenaissanceu = datenaissanceu;
        this.dateinscriu = dateinscriu;
    }
    public Utilisateur(int numtelu, String nomu, String prenomu, String mailu, String mdpu, String typeu, LocalDate datenaissanceu, LocalDate dateinscriu) {
        this.numtelu = numtelu;
        this.nomu = nomu;
        this.prenomu = prenomu;
        this.mailu = mailu;
        this.mdpu = mdpu;
        this.typeu = typeu;
        this.datenaissanceu = datenaissanceu;
        this.dateinscriu = dateinscriu;
    }

    // Getters
    public int getIdu() { return idu; }
    public int getNumtelu() { return numtelu; }
    public String getNomu() { return nomu; }
    public String getPrenomu() { return prenomu; }
    public String getMailu() { return mailu; }
    public String getMdpu() { return mdpu; }
    public String getTypeu() { return typeu; }
    public LocalDate getDatenaissanceu() { return datenaissanceu; }
    public LocalDate getDateinscriu() { return dateinscriu; }

    // Setters
    public void setIdu(int idu) { this.idu = idu; }
    public void setNumtelu(int numtelu) { this.numtelu = numtelu; }
    public void setNomu(String nomu) { this.nomu = nomu; }
    public void setPrenomu(String prenomu) { this.prenomu = prenomu; }
    public void setMailu(String mailu) { this.mailu = mailu; }
    public void setMdpu(String mdpu) { this.mdpu = mdpu; }
    public void setTypeu(String typeu) { this.typeu = typeu; }
    public void setDatenaissanceu(LocalDate datenaissanceu) { this.datenaissanceu = datenaissanceu; }
    public void setDateinscriu(LocalDate dateinscriu) { this.dateinscriu = dateinscriu; }

    // Affichage de l'utilisateur
    @Override
    public String toString() {
        return "Utilisateur{" +
                "idu=" + idu +
                ", numtelu=" + numtelu +
                ", nomu='" + nomu + '\'' +
                ", prenomu='" + prenomu + '\'' +
                ", mailu='" + mailu + '\'' +
                ", mdpu='" + mdpu + '\'' +
                ", typeu='" + typeu + '\'' +
                ", datenaissanceu=" + datenaissanceu +
                ", dateinscriu=" + dateinscriu +
                '}';
    }
}
