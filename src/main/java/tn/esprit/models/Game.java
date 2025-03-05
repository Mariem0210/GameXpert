package tn.esprit.models;

public class Game {
    private String name;
    private String imageUrl;
    private double rating;

    public Game(String name, String imageUrl, double rating) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public double getRating() { return rating; }
}
