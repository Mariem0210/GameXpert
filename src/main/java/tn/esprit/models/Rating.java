package tn.esprit.models;

public class Rating {
    private int id;
    private int idf;
    private double note;
    private String nomf;

    // Constructeur par défaut (sans arguments)
    public Rating() {
        // Initialisation par défaut ou vide
    }

    // Constructeur avec 4 arguments
    public Rating(int id, int idf, double note, String nomf) {
        this.id = id;
        this.idf = idf;
        this.note = note;
        this.nomf = nomf;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getIdf() {
        return idf;
    }

    public double getNote() {
        return note;
    }

    public String getNomf() {
        return nomf;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setIdf(int idf) {
        this.idf = idf;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public void setNomf(String nomf) {
        this.nomf = nomf;
    }
}
