package com.example.zhangjinzhi.answer_question.questiongenerator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/10/30.
 */

public class QuestionManager {

    public static Question generateLinearEquation(){
        int a ;
        int b ;
        StringBuilder questionBodyBuilder = new StringBuilder();
        Question linearQuestion;
        double result;
        List<Double> answerList = new ArrayList<>();

        a = new Random().nextInt(199) - 99;
        b = new Random().nextInt(199) - 99;
        while (a == 0) {
            a = new Random().nextInt(199) - 99;
        }

        questionBodyBuilder.append(a);
        questionBodyBuilder.append("x");
        if (b > 0) {
            questionBodyBuilder.append("+");
            questionBodyBuilder.append(b);
        } else if (b < 0) {
            questionBodyBuilder.append(b);
        }
        questionBodyBuilder.append("=0");

        result = ((double)(0 - b)) / a;
        DecimalFormat df = new DecimalFormat("#.00");
        result = Double.valueOf(df.format(result));
        answerList.add(result);

        linearQuestion= new Question(questionBodyBuilder.toString(),answerList);

        return linearQuestion;
    }

    public static Question generateQuadraticEquation(){
        int a,b,c;
        int judgment;
        StringBuilder questionBodyBuilder = new StringBuilder();
        Question quadraticQuestion;
        List<Double> answerList= new ArrayList<>();
        double result1,result2;

        a = new Random().nextInt(199) - 99;
        while (a == 0) {
            a = new Random().nextInt(199) - 99;
        }
        b = new Random().nextInt(199) - 99;
        c = new Random().nextInt(199) - 99;
        judgment = (b * b) - (4 * a * c);
        while (judgment < 0) {
            b = new Random().nextInt(199) - 99;
            c = new Random().nextInt(199) - 99;
            judgment = (b * b) - (4 * a * c);
        }
        questionBodyBuilder.append(a).append("x^2");
        if (b > 0) {
            questionBodyBuilder.append("+").append(b).append("x");
        } else if (b < 0) {
            questionBodyBuilder.append(b).append("x");
        }
        if (c > 0) {
            questionBodyBuilder.append("+").append(c);
        } else if (c < 0) {
            questionBodyBuilder.append(c);
        }
        questionBodyBuilder.append("=0");

        if (judgment == 0) {
            result1 = -(((double)b)/ (2 * a));
            DecimalFormat df = new DecimalFormat("#.00");
            result1 = Double.valueOf(df.format(result1));
            answerList.add(result1);
        } else if (judgment > 0) {
            result1 =  ((-b) + Math.sqrt(judgment))/ (2 * a);
            DecimalFormat df = new DecimalFormat("#.00");
            result1 = Double.valueOf(df.format(result1));
            answerList.add(result1);

            result2 =  ((-b) - Math.sqrt(judgment))/ (2 * a);
            DecimalFormat df2 = new DecimalFormat("#.00");
            result2 = Double.valueOf(df2.format(result2));
            answerList.add(result2);
        }

        quadraticQuestion = new Question(questionBodyBuilder.toString(),answerList);

        return quadraticQuestion;
    }


}














