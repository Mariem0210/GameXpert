package tn.esprit.models;

public class Rating {
    private int id;
    private int idf;
    private double note;

    public Rating(int id, int idf, double note) {
        this.id = id;
        this.idf = idf;
        this.note = note;
    }

    // Getters et Setters
    public int getId() { return id; }
    public int getIdf() { return idf; }
    public double getNote() { return note; }
}
