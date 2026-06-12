package com.app.kafeteria;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter listAdapter;
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

            cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    showEditDialog(id);
                }
            });

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

        listAdapter = new SimpleCursorAdapter(this,
                R.layout.cart_item,
                cursor,
                new String[]{"PRODUCT_NAME", "QUANTITY", "PRICE"},
                new int[]{R.id.cart_product_name, R.id.cart_product_quantity, R.id.cart_product_price},
                0);

        listAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.cart_product_quantity) {
                    int qty = cursor.getInt(columnIndex);
                    ((TextView) view).setText("Ilość: " + qty);
                    return true;
                }
                if (view.getId() == R.id.cart_product_price) {
                    double price = cursor.getDouble(columnIndex);
                    int qty = cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY"));
                    ((TextView) view).setText(String.format(Locale.getDefault(), "Cena: %.2f zł", price * qty));
                    return true;
                }
                return false;
            }
        });

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

    private void showEditDialog(final long itemId) {
        Cursor itemCursor = db.query("CART",
                new String[]{"PRODUCT_NAME", "QUANTITY"},
                "_id = ?",
                new String[]{Long.toString(itemId)},
                null, null, null);

        if (itemCursor.moveToFirst()) {
            String name = itemCursor.getString(0);
            int currentQty = itemCursor.getInt(1);
            itemCursor.close();

            final EditText input = new EditText(this);
            input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            input.setText(String.valueOf(currentQty));
            input.setSelection(input.getText().length());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Edytuj produkt: " + name);
            builder.setView(input);

            builder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String val = input.getText().toString();
                    if (val.isEmpty() || Integer.parseInt(val) <= 0) {
                        db.delete("CART", "_id = ?", new String[]{Long.toString(itemId)});
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("QUANTITY", Integer.parseInt(val));
                        db.update("CART", values, "_id = ?", new String[]{Long.toString(itemId)});
                    }
                    refreshCart();
                }
            });

            builder.setNegativeButton("Usuń z koszyka", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.delete("CART", "_id = ?", new String[]{Long.toString(itemId)});
                    refreshCart();
                }
            });

            builder.setNeutralButton("Anuluj", null);
            builder.show();
        }
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
}