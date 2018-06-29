package com.example.hfp.MicroCommonweal.object;


public class Question_Chose {
    private String question;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private int answer;
    private int selectanswer = -1;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getSelectanswer() {
        return selectanswer;
    }

    public void setSelectanswer(int selectanswer) {
        this.selectanswer = selectanswer;
    }
}
