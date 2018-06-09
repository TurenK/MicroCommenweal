package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterTeamActivity extends AppCompatActivity implements View.OnClickListener{
    //声明控件
    private ImageButton team_avatar;
    private EditText team_name;
    private EditText team_email;
    private EditText password_reg_team;
    private EditText password_reg_team_again;
    private EditText groupadress;
    private EditText groupintro;
    private TextView login_text;
    private Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_team);

        //初始化控件
        team_avatar = (ImageButton)findViewById(R.id.team_avatar);
        team_name =(EditText)findViewById(R.id.team_name);
        team_email =(EditText)findViewById(R.id.team_email);
        password_reg_team =(EditText)findViewById(R.id.password_reg_team);
        password_reg_team_again =(EditText)findViewById(R.id.password_reg_team_again);
        groupadress =(EditText)findViewById(R.id.groupadress);
        groupintro =(EditText)findViewById(R.id.groupintro);
        login_text =(TextView)findViewById(R.id.login_text);
        register_btn = (Button)findViewById(R.id.register_btn);

        //控件事件监听器
        team_avatar.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        login_text.setOnClickListener(this);

        //设置不能输入特殊字符和空格
        setEditTextInhibitInputSpeChat(team_name);
        setEditTextInhibitInputSpace(team_name);
    }

    /**
     * 禁止EditText输入空格
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(" ")){
                    return "";
                }else{
                    return null;
                }

            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText){

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(matcher.find())return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar: // 头像
                Toast.makeText(RegisterTeamActivity.this, "点击头像", Toast.LENGTH_SHORT).show();
                break;
            case R.id.register_btn: // 注册
                if(!password_reg_team.getText().toString().equals(password_reg_team_again.getText().toString())){
                    Toast.makeText(RegisterTeamActivity.this, "密码不匹配！", Toast.LENGTH_LONG).show();
                } else if (team_name.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterTeamActivity.this, "组织名不能为空！", Toast.LENGTH_LONG).show();
                }else if (team_email.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterTeamActivity.this, "组织邮箱不能为空！", Toast.LENGTH_LONG).show();
                }else if (groupadress.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterTeamActivity.this, "组织地址不能为空！", Toast.LENGTH_LONG).show();
                }else if (groupintro.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterTeamActivity.this, "组织介绍不能为空！", Toast.LENGTH_LONG).show();
                }
                else{
                    register();
                }
                break;
            case R.id.login_text: // 登录
                startActivity(new Intent(RegisterTeamActivity.this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }


    private boolean register() {
        String g_name = team_name.getText().toString().trim();
        String g_pwd = password_reg_team.getText().toString();
        String g_email = team_email.getText().toString().trim();
        String g_adress = groupadress.getText().toString().trim();
        String g_intro = groupintro.getText().toString().trim();

        //创建网络访问对象
        JSONObject register_json = new JSONObject();
        register_json.put("groupName", g_name);
        register_json.put("groupPassword", g_pwd);
        register_json.put("groupMail",g_email);
        register_json.put("groupAddress",g_adress);
        register_json.put("groupIntro",g_intro);
        register_json.put("groupType","");

        StringEntity stringEntity = null;
        stringEntity = new StringEntity(register_json.toString(),"utf-8");

        Log.d("RegisterTeamActivity", "prepare to send!");

        AsyncHttpUtil.post(this, this.getString(R.string.URL_GROUP_REG), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                Log.d("RegisterTeamActivity", content);
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("RegisterTeamActivity", info);
                if (code == 200){
                    Toast.makeText(RegisterTeamActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterTeamActivity.this,MainActivity.class));
                    finish();
                }else if(code == 400){
                    Toast.makeText(RegisterTeamActivity.this, "注册失败！", Toast.LENGTH_LONG).show();
                    register_btn.setEnabled(true);
                    register_btn.setText("注册");
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("RegisterTeamActivity", "cannot connect to server!");
                Toast.makeText(RegisterTeamActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
                register_btn.setEnabled(true);
                register_btn.setText("注册");
            }
        });

        return true;


    }


}
