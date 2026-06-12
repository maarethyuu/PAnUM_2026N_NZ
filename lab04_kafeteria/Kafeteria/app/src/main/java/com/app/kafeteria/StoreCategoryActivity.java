package com.app.kafeteria;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class StoreCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ArrayAdapter<Store> listAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                Store.stores
        );

        ListView listStores = findViewById(R.id.list_items);
        listStores.setAdapter(listAdapter);

        listStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(StoreCategoryActivity.this, StoreActivity.class);
                intent.putExtra(StoreActivity.EXTRA_STOREID, (int) id);
                startActivity(intent);
            }
        });
    }
}