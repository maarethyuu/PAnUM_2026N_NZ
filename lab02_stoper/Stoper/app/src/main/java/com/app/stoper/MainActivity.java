package com.app.stoper;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // stoper
    private TextView tvStopwatch;
    private Handler stopwatchHandler = new Handler();
    private boolean isStopwatchRunning = false;
    private long stopwatchStartTime = 0;
    private long stopwatchAccumulatedTime = 0;

    // zmienne
    private TextView tvTimer;
    private EditText etTimerMinutes, etTimerSeconds;
    private Handler timerHandler = new Handler();
    private boolean isTimerRunning = false;
    private long timerTimeLeftInMillis = 0;
    private long timerEndTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStopwatch = findViewById(R.id.tvStopwatch);
        tvTimer = findViewById(R.id.tvTimer);
        etTimerMinutes = findViewById(R.id.etTimerMinutes);
        etTimerSeconds = findViewById(R.id.etTimerSeconds);

        // przyciski stoper - obsługa
        findViewById(R.id.btnStopwatchStart).setOnClickListener(v -> startStopwatch());
        findViewById(R.id.btnStopwatchPause).setOnClickListener(v -> pauseStopwatch());
        findViewById(R.id.btnStopwatchReset).setOnClickListener(v -> resetStopwatch());

        // przyciski minutnika - obsługa
        findViewById(R.id.btnTimerSet).setOnClickListener(v -> setTimer());
        findViewById(R.id.btnTimerStart).setOnClickListener(v -> startTimer());
        findViewById(R.id.btnTimerPause).setOnClickListener(v -> pauseTimer());
        findViewById(R.id.btnTimerReset).setOnClickListener(v -> resetTimer());

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }


    private void startStopwatch() {
        if (!isStopwatchRunning) {
            stopwatchStartTime = System.currentTimeMillis();
            isStopwatchRunning = true;
            stopwatchHandler.post(stopwatchRunnable);
        }
    }

    private void pauseStopwatch() {
        if (isStopwatchRunning) {
            stopwatchAccumulatedTime += System.currentTimeMillis() - stopwatchStartTime;
            isStopwatchRunning = false;
            stopwatchHandler.removeCallbacks(stopwatchRunnable);
        }
    }

    private void resetStopwatch() {
        isStopwatchRunning = false;
        stopwatchHandler.removeCallbacks(stopwatchRunnable);
        stopwatchAccumulatedTime = 0;
        updateStopwatchUI(0);
    }

    private Runnable stopwatchRunnable = new Runnable() {
        @Override
        public void run() {
            if (isStopwatchRunning) {
                long currentMillis = System.currentTimeMillis() - stopwatchStartTime + stopwatchAccumulatedTime;
                updateStopwatchUI(currentMillis);
                stopwatchHandler.postDelayed(this, 10);
            }
        }
    };

    private void updateStopwatchUI(long totalMillis) {
        int minutes = (int) (totalMillis / 60000);
        int seconds = (int) ((totalMillis % 60000) / 1000);
        int hundredths = (int) ((totalMillis % 1000) / 10);

        tvStopwatch.setText(String.format("%02d:%02d:%02d", minutes, seconds, hundredths));
    }


    // minutnik
    private void setTimer() {
        if (isTimerRunning) return;

        String minInput = etTimerMinutes.getText().toString();
        String secInput = etTimerSeconds.getText().toString();

        int minutes = minInput.isEmpty() ? 0 : Integer.parseInt(minInput);
        int seconds = secInput.isEmpty() ? 0 : Integer.parseInt(secInput);

        timerTimeLeftInMillis = (minutes * 60000L) + (seconds * 1000L);
        updateTimerUI(timerTimeLeftInMillis);
    }

    private void startTimer() {
        if (!isTimerRunning && timerTimeLeftInMillis > 0) {
            timerEndTime = System.currentTimeMillis() + timerTimeLeftInMillis;
            isTimerRunning = true;
            timerHandler.post(timerRunnable);
        }
    }

    private void pauseTimer() {
        if (isTimerRunning) {
            timerTimeLeftInMillis = timerEndTime - System.currentTimeMillis();
            isTimerRunning = false;
            timerHandler.removeCallbacks(timerRunnable);
        }
    }

    private void resetTimer() {
        isTimerRunning = false;
        timerHandler.removeCallbacks(timerRunnable);
        timerTimeLeftInMillis = 0;
        updateTimerUI(0);
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTimerRunning) {
                timerTimeLeftInMillis = timerEndTime - System.currentTimeMillis();

                if (timerTimeLeftInMillis <= 0) {
                    timerTimeLeftInMillis = 0;
                    isTimerRunning = false;
                    updateTimerUI(0);
                    Toast.makeText(MainActivity.this, "Czas minął!", Toast.LENGTH_SHORT).show();
                } else {
                    updateTimerUI(timerTimeLeftInMillis);
                    timerHandler.postDelayed(this, 100);
                }
            }
        }
    };

    private void updateTimerUI(long timeLeft) {
        int minutes = (int) (timeLeft / 60000);
        int seconds = (int) ((timeLeft % 60000) / 1000);
        int tenths = (int) ((timeLeft % 1000) / 100);

        tvTimer.setText(String.format("%02d:%02d:%d", minutes, seconds, tenths));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("isStopwatchRunning", isStopwatchRunning);
        outState.putLong("stopwatchStartTime", stopwatchStartTime);
        outState.putLong("stopwatchAccumulatedTime", stopwatchAccumulatedTime);

        outState.putBoolean("isTimerRunning", isTimerRunning);
        outState.putLong("timerTimeLeftInMillis", timerTimeLeftInMillis);
        outState.putLong("timerEndTime", timerEndTime);
    }

    private void restoreState(Bundle savedInstanceState) {
        isStopwatchRunning = savedInstanceState.getBoolean("isStopwatchRunning");
        stopwatchStartTime = savedInstanceState.getLong("stopwatchStartTime");
        stopwatchAccumulatedTime = savedInstanceState.getLong("stopwatchAccumulatedTime");

        if (isStopwatchRunning) {
            stopwatchHandler.post(stopwatchRunnable);
        } else {
            updateStopwatchUI(stopwatchAccumulatedTime);
        }

        isTimerRunning = savedInstanceState.getBoolean("isTimerRunning");
        timerTimeLeftInMillis = savedInstanceState.getLong("timerTimeLeftInMillis");
        timerEndTime = savedInstanceState.getLong("timerEndTime");

        if (isTimerRunning) {
            timerHandler.post(timerRunnable);
        } else {
            updateTimerUI(timerTimeLeftInMillis);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopwatchHandler.removeCallbacks(stopwatchRunnable);
        timerHandler.removeCallbacks(timerRunnable);
    }
}