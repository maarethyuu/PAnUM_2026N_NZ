package com.app.kafeteria;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SnackActivity extends AppCompatActivity {

    public static final String EXTRA_SNACKID = "snackId";
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int snackId = (Integer) getIntent().getExtras().get(EXTRA_SNACKID);

        try {
            SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(this);
            db = coffeinaDatabaseHelper.getReadableDatabase();
            cursor = db.query("SNACK",
                    new String[]{"NAME", "DESCRIPTION", "PRICE", "IMAGE_RES_ID"},
                    "_id = ?",
                    new String[]{Integer.toString(snackId)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                double priceVal = cursor.getDouble(2);
                int photoId = cursor.getInt(3);

                TextView name = findViewById(R.id.name);
                name.setText(nameText);

                TextView price = findViewById(R.id.price);
                price.setText(String.format(Locale.getDefault(), "Cena: %.2f zł", priceVal));

                TextView description = findViewById(R.id.description);
                description.setText(descriptionText);

                ImageView photo = findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_SHORT);
            toast.show();
        }
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