package com.app.kafeteria;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CoffeinaDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "coffeina";
    private static final int DB_VERSION = 2;

    public CoffeinaDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE DRINK (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "PRICE REAL, "
                    + "IMAGE_RES_ID INTEGER);");

            insertDrink(db, "Latte", "Klasyczne espresso z gorącym spienionym mlekiem.", 14.50, R.drawable.latte);
            insertDrink(db, "Cappuccino", "Mocna kawa ze sporym dodatkiem aksamitnej pianki.", 13.00, R.drawable.cappuccino);
            insertDrink(db, "Espresso", "Intensywny napar o głębokim smaku i aromacie.", 9.00, R.drawable.espresso);

            db.execSQL("CREATE TABLE SNACK (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "PRICE REAL, "
                    + "IMAGE_RES_ID INTEGER);");

            insertSnack(db, "Sernik", "Domowy sernik z puszystego twarogu z polewą czekoladową.", 16.00, R.drawable.sernik);
            insertSnack(db, "Croissant", "Maślany rogalik z nadzieniem malinowym.", 8.50, R.drawable.croissant);
            insertSnack(db, "Szarlotka", "Ciepłe ciasto z polskimi jabłkami i cynamonem.", 15.00, R.drawable.szarlotka);

            db.execSQL("CREATE TABLE STORE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "ADDRESS TEXT, "
                    + "OPEN_HOURS TEXT);");

            insertStore(db, "Kafeteria Centrum", "ul. Jasna 12, Warszawa", "Pn-Pt: 7:00 - 21:00, So-Nd: 9:00 - 20:00");
            insertStore(db, "Kafeteria Parkowa", "Al. Róż 4, Kraków", "Pn-Pt: 8:00 - 22:00, So-Nd: 8:00 - 22:00");
            insertStore(db, "Kafeteria Starówka", "Rynek 15, Wrocław", "Pn-Pt: 7:30 - 20:00, So-Nd: 10:00 - 18:00");
        }
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE CART (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "PRODUCT_NAME TEXT, "
                    + "PRICE REAL, "
                    + "QUANTITY INTEGER);");
        }
    }

    private void insertDrink(SQLiteDatabase db, String name, String description, double price, int resourceId) {
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME", name);
        drinkValues.put("DESCRIPTION", description);
        drinkValues.put("PRICE", price);
        drinkValues.put("IMAGE_RES_ID", resourceId);
        db.insert("DRINK", null, drinkValues);
    }

    private void insertSnack(SQLiteDatabase db, String name, String description, double price, int resourceId) {
        ContentValues snackValues = new ContentValues();
        snackValues.put("NAME", name);
        snackValues.put("DESCRIPTION", description);
        snackValues.put("PRICE", price);
        snackValues.put("IMAGE_RES_ID", resourceId);
        db.insert("SNACK", null, snackValues);
    }

    private void insertStore(SQLiteDatabase db, String name, String address, String openHours) {
        ContentValues storeValues = new ContentValues();
        storeValues.put("NAME", name);
        storeValues.put("ADDRESS", address);
        storeValues.put("OPEN_HOURS", openHours);
        db.insert("STORE", null, storeValues);
    }
}