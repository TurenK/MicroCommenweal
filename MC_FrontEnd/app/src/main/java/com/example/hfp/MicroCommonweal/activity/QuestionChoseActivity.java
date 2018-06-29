package com.example.hfp.MicroCommonweal.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Question_Chose;
import com.example.hfp.MicroCommonweal.object.Question_Judge;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class QuestionChoseActivity extends AppCompatActivity implements View.OnClickListener{


    private Button btn_back;
    private RadioButton[] radioButtons;
    private int count=2;
    private int current=0;
    private int right_num;
    private Button btn_next;
    private TextView tv_question;
    private RadioGroup radio_group;
    private List<Question_Chose> question_choseList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_chose);



        tv_question = findViewById(R.id.tv_question);
        radioButtons = new RadioButton[4];
        radioButtons[0] = findViewById(R.id.RadioA);
        radioButtons[1] = findViewById(R.id.RadioB);
        radioButtons[2] = findViewById(R.id.RadioC);
        radioButtons[3] = findViewById(R.id.RadioD);
        Button btn_previous = findViewById(R.id.btn_last);
        btn_next = findViewById(R.id.btn_next);
        radio_group = findViewById(R.id.radio_group);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        initQuestions();//初始化题目

        current = 0;

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == count - 2){
                    btn_next.setText("提交");
                }
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
                    Log.d("choose question", current + "  " + q.getSelectanswer()+"");
                    if (q.getSelectanswer() != -1) {
                        radioButtons[q.getSelectanswer()].setChecked(true);
                    }

                }else{
                    right_num = checkAnswer(question_choseList);
                    submitScore();
                }
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current != count - 2) {
                    btn_next.setText("下一题");
                }
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
                    Log.d("choose question", current + "  " + q.getSelectanswer()+"");
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
        JSONObject join_info = new JSONObject();
        join_info.put("userId", UserInfo.getUserInfo().getuId());
        Log.d("QuestionChoseActivity", join_info.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(join_info.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_QUESTION_CHOOSE), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("QuestionChoseActivity", jsonObject.toString());
                if (code == 200) {
                    JSONObject objectdata = jsonObject.getJSONObject("data");
                    List<Charity> charities = new ArrayList<>();
                    for (int i = 1; i <= 5; i++) {
                        if (objectdata.containsKey(String.valueOf(i))) {
                            JSONObject object = objectdata.getJSONObject(String.valueOf(i));
                            Question_Chose question = new Question_Chose();
                            question.setQuestion(object.getString("question"));
                            question.setAnswerA(object.getString("0"));
                            question.setAnswerB(object.getString("1"));
                            question.setAnswerC(object.getString("2"));
                            question.setAnswerD(object.getString("3"));
                            question.setAnswer(Integer.valueOf(object.getString("answer")));
                            question_choseList.add(question);
                        }
                    }
                    Question_Chose q = question_choseList.get(current);
                    tv_question.setText(q.getQuestion());
                    radioButtons[0].setText(q.getAnswerA());
                    radioButtons[1].setText(q.getAnswerB());
                    radioButtons[2].setText(q.getAnswerC());
                    radioButtons[3].setText(q.getAnswerD());
                    count = question_choseList.size();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("QuestionChoseActivity", "cannot connect to server!");
                Toast.makeText(QuestionChoseActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
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

    private void submitScore() {
        JSONObject join_info = new JSONObject();
        join_info.put("userId", UserInfo.getUserInfo().getuId());
        join_info.put("score", right_num);
        Log.d("QuestionJudgeActivity", join_info.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(join_info.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_SUBMIT_SCORE), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("QuestionJudgeActivity", jsonObject.toString());
                if (code == 200) {
                    new AlertDialog.Builder(QuestionChoseActivity.this)
                            .setTitle("提交成功！")
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

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("CollectionActivity", "cannot connect to server!");
                Toast.makeText(QuestionChoseActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }
}
