package com.example.hfp.MicroCommonweal.object;

public class Question_Judge {
    private String question;
    private String answer;
    //用户选择的答案1
    private String selectanswer = "";

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSelectanswer() {
        return selectanswer;
    }

    public void setSelectanswer(String selectanswer) {
        this.selectanswer = selectanswer;
    }
}
