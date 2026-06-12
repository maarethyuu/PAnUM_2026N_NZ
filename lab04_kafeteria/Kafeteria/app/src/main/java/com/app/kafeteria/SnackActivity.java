package com.app.kafeteria;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SnackActivity extends AppCompatActivity {

    public static final String EXTRA_SNACKID = "snackId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int snackId = (Integer) getIntent().getExtras().get(EXTRA_SNACKID);
        Snack snack = Snack.snacks[snackId];

        TextView name = findViewById(R.id.name);
        name.setText(snack.getName());

        TextView price = findViewById(R.id.price);
        price.setText(String.format(Locale.getDefault(), "Cena: %.2f zł", snack.getPrice()));

        TextView description = findViewById(R.id.description);
        description.setText(snack.getDescription());

        ImageView photo = findViewById(R.id.photo);
        photo.setImageResource(snack.getImageResourceId());
        photo.setContentDescription(snack.getName());
    }
}