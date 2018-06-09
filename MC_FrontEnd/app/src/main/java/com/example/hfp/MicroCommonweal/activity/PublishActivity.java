package com.example.hfp.MicroCommonweal.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.Utils.EncodingUtil;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.Utils.ShowImFileCallBack;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.apache.http.entity.StringEntity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

import okhttp3.Call;
import okhttp3.Response;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int IMAGE_REQUEST_CODE = 0;

    private String pic_path;
    private String received_filepath;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private String charity_category;
    int mYear, mMonth, mDay;
    int eYear, eMonth, eDay;
    final int DATE_DIALOG = 1;
    final int END_DATE_DIALOG = 2;

    private Button selectpic;
    private Button btn_submit;
    private Spinner spDown;
    private ImageView charity_iamge;
    private EditText et_title;
    private EditText et_people_num;
    private TextView et_begin;
    private TextView et_end;
    private EditText et_position;
    private EditText et_phone;
    private EditText et_detail;

    private ImageUpAndDownUtil imageUpAndDownUtil;


    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        selectpic = (Button) findViewById(R.id.select_pic);
        spDown = (Spinner) findViewById(R.id.et_charity_category);
        et_title = (EditText) findViewById(R.id.et_title);
        et_people_num = (EditText) findViewById(R.id.et_people_num);
        et_begin = (TextView) findViewById(R.id.et_begin);
        et_end = (TextView) findViewById(R.id.et_end);
        et_position = (EditText) findViewById(R.id.et_position);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_detail = (EditText) findViewById(R.id.et_detail);
        charity_iamge = (ImageView) findViewById(R.id.charity_iamge);
        btn_submit = findViewById(R.id.charity_submit);
        selectpic.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        et_begin.setOnClickListener(this);
        et_end.setOnClickListener(this);

        getPermission();

        imageUpAndDownUtil = new ImageUpAndDownUtil(getApplicationContext());

        /*设置数据源*/
        list = new ArrayList<String>();
        list.add("青少年活动");
        list.add("青少年活动");
        list.add("青少年活动");
        list.add("青少年活动");


        /*新建适配器*/
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        /*adapter设置一个下拉列表样式，参数为系统子布局*/
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        /*spDown加载适配器*/
        spDown.setAdapter(adapter);

        /*soDown的监听器*/
        spDown.setOnItemSelectedListener(this);


        final Calendar begin = Calendar.getInstance();
        mYear = begin.get(Calendar.YEAR);
        mMonth = begin.get(Calendar.MONTH);
        mDay = begin.get(Calendar.DAY_OF_MONTH);

        final Calendar end = Calendar.getInstance();
        eYear = end.get(Calendar.YEAR);
        eMonth = end.get(Calendar.MONTH);
        eDay = end.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
            case END_DATE_DIALOG:
                return new DatePickerDialog(this, edateListener, eYear, eMonth, eDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            et_begin.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
        }
    };

    private DatePickerDialog.OnDateSetListener edateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            eYear = year;
            eMonth = monthOfYear;
            eDay = dayOfMonth;
            et_end.setText(new StringBuffer().append(eYear).append("-").append(eMonth + 1).append("-").append(eDay).append(" "));
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_pic:
                // 调用android自带的图库
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
                break;
            case R.id.charity_submit:
                if (et_title.getText().toString().trim().equals("")) {
                    Toast.makeText(PublishActivity.this, "义工标题不能为空！", Toast.LENGTH_LONG).show();
                } else if (et_people_num.getText().toString().trim().equals("")) {
                    Toast.makeText(PublishActivity.this, "义工人数不能为空！", Toast.LENGTH_LONG).show();
                } else if (et_begin.getText().toString().trim().equals("")) {
                    Toast.makeText(PublishActivity.this, "开始时间不能为空！", Toast.LENGTH_LONG).show();
                } else if (et_end.getText().toString().trim().equals("")) {
                    Toast.makeText(PublishActivity.this, "结束时间不能为空！", Toast.LENGTH_LONG).show();
                } else if (et_position.getText().toString().trim().equals("")) {
                    Toast.makeText(PublishActivity.this, "义工地点不能为空！", Toast.LENGTH_LONG).show();
                } else if (et_phone.getText().toString().trim().equals("")) {
                    Toast.makeText(PublishActivity.this, "联系方式不能为空！", Toast.LENGTH_LONG).show();
                } else if (et_detail.getText().toString().trim().equals("")) {
                    Toast.makeText(PublishActivity.this, "义工详情不能为空！", Toast.LENGTH_LONG).show();
                } else {
                    sendInfo();
//                    Log.d("PublishActivity", EncodingUtil.gbEncoding(et_title.getText().toString().trim()));
                    btn_submit.setEnabled(false);
                }
                break;
            case R.id.et_begin:
                showDialog(DATE_DIALOG);
                break;
            case R.id.et_end:
                showDialog(END_DATE_DIALOG);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        charity_category = adapter.getItem(position);   //获取选中的那一项

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
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
                        charity_iamge.setImageBitmap(bitmap);
                        //Thread.sleep(2000);

                        imageUpAndDownUtil.testPostImage(pic_path,"1");
                        Log.d("PublishActivity:", received_filepath);
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void sendInfo() {
        //封装需要传递的参数
        Log.d("PublishActivity", et_title.getText().toString().trim());
        Log.d("PublishActivity", et_end.getText().toString().trim());
        Log.d("PublishActivity", et_begin.getText().toString().trim());
        Log.d("PublishActivity", et_position.getText().toString().trim());
        Log.d("PublishActivity", et_detail.getText().toString().trim());
        Log.d("PublishActivity", et_people_num.getText().toString().trim());
        Log.d("PublishActivity", UserInfo.getUserInfo().getuId());


        Log.d("PublishActivity", "Data is ok! Ready to send.");

        JSONObject pub_info = new JSONObject();
        pub_info.put("activitySponsor", UserInfo.getUserInfo().getuId());
//        pub_info.put("activityType",EncodingUtil.gbEncoding(charity_category));
        pub_info.put("activityType", charity_category);
//        pub_info.put("activityName", EncodingUtil.gbEncoding(et_title.getText().toString().trim()));
        pub_info.put("activityName", et_title.getText().toString().trim());
        pub_info.put("activityDeadline", et_end.getText().toString().trim());
        pub_info.put("activityStartTime", et_begin.getText().toString().trim());
        pub_info.put("activityEndTime", et_end.getText().toString().trim());
        pub_info.put("activityAddress", et_position.getText().toString().trim());
        pub_info.put("activityTel", et_phone.getText().toString().trim());
        pub_info.put("activityIntroduction", et_detail.getText().toString().trim());
        pub_info.put("aNeedNumOfPerson", et_people_num.getText().toString().trim());
        received_filepath = imageUpAndDownUtil.getReceived_filepath();
        pub_info.put("activityImage", received_filepath);

        //new ImageUpAndDownUtil(getApplicationContext()).testPostImage(pic_path);


        Log.d("PublishActivity", pub_info.toString());

        StringEntity stringEntity = null;
        stringEntity = new StringEntity(pub_info.toString(), "UTF-8");

        AsyncHttpUtil.post(this, this.getString(R.string.URL_PUBLISH), stringEntity, "application/x-www-form-urlencoded", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("PublishActivity", jsonObject.toString());

                if (code == 200) {
                    //TODO Intent
//                    JSONObject jObject = jsonObject.getJSONObject("data");
//                    Log.d("PublishActivity", EncodingUtil.decodeUnicode(jObject.getString("activityName")));
                    Toast.makeText(PublishActivity.this, "创建成功！", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PublishActivity.this, MainUIActivity.class));
                    finish();
                } else if (code == 400) {
                    Toast.makeText(PublishActivity.this, "创建活动失败！请稍后再试", Toast.LENGTH_LONG).show();
                    btn_submit.setEnabled(true);
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("PublishActivity", "cannot connect to server!");
                Toast.makeText(PublishActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                btn_submit.setEnabled(true);
//                super.onFailure(error, content);
            }
        });
    }
}
