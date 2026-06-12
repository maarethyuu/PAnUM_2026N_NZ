package com.app.kafeteria;


import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StoreActivity extends AppCompatActivity {

    public static final String EXTRA_STOREID = "storeId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        int storeId = (Integer) getIntent().getExtras().get(EXTRA_STOREID);
        Store store = Store.stores[storeId];

        TextView storeName = findViewById(R.id.store_name);
        storeName.setText(store.getName());

        TextView storeAddress = findViewById(R.id.store_address);
        storeAddress.setText(store.getAddress());

        TextView storeHours = findViewById(R.id.store_hours);
        storeHours.setText(store.getOpenHours());
    }
}