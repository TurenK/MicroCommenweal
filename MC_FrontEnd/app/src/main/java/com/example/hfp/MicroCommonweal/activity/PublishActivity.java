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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hfp.MicroCommonweal.R;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int IMAGE_REQUEST_CODE = 0;

    private String pic_path;


    private Button selectpic;
    private ImageView charity_iamge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        selectpic = (Button)findViewById(R.id.select_pic);
        charity_iamge = (ImageView)findViewById(R.id.charity_iamge);
        selectpic.setOnClickListener(this);
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


//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (data != null) {
//            if (requestCode == ActivityRequestCode.SHOW_MAP_DEPOT
//                    && resultCode == Activity.RESULT_OK)
//                showYourPic(data);
//        }
//    }

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
