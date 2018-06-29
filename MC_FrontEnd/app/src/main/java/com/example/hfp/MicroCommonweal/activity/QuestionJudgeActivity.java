package com.example.hfp.MicroCommonweal.activity;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.object.Question_Judge;

import java.util.ArrayList;
import java.util.List;

public class QuestionJudgeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_back;
    private TextView tv_question;
    private RadioButton RadioA;
    private RadioButton RadioB;
    private Button btn_last;
    private Button btn_next;
    private RadioGroup radio_group;
    private int right_num;
    private int count=2;
    private int current=0;
    private List<Question_Judge> question_judgeList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_judge);

        btn_back = findViewById(R.id.btn_back);
        tv_question = findViewById(R.id.tv_question);
        RadioA = findViewById(R.id.RadioA);
        RadioB = findViewById(R.id.RadioB);
        btn_last = findViewById(R.id.btn_last);
        btn_next = findViewById(R.id.btn_next);
        radio_group = findViewById(R.id.radio_group);
        btn_back.setOnClickListener(this);
        btn_last.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        current = 0;
        initQuestions();//初始化消息
        Question_Judge q = question_judgeList.get(current);
        tv_question.setText(q.getQuestion());

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                for(int i = 0;i<2;i++){
                    if(RadioA.isChecked()){
                        question_judgeList.get(current).setSelectanswer("对");
                    }else if (RadioB.isChecked()){
                        question_judgeList.get(current).setSelectanswer("错");
                    }
//                    break;
//                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_last:
                if (current > 0)//若当前题目不为第一题，点击previous按钮跳转到上一题；否则不响应
                {
                    current--;
                    Question_Judge q = question_judgeList.get(current);
                    tv_question.setText(q.getQuestion());


                    //若之前已经选择过，则应记录选择
                    radio_group.clearCheck();
                        if (q.getSelectanswer().equals("对")) {
                            RadioA.setChecked(true);
                        }else if(q.getSelectanswer().equals("错")){
                            RadioB.setChecked(true);
                        }
                }
                break;
            case R.id.btn_next:
                if (current < count - 1) {//若当前题目不为最后一题，点击next按钮跳转到下一题；否则不响应
                    current++;
                    //更新题目
                    Question_Judge q = question_judgeList.get(current);
                    tv_question.setText(q.getQuestion());

//                    //若之前已经选择过，则应记录选择
                    radio_group.clearCheck();
                    Log.d("Question", "is "+q.getSelectanswer());
                        if (q.getSelectanswer().equals("对")) {
                            RadioA.setChecked(true);
                        }else if(q.getSelectanswer().equals("错")){
                            RadioB.setChecked(true);
                        }

                }else{
                    right_num = checkAnswer(question_judgeList);
                    new AlertDialog.Builder(QuestionJudgeActivity.this)
                            .setTitle("提示")
                            .setMessage("您答对了"+right_num+
                                    "道题目；答错了"+(question_judgeList.size()-right_num)+"道题目。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    QuestionJudgeActivity.this.finish();
                                }
                            }).show();


                }

                break;
        }

    }

    private  void initQuestions(){
       // for(int i =0;i<6;i++){
            Question_Judge question = new Question_Judge();
            question.setQuestion("公益需要报名");
            question.setAnswer("对");
        Question_Judge question2 = new Question_Judge();
        question2.setQuestion("公益是坏的");
        question2.setAnswer("错");
            question_judgeList.add(question);
        question_judgeList.add(question2);
       // }
    }

    /*
判断用户作答是否正确，并将作答错误题目的下标生成list,返回给调用者
*/
    private int checkAnswer(List<Question_Judge> list) {
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
