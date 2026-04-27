package com.app.konwerter;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText inputField;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputField = findViewById(R.id.arabicNumber);
        resultText = findViewById(R.id.result);
        Button buttonConvert = findViewById(R.id.buttonConverter);

        setupKeyboard();

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = inputField.getText().toString();

                if (text.isEmpty()) {
                    Toast.makeText(MainActivity.this, "wpisz coś !", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int number = Integer.parseInt(text);

                    if (number > 0 && number < 4000) {
                        String roman = toRoman(number);
                        resultText.setText(roman);
                        Log.d("KONWERTER", "przeliczono: " + number + " -> " + roman);
                    } else {
                        resultText.setText("zły zakres");
                        Toast.makeText(MainActivity.this, "tylko liczby od 1 do 3999", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    resultText.setText("błąd");
                }
            }
        });
    }

    private void setupKeyboard() {
        findViewById(R.id.button1).setOnClickListener(v -> typeNumber("1"));
        findViewById(R.id.button2).setOnClickListener(v -> typeNumber("2"));
        findViewById(R.id.button3).setOnClickListener(v -> typeNumber("3"));
        findViewById(R.id.button4).setOnClickListener(v -> typeNumber("4"));
        findViewById(R.id.button5).setOnClickListener(v -> typeNumber("5"));
        findViewById(R.id.button6).setOnClickListener(v -> typeNumber("6"));
        findViewById(R.id.button7).setOnClickListener(v -> typeNumber("7"));
        findViewById(R.id.button8).setOnClickListener(v -> typeNumber("8"));
        findViewById(R.id.button9).setOnClickListener(v -> typeNumber("9"));
        findViewById(R.id.button0).setOnClickListener(v -> typeNumber("0"));

        findViewById(R.id.buttonClear).setOnClickListener(v -> {
            inputField.setText("");
            resultText.setText("...");
        });

        findViewById(R.id.buttonDelete).setOnClickListener(v -> {
            String currentText = inputField.getText().toString();
            if (currentText.length() > 0) {
                String newText = currentText.substring(0, currentText.length() - 1);
                inputField.setText(newText);
            }
        });
    }

    private void typeNumber(String num) {
        String current = inputField.getText().toString();
        if (current.length() < 4) {
            inputField.setText(current + num);
        }
    }

    private String toRoman(int num) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanLetters = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        int tempNum = num;
        String result = "";

        for (int i = 0; i < values.length; i++) {
            while (tempNum >= values[i]) {
                tempNum = tempNum - values[i];
                result = result + romanLetters[i];
            }
        }

        return result;
    }
}