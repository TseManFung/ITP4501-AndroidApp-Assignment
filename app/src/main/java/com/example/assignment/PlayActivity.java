package com.example.assignment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayActivity extends AppCompatActivity {
    TextView tvTimer, tvGameQuestionNo, tvQuestion;
    LinearLayout llNormalMode;
    TableLayout tlMCMode;
    EditText etAnswer;
    Button btnSubmit, btnMC1, btnMC2, btnMC3, btnMC4;
    boolean easyMode;

    final String[] operator = {"+", "-", "*", "/"};
    int answer, correctAnswer, questionNo = 0, correctNo = 0, wrongNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // get easyMode
        SharedPreferences settings = getSharedPreferences("assignment_setting", 0);
        easyMode = settings.getBoolean("easyMode", false);

        // bind view
        tvTimer = findViewById(R.id.gameTimer);
        tvGameQuestionNo = findViewById(R.id.gameQuestionNo);
        tvQuestion = findViewById(R.id.question);
        llNormalMode = findViewById(R.id.normalMode);
        tlMCMode = findViewById(R.id.mcMode);
        etAnswer = findViewById(R.id.answer);
        btnSubmit = findViewById(R.id.submit);
        btnMC1 = findViewById(R.id.mc1);
        btnMC2 = findViewById(R.id.mc2);
        btnMC3 = findViewById(R.id.mc3);
        btnMC4 = findViewById(R.id.mc4);

        showAnswerArea();

    }

    // MCAnswer
    public void MCAnswer(View v) {
        //submitMCAnswer
        if (v == btnMC1) {

        } else if (v == btnMC2) {

        } else if (v == btnMC3) {

        } else if (v == btnMC4) {

        }
    }

    //submitTextAnswer
    public void submitTextAnswer(View v) {
        //submitTextAnswer
    }

    private void showAnswerArea() {
        if (easyMode) {
            llNormalMode.setVisibility(View.GONE);
            tlMCMode.setVisibility(View.VISIBLE);
        } else {
            llNormalMode.setVisibility(View.VISIBLE);
            tlMCMode.setVisibility(View.GONE);
        }
    }

    private void hideAnswerArea() {
        llNormalMode.setVisibility(View.GONE);
        tlMCMode.setVisibility(View.GONE);
    }

    private void genQuestion() {
        // select operator
        int opIndex = (int) (Math.random() * operator.length);
        String op = operator[opIndex];
        int a = (int) (Math.random() * 100),
                b = (int) (Math.random() * 100);
        switch (op) {
            case "+":
                correctAnswer = a + b;
                break;
            case "-":
                correctAnswer = a - b;
                break;
            case "*":
                correctAnswer = a * b;
                break;
            case "/":
                correctAnswer = a;
                a *= b;
                break;
        }
        tvQuestion.setText(a + " " + op + " " + b + " = ?");

    }

}