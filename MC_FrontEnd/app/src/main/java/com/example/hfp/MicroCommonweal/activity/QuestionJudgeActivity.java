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
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Question_Judge;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class QuestionJudgeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_back;
    private TextView tv_question;
    private RadioButton RadioA;
    private RadioButton RadioB;
    private Button btn_last;
    private Button btn_next;
    private RadioGroup radio_group;
    private int right_num;
    private int count;
    private int current = 0;
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

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (RadioA.isChecked()) {
                    question_judgeList.get(current).setSelectanswer("对");
                } else if (RadioB.isChecked()) {
                    question_judgeList.get(current).setSelectanswer("错");
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
            case R.id.btn_last:
                if (current != count - 2) {
                    btn_next.setText("下一题");
                }
                if (current > 0)//若当前题目不为第一题，点击previous按钮跳转到上一题；否则不响应
                {
                    current--;
                    Question_Judge q = question_judgeList.get(current);
                    tv_question.setText(q.getQuestion());


                    //若之前已经选择过，则应记录选择
                    radio_group.clearCheck();
                    if (q.getSelectanswer().equals("对")) {
                        RadioA.setChecked(true);
                    } else if (q.getSelectanswer().equals("错")) {
                        RadioB.setChecked(true);
                    }
                }
                break;
            case R.id.btn_next:
                if (current < count - 1) {//若当前题目不为最后一题，点击next按钮跳转到下一题；否则不响应
                    if (current == count - 2) {
                        btn_next.setText("提交");
                    }
                    current++;
                    //更新题目
                    Question_Judge q = question_judgeList.get(current);
                    tv_question.setText(q.getQuestion());

//                    //若之前已经选择过，则应记录选择
                    radio_group.clearCheck();
                    Log.d("Question", "is " + q.getSelectanswer());
                    if (q.getSelectanswer().equals("对")) {
                        RadioA.setChecked(true);
                    } else if (q.getSelectanswer().equals("错")) {
                        RadioB.setChecked(true);
                    }

                } else {
                    right_num = checkAnswer(question_judgeList);
                    submitScore();
                }
                break;
        }

    }

    private void initQuestions() {
        JSONObject join_info = new JSONObject();
        join_info.put("userId", UserInfo.getUserInfo().getuId());
        Log.d("QuestionJudgeActivity", join_info.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(join_info.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_QUESTION_TOF), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("QuestionJudgeActivity", jsonObject.toString());
                if (code == 200) {
                    JSONObject objectdata = jsonObject.getJSONObject("data");
                    List<Charity> charities = new ArrayList<>();
                    for (int i = 1; i <= 5; i++) {
                        if (objectdata.containsKey(String.valueOf(i))) {
                            JSONObject object = objectdata.getJSONObject(String.valueOf(i));
                            Question_Judge question = new Question_Judge();
                            question.setQuestion(object.getString("question"));
                            question.setAnswer(object.getString("tof"));
                            question_judgeList.add(question);
                        }
                    }
                    Question_Judge q = question_judgeList.get(current);
                    tv_question.setText(q.getQuestion());
                    count = question_judgeList.size();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("CollectionActivity", "cannot connect to server!");
                Toast.makeText(QuestionJudgeActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }

    /*
判断用户作答是否正确，并将作答错误题目的下标生成list,返回给调用者
*/
    private int checkAnswer(List<Question_Judge> list) {
        int right_num = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAnswer().equals(list.get(i).getSelectanswer())) {
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
                    new AlertDialog.Builder(QuestionJudgeActivity.this)
                            .setTitle("提交成功！")
                            .setMessage("您答对了" + right_num +
                                    "道题目；答错了" + (question_judgeList.size() - right_num) + "道题目。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    QuestionJudgeActivity.this.finish();
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("CollectionActivity", "cannot connect to server!");
                Toast.makeText(QuestionJudgeActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }

}
