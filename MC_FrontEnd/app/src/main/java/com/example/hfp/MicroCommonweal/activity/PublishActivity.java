package com.example.hfp.MicroCommonweal.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hfp.MicroCommonweal.R;

import java.util.ArrayList;
import java.util.List;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private static final int IMAGE_REQUEST_CODE = 0;

    private String pic_path;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private String charity_category;

    private Button selectpic;
    private Spinner spDown;
    private ImageView charity_iamge;
    private EditText et_title;
    private EditText et_people_num;
    private EditText et_begin;
    private EditText et_end;
    private EditText et_position;
    private EditText et_phone;
    private EditText et_detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        selectpic = (Button)findViewById(R.id.select_pic);
        spDown=(Spinner) findViewById(R.id.et_charity_category);
        et_title = (EditText)findViewById(R.id.et_title);
        et_people_num = (EditText)findViewById(R.id.et_people_num);
        et_begin = (EditText)findViewById(R.id.et_begin);
        et_end = (EditText)findViewById(R.id.et_end);
        et_position = (EditText)findViewById(R.id.et_position);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_detail = (EditText)findViewById(R.id.et_detail);
        charity_iamge = (ImageView)findViewById(R.id.charity_iamge);
        selectpic.setOnClickListener(this);




        /*设置数据源*/
        list=new ArrayList<String>();
        list.add("青少年活动");
        list.add("青少年活动");
        list.add("青少年活动");
        list.add("青少年活动");


        /*新建适配器*/
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);

        /*adapter设置一个下拉列表样式，参数为系统子布局*/
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        /*spDown加载适配器*/
        spDown.setAdapter(adapter);

        /*soDown的监听器*/
        spDown.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_pic:
                // 调用android自带的图库
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
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
         charity_category=adapter.getItem(position);   //获取选中的那一项

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
                        Log.d("PublishActivity",columnIndex+"");
                        Log.d("PublishActivity:",pic_path );
                        if(bitmap !=null){
                            Toast.makeText(PublishActivity.this, "不空", Toast.LENGTH_SHORT).show();
                        }
                        charity_iamge.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}