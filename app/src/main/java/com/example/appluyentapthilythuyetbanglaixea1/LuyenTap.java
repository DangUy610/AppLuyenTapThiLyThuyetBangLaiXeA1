package com.example.appluyentapthilythuyetbanglaixea1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LuyenTap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luyen_tap);

        // Nhận chủ đề từ Intent
        String topic = getIntent().getStringExtra("topic");
        if (topic == null) topic = "";

        switch (topic) {
            case "luat":
                // load câu hỏi về Luật GTĐB
                break;
            case "bienbao":
                // load câu hỏi về Biển báo
                break;
            case "sahinh":
                // load câu hỏi về Sa hình
                break;
            default:
                // xử lý mặc định hoặc báo lỗi nếu cần
                break;
        }
    }
}