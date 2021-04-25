package com.example.tourismapp;

import java.io.Serializable;

public class Destination implements Serializable {
    private int id, image;
    private float rating;
    private String title, location, description;

    public Destination(int id, int image, float rating, String title, String location, String description) {
        this.id = id;
        this.image = image;
        this.rating = rating;
        this.title = title;
        this.location = location;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
