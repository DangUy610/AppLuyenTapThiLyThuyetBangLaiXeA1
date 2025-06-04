package com.example.appluyentapthilythuyetbanglaixea1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "sample_questions.db";
    private static final int DB_VERSION = 1;
    private final Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Không tạo bảng ở đây vì đã dùng file .db sẵn
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Tuỳ nhu cầu, ví dụ drop table rồi copy lại file .db mới
    }

    // Truy cập file .db đã được chép từ assets
    @Override
    public SQLiteDatabase getReadableDatabase() {
        copyDatabaseIfNeeded();
        return super.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        copyDatabaseIfNeeded();
        return super.getWritableDatabase();
    }

    private void copyDatabaseIfNeeded() {
        File dbFile = context.getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            dbFile.getParentFile().mkdirs();
            try (InputStream is = context.getAssets().open(DB_NAME);
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
