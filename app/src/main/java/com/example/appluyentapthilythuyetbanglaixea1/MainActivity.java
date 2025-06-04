package com.example.appluyentapthilythuyetbanglaixea1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    Button btnThithu, btnLuyentap;
    Switch swDarkmode;
    private static final String PREFS_NAME = "settings";
    private static final String KEY_DARK_MODE = "dark_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnThithu   = findViewById(R.id.btn_thi_thu);
        btnLuyentap = findViewById(R.id.btn_luyen_tap);
        swDarkmode  = findViewById(R.id.switch_dark_mode);

        // 1. Đọc trạng thái Dark Mode đã lưu từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkSaved = prefs.getBoolean(KEY_DARK_MODE, false);
        // 2. Trước khi setChecked, tạm ngắt listener để không kích hoạt khi khởi tạo
        swDarkmode.setOnCheckedChangeListener(null);
        swDarkmode.setChecked(isDarkSaved);

        // 3. Cập nhật ngay theme tương ứng (nếu cần) dựa trên isDarkSaved
        //    → Đây chỉ cần gọi một lần khi Activity vừa khởi tạo
        AppCompatDelegate.setDefaultNightMode(
                isDarkSaved
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );

        // 4. Bây giờ gắn listener cho Switch
        swDarkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 4.1. Lấy currentNightMode hiện tại từ Configuration
                int currentNightMode = getResources().getConfiguration().uiMode
                        & Configuration.UI_MODE_NIGHT_MASK;
                boolean currentlyDark = (currentNightMode == Configuration.UI_MODE_NIGHT_YES);

                // 4.2. Nếu trạng thái switch (isChecked) trùng với currentNightMode thì không làm gì
                if (isChecked == currentlyDark) {
                    return;
                }

                // 4.3. Lưu trạng thái mới
                prefs.edit()
                        .putBoolean(KEY_DARK_MODE, isChecked)
                        .apply();

                // 4.4. Thực sự đổi theme
                AppCompatDelegate.setDefaultNightMode(
                        isChecked
                                ? AppCompatDelegate.MODE_NIGHT_YES
                                : AppCompatDelegate.MODE_NIGHT_NO
                );
                // Sau khi gọi setDefaultNightMode, hệ thống sẽ recreate Activity.
                // Khi Activity khởi động lại, nó sẽ vào onCreate → setChecked lại theo prefs → listener sẽ không kích vì bước 4.2
            }
        });

        // Thiết lập sự kiện cho các nút
        btnThithu.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ThiThu.class);
            startActivity(intent);
        });
        btnLuyentap.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TopicLuyenTap.class);
            startActivity(intent);
        });
    }
}
