package com.example.hfp.MicroCommonweal.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.object.Question_Chose;
import com.example.hfp.MicroCommonweal.object.Question_Judge;

import java.util.ArrayList;
import java.util.List;

public class QuestionChoseActivity extends AppCompatActivity implements View.OnClickListener{


    private Button btn_back;
    private int count=2;
    private int current=0;
    private int right_num;
    private List<Question_Chose> question_choseList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_chose);



        final TextView tv_question = findViewById(R.id.tv_question);
        final RadioButton[] radioButtons = new RadioButton[4];
        radioButtons[0] = findViewById(R.id.RadioA);
        radioButtons[1] = findViewById(R.id.RadioB);
        radioButtons[2] = findViewById(R.id.RadioC);
        radioButtons[3] = findViewById(R.id.RadioD);
        Button btn_previous = findViewById(R.id.btn_last);
        Button btn_next = findViewById(R.id.btn_next);
        final RadioGroup radio_group = findViewById(R.id.radio_group);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        initQuestions();//初始化题目

        current = 0;

        Question_Chose q = question_choseList.get(current);
        tv_question.setText(q.getQuestion());
        radioButtons[0].setText(q.getAnswerA());
        radioButtons[1].setText(q.getAnswerB());
        radioButtons[2].setText(q.getAnswerC());
        radioButtons[3].setText(q.getAnswerD());

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current < count - 1) {//若当前题目不为最后一题，点击next按钮跳转到下一题；否则不响应
                    current++;
                    //更新题目
                    Question_Chose q = question_choseList.get(current);
                    tv_question.setText(q.getQuestion());
                    radioButtons[0].setText(q.getAnswerA());
                    radioButtons[1].setText(q.getAnswerB());
                    radioButtons[2].setText(q.getAnswerC());
                    radioButtons[3].setText(q.getAnswerD());

                    //若之前已经选择过，则应记录选择
                    radio_group.clearCheck();
                    if (q.getSelectanswer() != -1) {
                        radioButtons[q.getSelectanswer()].setChecked(true);
                    }

                }else{
                    right_num = checkAnswer(question_choseList);
                    new AlertDialog.Builder(QuestionChoseActivity.this)
                            .setTitle("提示")
                            .setMessage("您答对了"+right_num+
                                    "道题目；答错了"+(question_choseList.size()-right_num)+"道题目。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    QuestionChoseActivity.this.finish();
                                }
                            }).show();
                }
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current > 0)//若当前题目不为第一题，点击previous按钮跳转到上一题；否则不响应
                {
                    current--;
                    Question_Chose q = question_choseList.get(current);
                    tv_question.setText(q.getQuestion());
                    radioButtons[0].setText(q.getAnswerA());
                    radioButtons[1].setText(q.getAnswerB());
                    radioButtons[2].setText(q.getAnswerC());
                    radioButtons[3].setText(q.getAnswerD());

                    //若之前已经选择过，则应记录选择
                    radio_group.clearCheck();
                    if (q.getSelectanswer() != -1) {
                        radioButtons[q.getSelectanswer()].setChecked(true);
                    }

                }

            }
        });

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < 4; i++) {
                    if (radioButtons[i].isChecked()) {
                        question_choseList.get(current).setSelectanswer(i);
                        break;
                    }
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private  void initQuestions(){
        // for(int i =0;i<6;i++){
        Question_Chose question = new Question_Chose();
        question.setQuestion("这是题目1");
        question.setAnswerA("A1");
        question.setAnswerB("B1");
        question.setAnswerC("C1");
        question.setAnswerD("D1");
        question.setAnswer(0);

        Question_Chose question2 = new Question_Chose();
        question2.setQuestion("这是题目2");
        question2.setAnswerA("A2");
        question2.setAnswerB("B2");
        question2.setAnswerC("C2");
        question2.setAnswerD("D2");
        question2.setAnswer(1);

        question_choseList.add(question);
        question_choseList.add(question2);
        // }
    }

    /*
判断用户作答是否正确，并将作答错误题目的下标生成list,返回给调用者
*/
    private int checkAnswer(List<Question_Chose> list) {
        int right_num=0;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getAnswer()==list.get(i).getSelectanswer()){
                right_num++;
            }
        }
        return right_num;
    }
}
