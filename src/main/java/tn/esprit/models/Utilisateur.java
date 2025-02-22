package tn.esprit.models;

import java.time.LocalDate;

public class Utilisateur {

    private int idu, numtelu;
    private String nomu;
    private String prenomu;
    private String mailu;
    private String mdpu;
    private static String typeu;
    private LocalDate  dateinscriu,datenaissanceu;
    protected String photo_de_profile;



    // Constructeur
    public Utilisateur(int idu, String text, String prenomuTfText, String mailuTfText, String mdpuTfText, String typeuTfText, int numTel, LocalDate dateNaissance, LocalDate dateInscription) {

    }
    public Utilisateur(int idu, String nomu, String prenomu, String mailu, String mdpu, int numtelu, LocalDate datenaissanceu,String photo_de_profile) {
        this.idu = idu;
        this.nomu = nomu;
        this.prenomu = prenomu;
        this.mailu = mailu;
        this.mdpu = mdpu;
        this.numtelu = numtelu;
        this.datenaissanceu = datenaissanceu;
        this.dateinscriu = LocalDate.now();
        this.photo_de_profile = photo_de_profile;
    }

    // Constructeur sans ID
    public Utilisateur(String nomu, String prenomu, String mailu, String mdpu,int numtelu, LocalDate datenaissanceu,String photo_de_profile) {
        this.nomu = nomu;
        this.prenomu = prenomu;
        this.mailu = mailu;
        this.mdpu = mdpu;
        this.numtelu = numtelu;
        this.datenaissanceu = datenaissanceu;
        this.dateinscriu = LocalDate.now();
        this.photo_de_profile = photo_de_profile;
    }


    public Utilisateur(String nomu, String prenomu, String mailu, String mdpu, int numtelu, String datenaissanceu, String typeu ,LocalDate dateinscriu) {
    }

    public Utilisateur(int id, String nom, String prenom, String userType, String email, String password, LocalDate dateDeNaissance, int numTelephone) {
    }

    public Utilisateur() {

    }


    // Getters
    public int getIdu() { return idu; }
    public int getNumtelu() { return numtelu; }
    public String getNomu() { return nomu; }
    public String getPrenomu() { return prenomu; }
    public String getMailu() { return mailu; }
    public String getMdpu() { return mdpu; }
    public static String getTypeu() { return typeu; }
    public LocalDate getDatenaissanceu() { return datenaissanceu; }
    public LocalDate getDateinscriu() { return dateinscriu; }
    public String getPhoto_de_profile() { return photo_de_profile; }

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
    public void setPhoto_de_profile(String Photo_de_profile) { this.photo_de_profile = Photo_de_profile; }

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