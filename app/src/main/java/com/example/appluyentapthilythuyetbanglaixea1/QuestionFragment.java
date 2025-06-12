package com.example.appluyentapthilythuyetbanglaixea1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {
    private static final String ARG_POS = "arg_pos";

    public interface QuestionAnswerListener {
        void onAnswerSelected(int position, int selectedOption);
    }

    private int position;
    private QuestionAnswerListener callback;
    private TextView tvQuestion;
    private ImageView ivQuestionImage;
    private RadioGroup rgAnswers;


    public static QuestionFragment newInstance(int pos) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POS, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof QuestionAnswerListener) {
            callback = (QuestionAnswerListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement QuestionAnswerListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        ivQuestionImage = view.findViewById(R.id.ivQuestionImage);
        rgAnswers = view.findViewById(R.id.rgAnswers);

        // Lấy position
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POS);
        }
        ThiThu activity = (ThiThu) getActivity();
        Question q = activity.getQuestions().get(position);

        // Hiển thị nội dung câu hỏi
        tvQuestion.setText((position + 1) + ". " + q.getText());

        // Hiển thị ảnh nếu có
        String imgPath = q.getImagePath();
        if (imgPath != null && !imgPath.isEmpty()) {
            try (InputStream is = activity.getAssets().open(imgPath)) {
                Drawable drawable = Drawable.createFromStream(is, null);
                ivQuestionImage.setImageDrawable(drawable);
                ivQuestionImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                ivQuestionImage.setVisibility(View.GONE);
            }
        } else {
            ivQuestionImage.setVisibility(View.GONE);
        }

        // Tạo và chọn lại đáp án
        List<String> options = q.getOptions();
        for (int i = 0; i < options.size(); i++) {
            RadioButton rb = new RadioButton(requireContext());
            rb.setId(i);
            rb.setText(options.get(i));
            rgAnswers.addView(rb);
        }
        // Prefill nếu đã chọn
        int sel = activity.getUserSelections()[position];
        if (sel >= 0) {
            rgAnswers.check(sel);
        }

        // Lắng nghe chọn đáp án
        rgAnswers.setOnCheckedChangeListener((group, checkedId) -> {
            callback.onAnswerSelected(position, checkedId);
        });

        // Nếu đã thi xong, disable và highlight
        if (activity.isExamFinished()) {
            sel = activity.getUserSelections()[position];
            for (int i = 0; i < rgAnswers.getChildCount(); i++) {
                RadioButton rb = (RadioButton) rgAnswers.getChildAt(i);
                rb.setEnabled(false);
                // highlight đáp án đúng
                if (i == q.getCorrectIndex()) {
                    rb.setBackgroundColor(Color.parseColor("#4CAF50"));
                    rb.setTextColor(Color.BLACK);
                }
                // highlight đáp án sai nếu user chọn
                if (i == sel && sel != q.getCorrectIndex()) {
                    rb.setBackgroundColor(Color.parseColor("#FF0000"));
                    rb.setTextColor(Color.WHITE);
                }
            }
        }

        return view;
    }
}