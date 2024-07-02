package com.example.assignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends AppCompatActivity {
    TextView tvTimer, tvGameQuestionNo, tvQuestion, tvCorrectWrong, tvCorrectAnswer;
    LinearLayout llNormalMode,llFeedBack;
    TableLayout tlMCMode;
    EditText etAnswer;
    Button btnSubmit, btnMC1, btnMC2, btnMC3, btnMC4, btnNext,btnContinue;
    boolean easyMode,timerOn = false;

    final String[] operator = {"+", "-", "*", "/"};
    int correctAnswer, questionNo = 0, correctNo = 0, wrongNo = 0,timeCount = 0;

    Timer timer;

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
        btnNext = findViewById(R.id.next);
        tvCorrectWrong = findViewById(R.id.CorrectWrong);
        tvCorrectAnswer = findViewById(R.id.correctAnswer);
        llFeedBack = findViewById(R.id.feedBack);
        timerOn = true;
        btnContinue = findViewById(R.id.Continue);

        tvTimer.setText(getString(R.string.gameTime)+ timeCount +" "+ getString(R.string.sec));

        startGame();


    }

    public void restartGame(View v){
        startGame();
    }

    //startGame
    private void startGame() {
        btnContinue.setVisibility(View.GONE);
        timerOn = false;
        questionNo =  correctNo =  wrongNo = timeCount = 0;

        nextQuestion();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (timerOn) {
                    timeCount++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTimer.setText(getString(R.string.gameTime)+ timeCount +" "+ getString(R.string.sec));
                        }
                    });
                }
            }
        }, 1000, 1000);

    }

    //not done
    private void endGame() {
        //endGame
        timer.cancel();
        timer.purge();
        btnContinue.setVisibility(View.VISIBLE);

        hideAnswerArea();
        llFeedBack.setVisibility(View.GONE);
        tvQuestion.setText(tvTimer.getText());
        tvTimer.setText(R.string.finish);
        tvGameQuestionNo.setText(getString(R.string.Correctis)+correctNo+getString(R.string.Wrongis)+wrongNo+"!");

    }

    //nextQuestion
    private void nextQuestion() {
        llFeedBack.setVisibility(View.GONE);
        if (questionNo < 10) {
            questionNo++;
            tvGameQuestionNo.setText(getString(R.string.gameQuestion) + questionNo);
            genQuestion();
            showAnswerArea();

        } else {
            //endGame
            endGame();

        }
    }

    public void nextQuestion(View v) {
        nextQuestion();
    }

    // MCAnswer
    public void MCAnswer(View v) {
        hideAnswerArea();

        int answer;
        if (v == btnMC1) {
            answer = Integer.parseInt(btnMC1.getText().toString());
        } else if (v == btnMC2) {
            answer = Integer.parseInt(btnMC2.getText().toString());
        } else if (v == btnMC3) {
            answer = Integer.parseInt(btnMC3.getText().toString());
        } else if (v == btnMC4) {
            answer = Integer.parseInt(btnMC4.getText().toString());
        } else {
            throw new RuntimeException("Invalid button");
        }
        checkAnswer(answer);
    }

    private void checkAnswer(int answer) {

        if (answer == correctAnswer) {
            correctNo++;
            tvCorrectWrong.setText(R.string.Correct);
            tvCorrectAnswer.setText("");
        } else {
            wrongNo++;
            tvCorrectWrong.setText(R.string.Wrong);
            tvCorrectAnswer.setText(getString(R.string.Answeris)+ correctAnswer+" !");
        }
        llFeedBack.setVisibility(View.VISIBLE);
    }
    //submitTextAnswer
    private void alert(String s) {
        AlertDialog.Builder alertDialog =
                new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.AnswerMissing));
        alertDialog.setMessage(s);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        alertDialog.setCancelable(true);
        alertDialog.show();
    }
    public void submitTextAnswer(View v) {
        if (etAnswer.getText().toString().isEmpty()) {
            alert(getString(R.string.PleaseEnterAnswer));
            return;
        }


        hideAnswerArea();

        int answer = Integer.parseInt(etAnswer.getText().toString());
        checkAnswer(answer);
    }

    private void showAnswerArea() {

        llFeedBack.setVisibility(View.GONE);
        if (easyMode) {
            llNormalMode.setVisibility(View.GONE);
            tlMCMode.setVisibility(View.VISIBLE);
        } else {
            llNormalMode.setVisibility(View.VISIBLE);
            tlMCMode.setVisibility(View.GONE);
        }
        timerOn = true;
    }

    private void hideAnswerArea() {
        timerOn = false;
        llNormalMode.setVisibility(View.GONE);
        tlMCMode.setVisibility(View.GONE);
    }

    private void genQuestion() {
        // select operator
        int opIndex = (int) (Math.random() * operator.length);
        String op = operator[opIndex];
        int a = ((int) (Math.random() * 100)) + 1, b;
        switch (op) {
            case "+":
                b = ((int) (Math.random() * 100)) + 1;
                correctAnswer = a + b;
                break;
            case "-":
                b = ((int) (Math.random() * a)) + 1;
                correctAnswer = a - b;
                break;
            case "*":
                b = ((int) (Math.random() * 100)) + 1;
                correctAnswer = a * b;
                break;
            case "/":
                a = ((int) (Math.random() * 50)) + 1;
                b = ((int) (Math.random() * ((int) (100 / a)))) + 1;
                correctAnswer = a;
                a *= b;
                break;
            default:
                throw new RuntimeException("Invalid operator");
        }
        tvQuestion.setText(a + " " + op + " " + b + " = ?");
        if (easyMode) {
            genWrongAnswer();
        }


    }

    private int genWrongMCAnswer() {
        int opIndex = (int) (Math.random() * operator.length);
        String op = operator[opIndex];
        int a = ((int) (Math.random() * 100)) + 1, b;
        int answer;
        switch (op) {
            case "+":
                b = ((int) (Math.random() * 100)) + 1;
                answer = a + b;
                break;
            case "-":
                b = ((int) (Math.random() * a)) + 1;
                answer = a - b;
                break;
            case "*":
                b = ((int) (Math.random() * 100)) + 1;
                answer = a * b;
                break;
            case "/":
                a = ((int) (Math.random() * 50)) + 1;
                b = ((int) (Math.random() * ((int) (100 / a)))) + 1;
                answer = a;
                a *= b;
                break;
            default:
                throw new RuntimeException("Invalid operator");
        }
        return answer;
    }

    private void genWrongAnswer() {
        ArrayList<Integer> answers = new ArrayList<Integer>(4);
        answers.add(correctAnswer);
        for (int i = 1; i < 4; i++) {
            answers.add(genWrongMCAnswer());
        }
        Collections.shuffle(answers);
        btnMC1.setText(String.valueOf(answers.get(0)));
        btnMC2.setText(String.valueOf(answers.get(1)));
        btnMC3.setText(String.valueOf(answers.get(2)));
        btnMC4.setText(String.valueOf(answers.get(3)));
    }

}