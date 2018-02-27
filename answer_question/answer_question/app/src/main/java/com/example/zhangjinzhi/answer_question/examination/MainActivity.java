package com.example.zhangjinzhi.answer_question.examination;

import com.example.zhangjinzhi.answer_question.R;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView stuNameTextView;
    private TextView stuNumTextView;
    private Button startQuizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stuNameTextView = (TextView)this.findViewById(R.id.stuName);
        stuNumTextView = (TextView)this.findViewById(R.id.stuNum);
        startQuizButton = (Button)this.findViewById(R.id.startQuiz);

        // I use setText to set the text of stuNameTextView
        // and stuNumTextView according to my name and student ID
        stuNameTextView.setText("Name: ZhangJinzhi");
        stuNumTextView.setText("Student ID: 3035455008");
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( MainActivity.this,ExaminationFrame.class));
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

