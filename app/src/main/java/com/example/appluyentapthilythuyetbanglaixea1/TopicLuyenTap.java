package com.example.appluyentapthilythuyetbanglaixea1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopicLuyenTap extends AppCompatActivity {
    Button btnLuat, btnBienBao, btnSaHinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_luyen_tap);

        btnLuat      = findViewById(R.id.btn_topic_luat);
        btnBienBao   = findViewById(R.id.btn_topic_bienbao);
        btnSaHinh    = findViewById(R.id.btn_topic_sahinh);

        btnLuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicLuyenTap.this, LuyenTap.class);
                intent.putExtra("topic", "luat");
                startActivity(intent);
            }
        });

        btnBienBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicLuyenTap.this, LuyenTap.class);
                intent.putExtra("topic", "bienbao");
                startActivity(intent);
            }
        });
        
        btnSaHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicLuyenTap.this, LuyenTap.class);
                intent.putExtra("topic", "sahinh");
                startActivity(intent);
            }
        });
    }
}
