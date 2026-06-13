package com.app.kafeteria;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private CartCursorAdapter listAdapter;
    private ListView cartList;
    private TextView totalPriceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartList = findViewById(R.id.cart_list);
        totalPriceText = findViewById(R.id.total_price);

        try {
            SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(this);
            db = coffeinaDatabaseHelper.getWritableDatabase();

            refreshCart();

            Button btnEmail = findViewById(R.id.btn_send_email);
            btnEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendOrderEmail();
                }
            });

        } catch (SQLiteException e) {
            Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshCart() {
        if (cursor != null) {
            cursor.close();
        }

        cursor = db.query("CART",
                new String[]{"_id", "PRODUCT_NAME", "PRICE", "QUANTITY"},
                null, null, null, null, null);

        listAdapter = new CartCursorAdapter(this, cursor, 0);
        cartList.setAdapter(listAdapter);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double sum = 0;
        Cursor sumCursor = db.rawQuery("SELECT SUM(PRICE * QUANTITY) FROM CART", null);
        if (sumCursor.moveToFirst()) {
            sum = sumCursor.getDouble(0);
        }
        sumCursor.close();
        totalPriceText.setText(String.format(Locale.getDefault(), "Suma: %.2f zł", sum));
    }

    private void sendOrderEmail() {
        Cursor emailCursor = db.query("CART",
                new String[]{"PRODUCT_NAME", "PRICE", "QUANTITY"},
                null, null, null, null, null);

        if (emailCursor.getCount() == 0) {
            Toast.makeText(this, "Koszyk jest pusty", Toast.LENGTH_SHORT).show();
            emailCursor.close();
            return;
        }

        StringBuilder body = new StringBuilder("Podsumowanie zamówienia w Kafeterii:\n\n");
        double sum = 0;

        while (emailCursor.moveToNext()) {
            String name = emailCursor.getString(0);
            double price = emailCursor.getDouble(1);
            int qty = emailCursor.getInt(2);
            double itemTotal = price * qty;
            sum += itemTotal;

            body.append(name).append(" x ").append(qty)
                    .append(String.format(Locale.getDefault(), " (%.2f zł szt.) = %.2f zł\n", price, itemTotal));
        }
        emailCursor.close();

        body.append(String.format(Locale.getDefault(), "\nŁączna suma zamówienia: %.2f zł", sum));

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Zamówienie z Kafeterii");
        intent.putExtra(Intent.EXTRA_TEXT, body.toString());

        try {
            startActivity(Intent.createChooser(intent, "Wybierz aplikację pocztową:"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Brak zainstalowanej aplikacji pocztowej", Toast.LENGTH_SHORT).show();
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

    private class CartCursorAdapter extends CursorAdapter {

        public CartCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final long itemId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            final String name = cursor.getString(cursor.getColumnIndexOrThrow("PRODUCT_NAME"));
            final double price = cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
            final int qty = cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY"));

            TextView nameView = view.findViewById(R.id.cart_product_name);
            TextView priceView = view.findViewById(R.id.cart_product_price);
            TextView qtyView = view.findViewById(R.id.cart_product_quantity);
            Button btnPlus = view.findViewById(R.id.btn_plus);
            Button btnMinus = view.findViewById(R.id.btn_minus);

            nameView.setText(name);
            qtyView.setText(String.valueOf(qty));
            priceView.setText(String.format(Locale.getDefault(), "Cena: %.2f zł", price * qty));

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put("QUANTITY", qty + 1);
                    db.update("CART", values, "_id = ?", new String[]{Long.toString(itemId)});
                    refreshCart();
                }
            });

            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (qty > 1) {
                        ContentValues values = new ContentValues();
                        values.put("QUANTITY", qty - 1);
                        db.update("CART", values, "_id = ?", new String[]{Long.toString(itemId)});
                    } else {
                        db.delete("CART", "_id = ?", new String[]{Long.toString(itemId)});
                    }
                    refreshCart();
                }
            });
        }
    }
}