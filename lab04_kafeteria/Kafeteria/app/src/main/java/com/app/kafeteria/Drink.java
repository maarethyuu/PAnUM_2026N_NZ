package com.app.kafeteria;

public class Drink {
    private String name;
    private String description;
    private double price;
    private int imageResourceId;

    public static final Drink[] drinks = {
            new Drink("Latte", "Klasyczne espresso z gorącym spienionym mlekiem.", 14.50, R.drawable.espresso),
            new Drink("Cappuccino", "Mocna kawa ze sporym dodatkiem aksamitnej pianki.", 13.00, R.drawable.cappuccino),
            new Drink("Espresso", "Intensywny napar o głębokim smaku i aromacie.", 9.00, R.drawable.latte)
    };

    private Drink(String name, String description, double price, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResourceId = imageResourceId;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getImageResourceId() { return imageResourceId; }

    @Override
    public String toString() {
        return this.name;
    }
}