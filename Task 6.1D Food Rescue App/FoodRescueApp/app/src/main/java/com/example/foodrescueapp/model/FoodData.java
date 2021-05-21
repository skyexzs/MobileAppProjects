package com.example.foodrescueapp.model;

public class FoodData {
    private int food_id;
    private String image_path;
    private String title;
    private String description;
    private long dateAndTime;
    private int quantity;
    private String location;
    private boolean shared;
    private String owner_email;

    public FoodData(String image_path, String title, String description, long dateAndTime, int quantity, String location, boolean shared, String owner_email) {
        this.image_path = image_path;
        this.title = title;
        this.description = description;
        this.dateAndTime = dateAndTime;
        this.quantity = quantity;
        this.location = location;
        this.shared = shared;
        this.owner_email = owner_email;
    }

    public FoodData(int food_id, String image_path, String title, String description, long dateAndTime, int quantity, String location, boolean shared, String owner_email) {
        this.food_id = food_id;
        this.image_path = image_path;
        this.title = title;
        this.description = description;
        this.dateAndTime = dateAndTime;
        this.quantity = quantity;
        this.location = location;
        this.shared = shared;
        this.owner_email = owner_email;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(long dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getOwner_email() {
        return owner_email;
    }

    public void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }
}
