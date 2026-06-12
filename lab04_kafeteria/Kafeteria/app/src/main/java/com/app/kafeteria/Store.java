package com.app.kafeteria;


public class Store {
    private String name;
    private String address;
    private String openHours;

    public static final Store[] stores = {
            new Store("Kafeteria Centrum", "ul. Jasna 12, Warszawa", "Pn-Pt: 7:00 - 21:00, So-Nd: 9:00 - 20:00"),
            new Store("Kafeteria Parkowa", "Al. Róż 4, Kraków", "Pn-Pt: 8:00 - 22:00, So-Nd: 8:00 - 22:00"),
            new Store("Kafeteria Starówka", "Rynek 15, Wrocław", "Pn-Pt: 7:30 - 20:00, So-Nd: 10:00 - 18:00")
    };

    private Store(String name, String address, String openHours) {
        this.name = name;
        this.address = address;
        this.openHours = openHours;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getOpenHours() { return openHours; }

    @Override
    public String toString() {
        return this.name;
    }
}