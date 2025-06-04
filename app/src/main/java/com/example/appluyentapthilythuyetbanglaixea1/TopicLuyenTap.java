package com.example.appluyentapthilythuyetbanglaixea1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopicLuyenTap extends AppCompatActivity {
    Button btnLuat, btnBienBao, btnSaHinh, btnQuaylai, btnTonghop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_luyen_tap);

        btnLuat      = findViewById(R.id.btn_topic_luat);
        btnBienBao   = findViewById(R.id.btn_topic_bienbao);
        btnSaHinh    = findViewById(R.id.btn_topic_sahinh);
        btnQuaylai   = findViewById(R.id.btn_quaylai);
        btnTonghop   = findViewById(R.id.btn_topic_tonghop);

        btnTonghop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicLuyenTap.this, LuyenTap.class);
                intent.putExtra("topic", "Tonghop");
                startActivity(intent);
            }
        });

        btnLuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicLuyenTap.this, LuyenTap.class);
                intent.putExtra("topic", "LuatGTDB");
                startActivity(intent);
            }
        });

        btnBienBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicLuyenTap.this, LuyenTap.class);
                intent.putExtra("topic", "BienBao");
                startActivity(intent);
            }
        });

        btnSaHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicLuyenTap.this, LuyenTap.class);
                intent.putExtra("topic", "SaHinh");
                startActivity(intent);
            }
        });
        btnQuaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
