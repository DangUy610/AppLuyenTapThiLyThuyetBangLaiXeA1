package com.example.appluyentapthilythuyetbanglaixea1;

import java.util.List;

//class này được tạo ra chỉ để dùng trong activity ThiThu
public class Question {
    private int id;
    private String text;
    private List<String> options;
    private int correctIndex;
    private boolean isCritical;
    private String imagePath;

    public Question(int id, String text, List<String> options, int correctIndex, boolean isCritical, String imagePath) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
        this.isCritical = isCritical;
        this.imagePath = imagePath;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }

    public boolean isCritical() {
        return isCritical;
    }

    public void setCritical(boolean critical) {
        isCritical = critical;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Optional helper: get the correct answer text
     */
    public String getCorrectAnswer() {
        if (options != null && correctIndex >= 0 && correctIndex < options.size()) {
            return options.get(correctIndex);
        }
        return null;
    }
}

