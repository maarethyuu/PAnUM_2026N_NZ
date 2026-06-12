package com.app.kafeteria;

public class Snack {
    private String name;
    private String description;
    private double price;
    private int imageResourceId;

    public static final Snack[] snacks = {
            new Snack("Sernik", "Domowy sernik z puszystego twarogu z polewą czekoladową.", 16.00, R.drawable.sernik),
            new Snack("Croissant", "Maślany rogalik z nadzieniem malinowym.", 8.50, R.drawable.croissant),
            new Snack("Szarlotka", "Ciepłe ciasto z polskimi jabłkami i cynamonem.", 15.00, R.drawable.szarlotka)
    };

    private Snack(String name, String description, double price, int imageResourceId) {
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