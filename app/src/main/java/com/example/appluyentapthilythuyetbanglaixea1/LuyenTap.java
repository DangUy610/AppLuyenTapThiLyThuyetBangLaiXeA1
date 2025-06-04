package com.example.appluyentapthilythuyetbanglaixea1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LuyenTap extends AppCompatActivity {
    int correctAnswerIndex = -1;  // 0 -> A, 1 -> B, 2 -> C, 3 -> D
    int currentQuestionIndex = 0;
    int answerQuantity = 0;
    TextView questionText;
    RadioGroup answersGroup;
    RadioButton answer1, answer2, answer3, answer4;
    Button nextQuestionBtn,btnQuaylaiTopic;
    ImageView questionImage;
    // Mở DB
    SQLiteDatabase db;
    String sqlcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luyen_tap);
        // Khởi tạo DBHelper
        copyDatabaseIfNeeded(this);
        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        //mapping
        questionText = findViewById(R.id.questionText);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        answersGroup = findViewById(R.id.answersGroup);
        nextQuestionBtn = findViewById(R.id.nextQuestionBtn);
        questionImage = findViewById(R.id.questionImage);
        btnQuaylaiTopic =findViewById(R.id.btn_quaylai_topic);
        btnQuaylaiTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LuyenTap.this, TopicLuyenTap.class);
                startActivity(intent);
            }
        });
        // Nhận chủ đề từ Intent
        String topic = getIntent().getStringExtra("topic");
        if (topic == null) topic = "";

        switch (topic) {
            case "LuatGTDB":
            {
                sqlcode = "SELECT id, content, image_path FROM Question WHERE topic = 'LuatGTDB' ORDER BY id LIMIT 1 OFFSET ";
                nextQuestionBtn.setOnClickListener(v -> {currentQuestionIndex++;
                loadQuestion(currentQuestionIndex, sqlcode);});
                loadQuestion(0,sqlcode);
            }
                break;
            case "BienBao":
            {
                sqlcode = "SELECT id, content, image_path FROM Question WHERE topic = 'BienBao' ORDER BY id LIMIT 1 OFFSET ";
                nextQuestionBtn.setOnClickListener(v -> {currentQuestionIndex++;
                    loadQuestion(currentQuestionIndex, sqlcode);});
                loadQuestion(0,sqlcode);
            }
                break;
            case "SaHinh":
            {
                sqlcode = "SELECT id, content, image_path FROM Question WHERE topic = 'SaHinh' ORDER BY id LIMIT 1 OFFSET ";
                nextQuestionBtn.setOnClickListener(v -> {currentQuestionIndex++;
                    loadQuestion(currentQuestionIndex, sqlcode);});
                loadQuestion(0,sqlcode);
            }
                break;
            case "Tonghop":
            {
                sqlcode = "SELECT id, content, image_path FROM Question ORDER BY id LIMIT 1 OFFSET ";
                nextQuestionBtn.setOnClickListener(v -> {currentQuestionIndex++;
                    loadQuestion(currentQuestionIndex, sqlcode);});
                loadQuestion(0,sqlcode);
            }
                break;
            default:
                // xử lý mặc định hoặc báo lỗi nếu cần
                break;
        }
    }
    private void loadQuestion(int index,String sqlcode) {
        RadioButton[] answerButtons = {answer1, answer2, answer3, answer4};
        // Reset UI
        answersGroup.clearCheck();
        for (RadioButton btn : answerButtons) {
            btn.setEnabled(true);
            btn.setTextColor(Color.BLACK);
            btn.setText("");
        }
        nextQuestionBtn.setEnabled(false);
        questionImage.setImageDrawable(null);
        questionImage.setVisibility(View.GONE);
        //test hiển thị câu hỏi
        Cursor questionCursor = db.rawQuery(sqlcode+index, null);
        if (questionCursor.moveToFirst()) {
            int questionId = questionCursor.getInt(questionCursor.getColumnIndex("id"));
            String question = questionCursor.getString(questionCursor.getColumnIndex("content"));
            String imagePath = questionCursor.getString(questionCursor.getColumnIndex("image_path"));
//            Log.d("image_path", "id= "+ questionId);
//            Log.d("IMAGE_PATH", "Path from DB: " + imagePath);
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    InputStream is = getAssets().open(imagePath);
                    Drawable drawable = Drawable.createFromStream(is, null);
                    questionImage.setImageDrawable(drawable);
                    questionImage.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    questionImage.setVisibility(View.GONE);
                }
            } else {
                questionImage.setVisibility(View.GONE);
            }
            questionText.setText("Câu số "+questionId+": "+question);

            //Hiển thị câu trả lời
            Cursor answerCursor = db.rawQuery(
                    "SELECT content, is_correct FROM Answer WHERE question_id = ? ORDER BY id ASC",
                    new String[]{String.valueOf(questionId)}
            );
            answerQuantity = answerCursor.getCount();
            if (answerQuantity<4){
                for(int i = answerQuantity; i<4; i++){
                    answerButtons[i].setEnabled(false);
                    answerButtons[i].setVisibility(View.GONE);
                }
            }
            int answerIndex = 0;
            while (answerCursor.moveToNext() && answerIndex < 4) {
                String answerText = answerCursor.getString(answerCursor.getColumnIndex("content"));
                int isCorrect = answerCursor.getInt(answerCursor.getColumnIndex("is_correct"));
                answerButtons[answerIndex].setText(answerText);
                if (isCorrect == 1) {
                    correctAnswerIndex = answerIndex;
                }
                answerIndex++;
            }
            answersGroup.setOnCheckedChangeListener(null);
            answersGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // Vô hiệu hoá group để tránh chọn lại
                    for (int i = 0; i < answerButtons.length; i++) {
                        answerButtons[i].setEnabled(false);
                    }

                    // Xác định lựa chọn người dùng
                    int selectedIndex = -1;
                    for (int i = 0; i < answerButtons.length; i++) {
                        if (answerButtons[i].getId() == checkedId) {
                            selectedIndex = i;
                            break;
                        }
                    }
                    // Nếu không tìm thấy chỉ số hợp lệ thì dừng lại
                    if (selectedIndex == -1) return;


                    // Tô màu: Xanh lá nếu đúng, đỏ nếu sai
                    if (selectedIndex == correctAnswerIndex) {
                        answerButtons[selectedIndex].setTextColor(Color.GREEN);
                    } else {
                        answerButtons[selectedIndex].setTextColor(Color.RED);
                        answerButtons[correctAnswerIndex].setTextColor(Color.GREEN);
                    }
                    nextQuestionBtn.setEnabled(true);
                }
            });

        } else {
            questionText.setText("Đã hoàn thành toàn bộ câu hỏi.");
            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i].setEnabled(false);
                answerButtons[i].setVisibility(View.GONE);
            }
        }
        questionCursor.close();
    }
    private void copyDatabaseIfNeeded(Context context) {
        File dbFile = context.getDatabasePath("sample_questions.db");
        if (!dbFile.exists()) {
            dbFile.getParentFile().mkdirs();
            try (InputStream is = context.getAssets().open("sample_questions.db");
                 OutputStream os = new FileOutputStream(dbFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}