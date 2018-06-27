package com.example.hfp.MicroCommonweal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

public class PersonalInfoActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final int IMAGE_REQUEST_CODE = 0;
    private final String TAG = "pengfeiwuer";
    private TextView tv_userid;
    private TextView tv_name;
    private TextView tv_gender;
    private TextView tv_signature;
    private TextView tv_phone;
    private ImageView iv_avator;
    private RelativeLayout rl_userid;
    private RelativeLayout rl_name;
    private RelativeLayout rl_gender;
    private Button btn_back;
    private Button btn_submit;
    private ImageUpAndDownUtil imageUpAndDownUtil;
    private String received_filepath;
    private String pic_path;

    private String[] sexArry = new String[]{ "女", "男"};// 性别选择
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        imageUpAndDownUtil = new ImageUpAndDownUtil(getApplicationContext());
        //初始化控件
        btn_submit = (Button) findViewById(R.id.btn_submit_personal_info);
        tv_userid = (TextView)findViewById(R.id.tv_userid);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_gender = (TextView)findViewById(R.id.tv_gender);
        tv_signature = (TextView)findViewById(R.id.tv_signature);
        tv_phone = (TextView)findViewById(R.id.tv_phone);
        iv_avator = (ImageView)findViewById(R.id.iv_avator);
        rl_userid = (RelativeLayout)findViewById(R.id.rl_userid);
        rl_name = (RelativeLayout)findViewById(R.id.rl_name);
        rl_gender = (RelativeLayout)findViewById(R.id.rl_gender);
        btn_back = (Button)findViewById(R.id.btn_back);
        //设置监听器
        rl_userid.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
        tv_signature.setOnClickListener(this);
        tv_phone.setOnClickListener(this);
        iv_avator.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        setInfo();
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
                        iv_avator.setImageBitmap(bitmap);
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

    private void setInfo(){
        UserInfo recentRegister = UserInfo.getUserInfo();
        //创建网络访问对象
        imageUpAndDownUtil.testDownloadImage(recentRegister.getuAvatar(),iv_avator);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_userid:
                final EditText inputUserid= new EditText(PersonalInfoActivity.this);
                AlertDialog.Builder builder_userid = new AlertDialog.Builder(PersonalInfoActivity.this);
                builder_userid.setTitle("用户ID").setView(inputUserid)
                        .setNegativeButton("取消", null);
                builder_userid.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        tv_userid.setText( inputUserid.getText().toString());
                    }
                });
                builder_userid.show();
                break;
            case R.id.rl_name:
                final EditText inputName= new EditText(PersonalInfoActivity.this);
                AlertDialog.Builder builder_name = new AlertDialog.Builder(PersonalInfoActivity.this);
                builder_name.setTitle("昵称").setView(inputName)
                        .setNegativeButton("取消", null);
                builder_name.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        tv_name.setText( inputName.getText().toString());
                    }
                });
                builder_name.show();
                break;
            case R.id.rl_gender:
                AlertDialog.Builder builder_gender = new AlertDialog.Builder(this);// 自定义对话框
                builder_gender.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 2默认的选中

                    @Override
                    public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                        // showToast(which+"");
                        tv_gender.setText(sexArry[which]);
                        dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
                    }
                });
                builder_gender.show();// 让弹出框显示
                break;
            case R.id.tv_signature:
                final EditText inputSignature= new EditText(PersonalInfoActivity.this);
                AlertDialog.Builder builder_signature = new AlertDialog.Builder(PersonalInfoActivity.this);
                builder_signature.setTitle("个性签名").setView(inputSignature)
                        .setNegativeButton("取消", null);
                builder_signature.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        tv_signature.setText( inputSignature.getText().toString());
                    }
                });
                builder_signature.show();
                break;
            case R.id.tv_phone:
                final EditText inputPhone= new EditText(PersonalInfoActivity.this);
                AlertDialog.Builder builder_phone = new AlertDialog.Builder(PersonalInfoActivity.this);
                builder_phone.setTitle("电话号码").setView(inputPhone)
                        .setNegativeButton("取消", null);
                builder_phone.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        tv_phone.setText( inputPhone.getText().toString());
                    }
                });
                builder_phone.show();
                break;
            case R.id.iv_avator:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
                break;
            case R.id.btn_back:
                //提交信息
                finish();
                break;
            case R.id.btn_submit_personal_info:
                finish();
                break;
        }
    }
}
