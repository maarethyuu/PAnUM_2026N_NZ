package com.app.kafeteria;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class DrinkActivity extends AppCompatActivity {

    public static final String EXTRA_DRINKID = "drinkId";
    private SQLiteDatabase db;
    private Cursor cursor;
    private String nameText;
    private double priceVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);

        try {
            SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(this);
            db = coffeinaDatabaseHelper.getWritableDatabase();
            cursor = db.query("DRINK",
                    new String[]{"NAME", "DESCRIPTION", "PRICE", "IMAGE_RES_ID"},
                    "_id = ?",
                    new String[]{Integer.toString(drinkId)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                priceVal = cursor.getDouble(2);
                int photoId = cursor.getInt(3);

                TextView name = findViewById(R.id.name);
                name.setText(nameText);

                TextView price = findViewById(R.id.price);
                price.setText(String.format(Locale.getDefault(), "Cena: %.2f zł", priceVal));

                TextView description = findViewById(R.id.description);
                description.setText(descriptionText);

                ImageView photo = findViewById(R.id.photo);
                if (photoId != 0) {
                    photo.setImageResource(photoId);
                } else {
                    photo.setImageResource(android.R.drawable.ic_menu_gallery);
                }
                photo.setContentDescription(nameText);
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_SHORT).show();
        }

        Button btnAdd = findViewById(R.id.btn_add_to_order);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText qtyInput = findViewById(R.id.quantity);
                String qtyStr = qtyInput.getText().toString();
                if (qtyStr.isEmpty()) {
                    Toast.makeText(DrinkActivity.this, "Wpisz poprawną ilość", Toast.LENGTH_SHORT).show();
                    return;
                }
                int qty = Integer.parseInt(qtyStr);
                if (qty <= 0) {
                    Toast.makeText(DrinkActivity.this, "Ilość musi być większa od 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Cursor checkCursor = db.query("CART",
                            new String[]{"_id", "QUANTITY"},
                            "PRODUCT_NAME = ?",
                            new String[]{nameText},
                            null, null, null);

                    if (checkCursor.moveToFirst()) {
                        int existingId = checkCursor.getInt(0);
                        int existingQty = checkCursor.getInt(1);
                        ContentValues updateValues = new ContentValues();
                        updateValues.put("QUANTITY", existingQty + qty);
                        db.update("CART", updateValues, "_id = ?", new String[]{Integer.toString(existingId)});
                    } else {
                        ContentValues cartValues = new ContentValues();
                        cartValues.put("PRODUCT_NAME", nameText);
                        cartValues.put("PRICE", priceVal);
                        cartValues.put("QUANTITY", qty);
                        db.insert("CART", null, cartValues);
                    }
                    checkCursor.close();
                    Toast.makeText(DrinkActivity.this, "Dodano do zamówienia!", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (SQLiteException e) {
                    Toast.makeText(DrinkActivity.this, "Błąd bazy danych", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
    }
}