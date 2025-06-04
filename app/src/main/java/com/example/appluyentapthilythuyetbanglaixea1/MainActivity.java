package com.example.appluyentapthilythuyetbanglaixea1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    Button btnThithu, btnLuyentap;
    Switch swDarkmode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnThithu = findViewById(R.id.btn_thi_thu);
        btnLuyentap = findViewById(R.id.btn_luyen_tap);
        swDarkmode = findViewById(R.id.switch_dark_mode);
        btnThithu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ThiThu.class);
                startActivity(intent);
            }
        });
        btnLuyentap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TopicLuyenTap.class);
                startActivity(intent);
            }
        });
    }
}