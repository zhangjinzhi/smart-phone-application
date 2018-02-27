package com.example.zhangjinzhi.answer_question.questiongenerator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/10/30.
 */

public class Question {
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Double> getAnswer() {
        return mAnswerList;
    }

    public void setAnswer(List<Double> answerList) {
        this.mAnswerList = answerList;
    }

    public Question(String body, List<Double> mAnswerList) {
        this.body = body;
        this.mAnswerList = mAnswerList;
    }

    @Override
    public String toString() {
        StringBuilder answers = new StringBuilder();
        for ( double answer : mAnswerList) {
            answers.append(answer);
            answers.append("  ");
        }
        return "question:"+body+"\n" + "anwers:"+answers.toString();
    }

    public String answers(){
        StringBuilder answers = new StringBuilder();
        for ( double answer : mAnswerList) {
            answers.append(answer);
            answers.append("  ");
        }
        return answers.toString();
    }

    private String body;
    private List<Double> mAnswerList;


}
