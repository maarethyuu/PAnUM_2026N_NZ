package com.app.kafeteria;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StoreActivity extends AppCompatActivity {

    public static final String EXTRA_STOREID = "storeId";
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        int storeId = (Integer) getIntent().getExtras().get(EXTRA_STOREID);

        try {
            SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(this);
            db = coffeinaDatabaseHelper.getReadableDatabase();
            cursor = db.query("STORE",
                    new String[]{"NAME", "ADDRESS", "OPEN_HOURS"},
                    "_id = ?",
                    new String[]{Integer.toString(storeId)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String addressText = cursor.getString(1);
                String hoursText = cursor.getString(2);

                TextView storeName = findViewById(R.id.store_name);
                storeName.setText(nameText);

                TextView storeAddress = findViewById(R.id.store_address);
                storeAddress.setText(addressText);

                TextView storeHours = findViewById(R.id.store_hours);
                storeHours.setText(hoursText);
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