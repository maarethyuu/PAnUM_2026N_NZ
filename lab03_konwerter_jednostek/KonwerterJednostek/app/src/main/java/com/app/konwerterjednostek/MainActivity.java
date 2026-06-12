package com.app.konwerterjednostek;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText inputValue;
    private EditText resultValue;
    private Spinner spinnerConversion;

    private final double EURO_RATE = 4.30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        resultValue = findViewById(R.id.resultValue);
        spinnerConversion = findViewById(R.id.spinnerConversion);
        Button btnConvert = findViewById(R.id.btnConvert);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.conversion_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConversion.setAdapter(adapter);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performConversion();
            }
        });
    }

    private void performConversion() {
        String inputText = inputValue.getText().toString();

        if (inputText.isEmpty()) {
            Toast.makeText(this, "Najpierw wpisz wartość!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double inputNumber = Double.parseDouble(inputText);
            double resultNumber = 0.0;

            int selectedOption = spinnerConversion.getSelectedItemPosition();

            switch (selectedOption) {
                case 0: // PLN na EUR
                    resultNumber = inputNumber / EURO_RATE;
                    break;
                case 1: // CM na CALE (1 cal = 2.54 cm)
                    resultNumber = inputNumber / 2.54;
                    break;
                case 2: // Celsjusze na Fahrenheity: F = C * 1.8 + 32
                    resultNumber = (inputNumber * 1.8) + 32;
                    break;
                case 3: // Kilometry na Mile (1 km = 0.621371 mil)
                    resultNumber = inputNumber * 0.621371;
                    break;
            }

            String finalResult = String.format("%.2f", resultNumber);
            resultValue.setText(finalResult);

        } catch (Exception e) {
            Toast.makeText(this, "Nieprawidłowy format liczby!", Toast.LENGTH_SHORT).show();
        }
    }
}