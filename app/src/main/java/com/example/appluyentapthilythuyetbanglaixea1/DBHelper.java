package com.example.appluyentapthilythuyetbanglaixea1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ly_thuyet_a1_200.db";
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
    public List<Question> getRandomQuestionsWithCritical(int n, int m) {
        SQLiteDatabase db = getReadableDatabase();
        List<Question> list = new ArrayList<>();

        // m câu điểm liệt
        Cursor c1 = db.rawQuery(
                "SELECT id, content, is_critical, image_path FROM Question " +
                        "WHERE is_critical=1 ORDER BY RANDOM() LIMIT "+m, null);
        if (c1.moveToFirst()) {
            list.add(loadTestQuestion(db, c1));
        }
        c1.close();

        // (n-m) câu thường
        Cursor c2 = db.rawQuery(
                "SELECT id, content, is_critical, image_path FROM Question " +
                        "WHERE is_critical=0 ORDER BY RANDOM() LIMIT " + (n - m),
                null);
        while (c2.moveToNext()) {
            list.add(loadTestQuestion(db, c2));
        }
        c2.close();

        return list;
    }
    private Question loadTestQuestion(SQLiteDatabase db, Cursor qc) {
        int qId        = qc.getInt(qc.getColumnIndexOrThrow("id"));
        String text    = qc.getString(qc.getColumnIndexOrThrow("content"));
        boolean fatal  = qc.getInt(qc.getColumnIndexOrThrow("is_critical")) == 1;
        String imagePath = qc.getString(qc.getColumnIndexOrThrow("image_path"));

        // Lấy các đáp án
        Cursor ac = db.rawQuery(
                "SELECT content, is_correct FROM Answer WHERE question_id=?",
                new String[]{ String.valueOf(qId) });
        List<String> options = new ArrayList<>();
        int correctIdx = -1, idx = 0;
        while (ac.moveToNext()) {
            options.add(ac.getString(ac.getColumnIndexOrThrow("content")));
            if (ac.getInt(ac.getColumnIndexOrThrow("is_correct")) == 1) {
                correctIdx = idx;
            }
            idx++;
        }
        ac.close();

        return new Question(qId, text, options, correctIdx, fatal, imagePath);
    }

}
