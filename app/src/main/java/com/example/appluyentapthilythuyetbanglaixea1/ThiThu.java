package com.example.appluyentapthilythuyetbanglaixea1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import androidx.viewpager2.widget.ViewPager2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThiThu extends AppCompatActivity
        implements QuestionFragment.QuestionAnswerListener {
    private TextView tvTimer;
    private ViewPager2 viewPager;
    private Button btnPrev;
    private Button btnSubmit;
    private Button btnNext;
    private Button btnHome;
    private List<Question> questions;
    private QuestionPagerAdapter adapter;
    private int[] userSelections;
    private CountDownTimer examTimer;
    private boolean examFinished = false;
    private static final long DURATION_MS = 19 * 60 * 1000;
    private int currentPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thi_thu);

        // Init views
        tvTimer   = findViewById(R.id.tvTimer);
        viewPager = findViewById(R.id.viewPager);
        btnPrev   = findViewById(R.id.btnPrev);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnNext   = findViewById(R.id.btnNext);
        btnHome   = findViewById(R.id.btnHome);

        // Load & shuffle
        DBHelper db = new DBHelper(this);
        questions = db.getRandomQuestionsWithFatal(25,1);
        Collections.shuffle(questions);

        // Khởi tạo selections
        userSelections = new int[questions.size()];
        Arrays.fill(userSelections, -1);

        // Adapter cho ViewPager2
        adapter = new QuestionPagerAdapter(this, questions);
        viewPager.setAdapter(adapter);

        // Cập nhật currentPos khi vuốt trang
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPos = position;
            }
        });

        // Nút Previous
        btnPrev.setOnClickListener(v -> {
            if (currentPos > 0) viewPager.setCurrentItem(--currentPos);
        });

        // Nút Next
        btnNext.setOnClickListener(v -> {
            if (currentPos < questions.size() - 1) viewPager.setCurrentItem(++currentPos);
        });

        // Nút Submit bài thi
        btnSubmit.setOnClickListener(v -> {
            finishExam();
        });

        // Nút Home
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ThiThu.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        startTimer();
    }

    private void startTimer() {
        examTimer = new CountDownTimer(DURATION_MS, 1000) {
            @Override public void onTick(long millis) {
                long m = millis / 60000;
                long s = (millis % 60000) / 1000;
                tvTimer.setText(String.format("%02d:%02d", m, s));
            }
            @Override public void onFinish() {
                finishExam();
            }
        }.start();
    }

    @Override
    public void onAnswerSelected(int position, int selectedOption) {
        userSelections[position] = selectedOption;
    }

    public List<Question> getQuestions() { return questions; }
    public int[] getUserSelections() { return userSelections; }
    public boolean isExamFinished() { return examFinished; }

    private void finishExam() {
        // Dừng timer & disable nút submit
        examTimer.cancel();
        examFinished = true;
        btnSubmit.setEnabled(false);

        // Thay adapter để ép recreate fragments
        viewPager.setAdapter(adapter);
        // Quay lại fragment hiện tại (không animation)
        viewPager.setCurrentItem(currentPos, false);

        // Tính điểm
        int correctCount = 0;
        boolean failedOnFatal = false;
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            int sel = userSelections[i];
            if (sel == q.getCorrectIndex()) correctCount++;
            else if (q.isCritical()) failedOnFatal = true;
        }
        boolean passed = !failedOnFatal && correctCount >= 21;

        // Hiển thị kết quả
        new AlertDialog.Builder(this)
                .setTitle(passed ? "Bạn đã vượt qua!" : "Bạn đã rớt")
                .setMessage(String.format("Đúng: %d/25/Điểm liệt: %s",
        correctCount,
                (failedOnFatal ? "bị sai" : "không sai")))
                .setPositiveButton("OK", null)
                .show();
    }
}