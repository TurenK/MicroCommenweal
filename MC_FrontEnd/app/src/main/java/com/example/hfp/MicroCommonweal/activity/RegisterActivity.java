package com.example.hfp.MicroCommonweal.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
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
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mob.MobSDK;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

import static com.mob.tools.utils.ResHelper.getStringRes;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //声明控件
    private static final int IMAGE_REQUEST_CODE = 0;
    private ImageButton avatar;
    private EditText name;
    private EditText phone;
    private EditText password_reg;
    private EditText password_reg_again;
    private EditText code;
    private TextView login_text;
    private Button register_btn;
    private Button btn_getCode;
    private ImageUpAndDownUtil imageUpAndDownUtil;
    private String received_filepath;
    private String pic_path;

    private int time = 60;
    private boolean sendedcode;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MobSDK.init(this);

        imageUpAndDownUtil = new ImageUpAndDownUtil(getApplicationContext());
        sendedcode = false;
        //初始化控件
        avatar = (ImageButton) findViewById(R.id.avatar);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.reg_phone);
        code = findViewById(R.id.input_code);
        password_reg = (EditText) findViewById(R.id.password_reg);
        password_reg_again = (EditText) findViewById(R.id.password_reg_again);
        login_text = (TextView) findViewById(R.id.login_text);
        register_btn = (Button) findViewById(R.id.register_btn);
        btn_getCode = findViewById(R.id.get_code);
        //控件事件监听器
        avatar.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        login_text.setOnClickListener(this);
        btn_getCode.setOnClickListener(this);
        //设置不能输入特殊字符和空格
        setEditTextInhibitInputSpeChat(name);
        setEditTextInhibitInputSpace(name);

        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result1, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result1;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);
    }
    private void reminderText() {
//        now.setVisibility(View.VISIBLE);
        handlerText.sendEmptyMessageDelayed(1, 1000);
    }

    @SuppressLint("HandlerLeak")
    Handler handlerText = new Handler() {
        public void handleMessage(Message msg) {
            if (sendedcode) {
                if (msg.what == 1) {
                    if (time > 0) {
                        btn_getCode.setText("重新发送(" + time + ")");
                        time--;
                        handlerText.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        time = 60;
                        setcodeen();
                    }
                } else {
                    code.setText("");
//                    now.setText("提示信息");
                    time = 60;
//                    now.setVisibility(View.GONE);
                    setcodeen();
                    //getCode.setVisibility(View.VISIBLE);
                }
            } else {
                setcodedis();
                //getCode.setVisibility(View.GONE);
            }
        }

        ;
    };

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result1 = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result1 == SMSSDK.RESULT_COMPLETE) {
// 短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
                    //Toast.makeText(getApplicationContext(), "验证码校验成功", Toast.LENGTH_SHORT).show();
                    handlerText.sendEmptyMessage(2);
                    register_btn.setEnabled(false);
                    register_btn.setText("注册中...");
                    register_btn.setBackgroundColor(0xD9BFBFBF);
                    register_btn.setTextColor(0xFF000000);
                    // 注册方法
                    register();

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//服务器验证码发送成功
                    reminderText();
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (flag) {
                    setcodeen();
                    //getCode.setVisibility(View.VISIBLE);
                    sendedcode = false;
                    Toast.makeText(RegisterActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    phone.requestFocus();
                } else {
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    code.selectAll();
                }
            }
        }
    };

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        switch (requestCode) {
            case IMAGE_REQUEST_CODE://这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        pic_path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(pic_path);
                        Log.d("PublishActivity:", pic_path);
                        avatar.setImageBitmap(bitmap);
                        //Thread.sleep(2000);

                        imageUpAndDownUtil.testPostImage(pic_path);
                        Log.d("PublishActivity:", received_filepath);
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar: // 头像
                //Toast.makeText(RegisterActivity.this, "点击头像", Toast.LENGTH_SHORT).show();
                // 调用android自带的图库
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
                break;
            case R.id.register_btn: // 注册
                if(password_reg.getText().toString().equals(password_reg_again.getText().toString())){
                    SMSSDK.submitVerificationCode("86", phone.getText().toString().trim(), code.getText().toString().trim());
                    flag = false;
//                    register();
                }else{
                    Toast.makeText(RegisterActivity.this, "密码不匹配！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.login_text: // 登录
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.get_code:
                Log.d("Register", "push");
                if (!TextUtils.isEmpty(phone.getText().toString().trim())) {
                    if (phone.getText().toString().trim().length() == 11) {
                        SMSSDK.getVerificationCode("86", phone.getText().toString().trim());
                        sendedcode = true;
                        code.requestFocus();
                        setcodedis();
                        //getCode.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入完整手机号码", Toast.LENGTH_LONG).show();
                        phone.requestFocus();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入您的手机号码", Toast.LENGTH_LONG).show();
                    phone.requestFocus();
                }
                break;
            default:
                break;
        }
    }


    private boolean register() {
        String s_username = name.getText().toString().trim();
        String s_pwd = password_reg.getText().toString();
        String s_phone = phone.getText().toString().trim();

        //创建网络访问对象
        JSONObject register_json = new JSONObject();
        register_json.put("username", s_username);
        register_json.put("password", s_pwd);
        register_json.put("phone",s_phone);
        received_filepath = imageUpAndDownUtil.getReceived_filepath();
        register_json.put("imageurl",received_filepath);

        StringEntity stringEntity = null;
        stringEntity = new StringEntity(register_json.toString(),"utf-8");

        Log.d("RegisterActivity", "prepare to send!");

        AsyncHttpUtil.post(this, this.getString(R.string.URL_REG), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                Log.d("RegisterActivity", content);
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("RegisterActivity", info);
                if (code == 200){
                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    finish();
                }else if(code == 404){
                    Toast.makeText(RegisterActivity.this, "用户名与密码不匹配！", Toast.LENGTH_LONG).show();
                    register_btn.setEnabled(true);
                    register_btn.setText("注册");
                }else if(code == 405){
                    Toast.makeText(RegisterActivity.this, "该用户注册过！", Toast.LENGTH_LONG).show();
                    register_btn.setEnabled(true);
                    register_btn.setText("注册");
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("RegisterActivity", "cannot connect to server!");
                Toast.makeText(RegisterActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
                register_btn.setEnabled(true);
                register_btn.setText("注册");
            }
        });

        return true;
    }

    private void setcodedis() {
        btn_getCode.setEnabled(false);
        btn_getCode.setBackgroundColor(0xD9BFBFBF);
    }

    private void setcodeen() {
        btn_getCode.setEnabled(true);
        btn_getCode.setBackgroundResource(R.drawable.login_btn_bg);
        btn_getCode.setText("发送验证码");
    }

}
