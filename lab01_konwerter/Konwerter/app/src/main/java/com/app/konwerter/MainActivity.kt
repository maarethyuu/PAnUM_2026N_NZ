package com.app.konwerter

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var inputField: EditText
    private lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputField = findViewById(R.id.arabicNumber)
        resultText = findViewById(R.id.result)
        val buttonConvert = findViewById<Button>(R.id.buttonConverter)

        setupKeyboard()

        buttonConvert.setOnClickListener {
            val text = inputField.text.toString()

            if (text.isEmpty()) {
                Toast.makeText(this, "wpisz liczbę", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val number = text.toInt()

                if (number > 0 && number < 4000) {
                    val roman = toRoman(number)
                    resultText.text = roman
                    Log.d("KONWERTER", "przeliczono: $number -> $roman")
                } else {
                    resultText.text = "zły zakres"
                    Toast.makeText(this, "tylko liczby od 1 do 3999", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                resultText.text = "błąd"
            }
        }
    }

    private fun setupKeyboard() {
        findViewById<Button>(R.id.button1).setOnClickListener { typeNumber("1") }
        findViewById<Button>(R.id.button2).setOnClickListener { typeNumber("2") }
        findViewById<Button>(R.id.button3).setOnClickListener { typeNumber("3") }
        findViewById<Button>(R.id.button4).setOnClickListener { typeNumber("4") }
        findViewById<Button>(R.id.button5).setOnClickListener { typeNumber("5") }
        findViewById<Button>(R.id.button6).setOnClickListener { typeNumber("6") }
        findViewById<Button>(R.id.button7).setOnClickListener { typeNumber("7") }
        findViewById<Button>(R.id.button8).setOnClickListener { typeNumber("8") }
        findViewById<Button>(R.id.button9).setOnClickListener { typeNumber("9") }
        findViewById<Button>(R.id.button0).setOnClickListener { typeNumber("0") }

        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            inputField.setText("")
            resultText.text = "..."
        }

        findViewById<Button>(R.id.buttonDelete).setOnClickListener {
            val currentText = inputField.text.toString()
            if (currentText.length > 0) {
                val newText = currentText.substring(0, currentText.length - 1)
                inputField.setText(newText)
            }
        }
    }

    private fun typeNumber(num: String) {
        val current = inputField.text.toString()
        if (current.length < 4) {
            inputField.setText(current + num)
        }
    }

    private fun toRoman(num: Int): String {
        val values = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
        val romanLetters = arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")

        var tempNum = num
        var result = ""

        for (i in values.indices) {
            while (tempNum >= values[i]) {
                tempNum = tempNum - values[i]
                result = result + romanLetters[i]
            }
        }

        return result
    }
}