package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button startButton;
    TextView sumTextView;
    TextView answerTextView;
    char[] operators = {'+', '-', '*'};
    int ans;
    int a, b , op;
    int locationOfCorrectAnswer;
    int score = 0;
    int numberOfQuestions = 0;
    ArrayList<Integer> answers = new ArrayList<Integer>();
    Button btn0, btn1, btn2, btn3;
    TextView pointsTextView;
    TextView timeTextView;
    CountDownTimer timer;
    boolean isRunning = false;
    Button playAgain;
    RelativeLayout gameView;
    MediaPlayer mp, correctAnswer, wrongAnswer;

    public void start(View view){
        startButton.setVisibility(View.INVISIBLE);
        gameView.setVisibility(View.VISIBLE);
        generateQuestion();
        startCountDown();
        mp.start();
    }

    public String getQuestionString(int op, int a, int b) {
        return Integer.toString(a) + " " + operators[op] + " " + Integer.toString(b);
    }

    public int getQuestionAnswer(int op, int a, int b) {
        switch (op){
            case 0 : return a + b;
            case 1 : return a - b;
            case 2 : return a * b;
            default: return 0;
        }
    }

    @SuppressLint("SetTextI18n")
    public void generateQuestion() {
        Random rand = new Random();


        op = rand.nextInt(3);

        a = rand.nextInt(31);
        b = rand.nextInt(31);

        sumTextView.setText(getQuestionString(op, a, b));

        locationOfCorrectAnswer = rand.nextInt(4);
        int wrongAnswer, noToAdd;
        int correctAnswer = getQuestionAnswer(op, a, b);

        for(int i = 0 ; i < 4; i++){
            if(i == locationOfCorrectAnswer){
                noToAdd = correctAnswer;
            }else{
                wrongAnswer = rand.nextInt(1000);
                boolean toAdd = correctAnswer != wrongAnswer;;
                if(answers.size() > 0){
                    for(int q : answers){
                        if(q == wrongAnswer ){
                            toAdd = false;
                            break;
                        }
                    }// for loop
                }
                while(!toAdd){
                    wrongAnswer = rand.nextInt(1000);
                    toAdd = correctAnswer != wrongAnswer;

                    if(answers.size() > 0){
                        for(int q : answers){
                            if(q == wrongAnswer ){
                                toAdd = false;
                                break;
                            }
                        }// for loop
                    }

                } // else statement

                noToAdd = wrongAnswer;
            }

            answers.add(noToAdd);
        } // for loop

        if(correctAnswer >= 100){
            btn0.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
            btn1.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
            btn2.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
            btn3.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
        }else{
            btn0.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            btn1.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            btn2.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            btn3.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
        }

        btn0.setText(Integer.toString(answers.get(0)));
        btn1.setText(Integer.toString(answers.get(1)));
        btn2.setText(Integer.toString(answers.get(2)));
        btn3.setText(Integer.toString(answers.get(3)));
        answers.clear();
    }

    @SuppressLint("SetTextI18n")
    public void chooseAnswer(View v) {
        String tag = (String) v.getTag();
        String ansLocation = Integer.toString(locationOfCorrectAnswer);

        if(tag.equals(ansLocation)){
            score++;
            answerTextView.setText("Correct!");
            correctAnswer.start();
        }else{
            answerTextView.setText("Wrong!");
            wrongAnswer.start();
        }

        numberOfQuestions++;
        generateQuestion();
        String pointTextViewText = Integer.toString(score)+ "/" + Integer.toString(numberOfQuestions) ;
        pointsTextView.setText(pointTextViewText);
    }


    public void startCountDown() {
        timer = new CountDownTimer(60100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timerText = Integer.toString((int)millisUntilFinished/1000) + "s";
                timeTextView.setText(timerText);
                isRunning = true;
            }

            @Override
            public void onFinish() {
                numberOfQuestions++;
                generateQuestion();
                answerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                answerTextView.setText("Finished ! Your Score : " + pointsTextView.getText());
                playAgain.setVisibility(View.VISIBLE);
                isRunning = false;
                mp.start();
            }
        };
        timer.start();
    }

    public void playAgain(View v) {
        score = 0;
        numberOfQuestions = 0;
        generateQuestion();
        answerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
        answerTextView.setText("");
        pointsTextView.setText("0/0");
        playAgain.setVisibility(View.INVISIBLE);
        startCountDown();
        mp.start();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.startButton);
        sumTextView = (TextView) findViewById(R.id.sumTextView);
        btn0 = (Button) findViewById(R.id.button0);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        answerTextView = (TextView) findViewById(R.id.answerTextView);
        pointsTextView = (TextView) findViewById(R.id.pointsTextView);
        timeTextView = (TextView) findViewById(R.id.timerTextView);
        playAgain = (Button) findViewById(R.id.playAgainButton);
        playAgain.setVisibility(View.INVISIBLE);
        gameView = (RelativeLayout) findViewById(R.id.gameView);
        mp = MediaPlayer.create(this, R.raw.sound);
        correctAnswer = MediaPlayer.create(this, R.raw.correct);
        wrongAnswer = MediaPlayer.create(this, R.raw.wrong);

    }
}

