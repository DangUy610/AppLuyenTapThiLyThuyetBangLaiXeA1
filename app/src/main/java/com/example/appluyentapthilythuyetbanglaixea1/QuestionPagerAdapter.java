package com.example.appluyentapthilythuyetbanglaixea1;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class QuestionPagerAdapter extends FragmentStateAdapter {
    private List<Question> questions;
    public QuestionPagerAdapter(FragmentActivity fa, List<Question> questions) {
        super(fa);
        this.questions = questions;
    }
    @Override public int getItemCount() { return questions.size(); }
    @Override public Fragment createFragment(int pos) {
        return QuestionFragment.newInstance(pos);
    }
}