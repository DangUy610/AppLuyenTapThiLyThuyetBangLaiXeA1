package com.example.appluyentapthilythuyetbanglaixea1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThiThu extends AppCompatActivity {
    private TextView tvTimer, tvQuestion;
    private ImageView ivQuestionImage;
    private RadioGroup rgAnswers;
    private Button btnNext;

    private List<Question> questions;
    private int currentIndex = 0;
    private int correctCount = 0;
    private boolean failedOnFatal = false;

    private CountDownTimer examTimer;
    private static final long DURATION_MS = 19 * 60 * 1000; // 19 minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thi_thu);

        tvTimer = findViewById(R.id.tvTimer);
        tvQuestion = findViewById(R.id.tvQuestion);
        ivQuestionImage = findViewById(R.id.ivQuestionImage);
        rgAnswers = findViewById(R.id.rgAnswers);
        btnNext = findViewById(R.id.btnNext);

        // Load 25 random questions including exactly 1 fatal
        DBHelper db = new DBHelper(this);
        questions = db.getRandomQuestionsWithFatal(25,1);
        Collections.shuffle(questions);

        startTimer();
        showCurrentQuestion();

        btnNext.setOnClickListener(v -> checkAnswerAndAdvance());
    }

    private void startTimer() {
        examTimer = new CountDownTimer(DURATION_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                finishExam();
            }
        };
        examTimer.start();
    }

    private void showCurrentQuestion() {
        Question q = questions.get(currentIndex);
        tvQuestion.setText((currentIndex + 1) + ". " + q.getText());

        // Hiển thị ảnh nếu có đường dẫn
        String imagePath = q.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                InputStream is = getAssets().open(imagePath);
                Drawable drawable = Drawable.createFromStream(is, null);
                ivQuestionImage.setImageDrawable(drawable);
                ivQuestionImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                ivQuestionImage.setVisibility(View.GONE);
            }
        } else {
            ivQuestionImage.setVisibility(View.GONE);
        }

        rgAnswers.removeAllViews();
        for (int i = 0; i < q.getOptions().size(); i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(q.getOptions().get(i));
            rb.setId(i);
            rgAnswers.addView(rb);
        }
    }

    private void checkAnswerAndAdvance() {
        int selectedId = rgAnswers.getCheckedRadioButtonId();
        if (selectedId < 0) {
            // no selection
            return;
        }
        Question q = questions.get(currentIndex);
        if (selectedId == q.getCorrectIndex()) {
            correctCount++;
        } else if (q.isCritical()) {
            failedOnFatal = true;
        }

        currentIndex++;
        if (currentIndex < questions.size()) {
            showCurrentQuestion();
        } else {
            examTimer.cancel();
            finishExam();
        }
    }

    private void finishExam() {
        boolean passed = !failedOnFatal && correctCount >= 21;
        String title = passed ? "Bạn đã vượt qua!" : "Bạn đã rớt";
        String msg = String.format("Đúng: %d/25\n%s câu điểm liệt %s.",
                correctCount,
                "câu điểm liệt",
                failedOnFatal ? "bị sai" : "không sai");

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }
}