package com.example.zhangjinzhi.answer_question.examination;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.zhangjinzhi.answer_question.R;

import com.example.zhangjinzhi.answer_question.questiongenerator.Question;
import com.example.zhangjinzhi.answer_question.questiongenerator.QuestionManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExaminationFrame extends AppCompatActivity implements View.OnClickListener,TextWatcher,ViewSwitcher.ViewFactory{

    private TextView showCorrectAnswerTextView;
    private EditText inputFirstAnswerExidtext,inputSecondAnswerExidtext;
    private Button submitButton,nextQuestionButton;
    private TextSwitcher questionTextSwitcher,timeTextview;
    private ArrayList<Question> questionList = new ArrayList<>();
    private int questionIndex = 0;
    private String questionBody;
    private Question question;
    private int answerLength;
    private long startTime = 0, endTime = 0;
    private List<Long> timeList = new ArrayList<>();
    private boolean hasSubmit = false;
    private int correctNum  = 0,wrongNum = 0,giveupNum = 0;
    private Context mContext;
    private Timer timer;

    private MediaPlayer mediaPlayer;

    private MediaPlayer mPlayer, mNextPlayer;
    int mPlayResId = R.raw.backmusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        mContext = this;
        showCorrectAnswerTextView = (TextView) this.findViewById(R.id.showCorrectAnswer);
        timeTextview = (TextSwitcher) this.findViewById(R.id.showTime);
        questionTextSwitcher = (TextSwitcher)this.findViewById(R.id.showQuestion);
        inputFirstAnswerExidtext = (EditText) this.findViewById(R.id.inputFirstAnswer);
        inputSecondAnswerExidtext = (EditText)this.findViewById(R.id.inputSecondAnswer);
        inputFirstAnswerExidtext.addTextChangedListener(this);
        inputSecondAnswerExidtext.addTextChangedListener(this);
        submitButton = (Button)this.findViewById(R.id.submit);
        nextQuestionButton = (Button)this.findViewById(R.id.nextQuestion);
        submitButton.setOnClickListener(this);
        nextQuestionButton.setOnClickListener(this);
        nextQuestionButton.setEnabled(false);
        generateQuestion();
        setSwitchAnimation();
        updateQuestion();
        updateTime();
        hasSubmit = true;
        onStart();
    }


    private void setSwitchAnimation() {
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        timeTextview.setFactory(this);
        timeTextview.setInAnimation(in);
        timeTextview.setOutAnimation(out);
        questionTextSwitcher.setFactory(this);
        questionTextSwitcher.setInAnimation(in);
        questionTextSwitcher.setOutAnimation(out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startPlayBackgroundMusic_version2();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayBackgroundMusic();
        stopUpdateTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:

                submitAnswer();
                break;
            case R.id.nextQuestion:

                updateQuestion();
                break;

            default:
                break;
        }
    }

    private void submitAnswer() {

        if (answerLength == 1) {
            double answer1;
            String answerPre1 = inputFirstAnswerExidtext.getText().toString().trim();

            if (answerPre1.length() == 0) {
                giveupNum ++;
                showCorrectAnswerTextView.setText("Unfortunately!\nyou did not answer!\nthe answer(s) should be:\n" + question.getAnswer().get(0));
                submitButtonProcess();
            } else if (answerPre1.length() != 0) {

                try {
                    answer1 = Double.parseDouble(answerPre1);
                } catch (Exception e) {
                    Toast.makeText(this,"Incorrect Format!\nplease enter again",Toast.LENGTH_SHORT).show();
                    return;
                }

                DecimalFormat df = new DecimalFormat("#.00");
                answer1 = Double.valueOf(df.format(answer1));

                if (areEqual(question.getAnswer().get(0), answer1)) {
                    showCorrectAnswerTextView.setText("Right!\nthe answer(s) should be:\n" + question.getAnswer().get(0));
                    correctNum++;
                    submitButtonProcess();
                } else {
                    showCorrectAnswerTextView.setText("Wrong!\nthe answer(s) should be:\n" + question.getAnswer().get(0));
                    wrongNum++;
                    submitButtonProcess();
                }
            }

        }else if(answerLength == 2){

            String answerPre1 = inputFirstAnswerExidtext.getText().toString().trim();
            String answerPre2 = inputSecondAnswerExidtext.getText().toString().trim();

            if (answerPre1.length() == 0 && answerPre2.length() == 0) {
                giveupNum++;
                showCorrectAnswerTextView.setText("Unfortunately!\nyou did not answer!\nthe answer(s) should be:\n(1) " + question.getAnswer().get(0)+"  (2) "+ question.getAnswer().get(1));
                submitButtonProcess();
            }else if (answerPre1.length() != 0 && answerPre2.length() == 0) {
                double answer1;
                List<Double> answerList = new ArrayList<>();
                answerList.addAll(question.getAnswer());
                try {
                    answer1 = Double.parseDouble(answerPre1);
                } catch (Exception e) {
                    Toast.makeText(this,"Incorrect Format!\nplease enter again",Toast.LENGTH_SHORT).show();
                    return;
                }
                DecimalFormat df = new DecimalFormat("#.00");
                answer1 = Double.valueOf(df.format(answer1));
                double tempAnswer1 = answerList.get(0);
                double tempAnswer2 = answerList.get(0);
                if (areEqual(tempAnswer1, answer1) || areEqual(tempAnswer2, answer1)) {
                    //答案不完整
                }
                wrongNum++;
                showCorrectAnswerTextView.setText("Wrong!\nthe answer(s) are:\n(1)" + question.getAnswer().get(0)+" (2)"+ question.getAnswer().get(1));
                submitButtonProcess();
            }else if (answerPre1.length() == 0 && answerPre2.length() != 0) {
                double answer2;
                List<Double> answerList = new ArrayList<>();
                answerList.addAll(question.getAnswer());
                try {
                    answer2 = Double.parseDouble(answerPre2);
                } catch (Exception e) {
                    Toast.makeText(this,"Incorrect Format!\nplease enter again",Toast.LENGTH_SHORT).show();
                    return;
                }
                DecimalFormat df2 = new DecimalFormat("#.00");
                answer2 = Double.valueOf(df2.format(answer2));
                double tempAnswer1 = answerList.get(0);
                double tempAnswer2 = answerList.get(0);
                if (areEqual(tempAnswer1, answer2) || areEqual(tempAnswer2, answer2)) {
                    //答案不完整
                }
                wrongNum++;
                showCorrectAnswerTextView.setText("Wrong!\nthe answer(s) are:\n(1)" + question.getAnswer().get(0)+" (2)"+ question.getAnswer().get(1));
                submitButtonProcess();
            }else if (answerPre1.length() != 0 && answerPre2.length() != 0) {
                double answer1,answer2;
                List<Double> answerList = new ArrayList<>();
                answerList.addAll(question.getAnswer());
                try {
                    answer1 = Double.parseDouble(answerPre1);
                } catch (Exception e) {
                    Toast.makeText(this,"Incorrect Format!\nplease enter again",Toast.LENGTH_SHORT).show();
                    return;
                }
                DecimalFormat df = new DecimalFormat("#.00");
                answer1 = Double.valueOf(df.format(answer1));
                try {
                    answer2 = Double.parseDouble(answerPre2);
                } catch (Exception e) {
                    Toast.makeText(this,"Incorrect Format!\nplease enter again",Toast.LENGTH_SHORT).show();
                    return;
                }
                DecimalFormat df2 = new DecimalFormat("#.00");
                answer2 = Double.valueOf(df2.format(answer2));
                double tempAnswer1 = answerList.get(0);
                double tempAnswer2 = answerList.get(0);
                if ((areEqual(tempAnswer1, answer1) && areEqual(tempAnswer2, answer2)) || (areEqual(tempAnswer1, answer2) && areEqual(tempAnswer2, answer1))) {
                    correctNum++;
                    showCorrectAnswerTextView.setText("Right!\nthe answer(s) should be:\n(1) " + question.getAnswer().get(0) + " (2)" + question.getAnswer().get(1));
                } else {
                    wrongNum++;
                    showCorrectAnswerTextView.setText("Wrong!\nthe answer(s) should be:\n(1) " + question.getAnswer().get(0)+" (2)"+ question.getAnswer().get(1));
                }
                submitButtonProcess();
            }
        }

        endTime =System.currentTimeMillis();
        timeList.add( endTime - startTime);

        if (questionIndex == 10) {
            nextQuestionButton.setText("Finish");
        }
        hasSubmit = false;
    }


    private void generateQuestion() {
        for (int i = 0;i < 5 ;i++) {
            questionList.add(QuestionManager.generateLinearEquation());
        }
        for (int i = 0;i < 5 ;i++){
            questionList.add(QuestionManager.generateQuadraticEquation());
        }
    }

    private void updateQuestion() {

        if (questionIndex >= questionList.size()) {
            showReport();
            return;
        }

        Log.i("----->",questionIndex+"");
        question = questionList.get(questionIndex);
        questionBody = question.getBody();
        answerLength = question.getAnswer().size();

        if (answerLength == 1) {
            inputFirstAnswerExidtext.setVisibility(View.VISIBLE);
            inputSecondAnswerExidtext.setVisibility(View.GONE);
        } else if (answerLength == 2) {
            inputFirstAnswerExidtext.setVisibility(View.VISIBLE);
            inputSecondAnswerExidtext.setVisibility(View.VISIBLE);
        }
        showCorrectAnswerTextView.setText("Tips:\nwhen not integers\nround the answers to 2 decimal places");
        questionTextSwitcher.setText(questionBody);
        inputFirstAnswerExidtext.setText("");
        inputSecondAnswerExidtext.setText("");
        nextQuestionButtonProcess();
        startTime = System.currentTimeMillis();

        questionIndex++;
        hasSubmit = true;
    }

    public void submitButtonProcess(){
        nextQuestionButton.setEnabled(true);
        submitButton.setEnabled(false);
    }
    public void nextQuestionButtonProcess(){
        nextQuestionButton.setEnabled(false);
        submitButton.setEnabled(true);
    }

    boolean areEqual(Double A, Double B) {
        if (Math.abs(A - B) < 1e-6)
            return true;
        return false;
    }

    public void showReport(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Summary");

        StringBuilder reportBuilder = new StringBuilder();

        reportBuilder.append("the number of correct answers:"+correctNum);
        reportBuilder.append("\n");
        reportBuilder.append("the number of wrong answers:"+wrongNum);
        reportBuilder.append("\n");
        reportBuilder.append("the number of forgone answers:"+giveupNum);

        long linearUseTime = 0;
        long quaUseTime = 0;

        for (int i = 0 ; i < 5; i++) {
            linearUseTime += timeList.get(i);
            quaUseTime+= timeList.get(i+5);
        }
        reportBuilder.append("\n");
        reportBuilder.append("\n");
        reportBuilder.append("average time on each linear equation question: ");
        reportBuilder.append((int)linearUseTime/5000.0);
        reportBuilder.append("s");
        reportBuilder.append("\n");
        reportBuilder.append("average time on each quadratic equation question: ");
        reportBuilder.append((int)quaUseTime/5000.0);
        reportBuilder.append("s");

        builder.setMessage(reportBuilder.toString());

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((ExaminationFrame)mContext).finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.i("----->pre",s.toString());
        if (s.toString().contains(".") &&  s.toString().substring(s.toString().indexOf(".")+1).length() > 2) {
            s.delete(s.length()-1,s.length());
        }
        Log.i("----->after",s.toString());
    }

    private void updateTime() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hasSubmit) {
                            timeTextview.setText(((int)((System.currentTimeMillis() - startTime)/1000)) +"" );
                        }
                    }
                });

            }
        },0,1000);
    }
    private void stopUpdateTime(){
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(this);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }


    private void startPlayBackgroundMusic() {
//        mediaPlayer = new MediaPlayer();
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.backmusic);
        try {
            if(!mediaPlayer.isPlaying()) mediaPlayer.start();
//            mediaPlayer.setDataSource(getAssets().openFd("backMusic.mp3").getFileDescriptor());
//            mediaPlayer.prepare();
//            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPlayBackgroundMusic_version2(){
        testLoopPlayer();
    }

    public void testLoopPlayer() {
        mPlayer = MediaPlayer.create(this, mPlayResId);
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mPlayer.start();
            }
        });
        createNextMediaPlayer();
    }

    private void createNextMediaPlayer() {
        mNextPlayer = MediaPlayer.create(this, mPlayResId);
        mPlayer.setNextMediaPlayer(mNextPlayer);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();

                mPlayer = mNextPlayer;

                createNextMediaPlayer();
            }
        });
    }
    private void stopPlayBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        if (mNextPlayer != null) {
            mNextPlayer.stop();
            mNextPlayer.release();
            mNextPlayer = null;
        }
    }
}
