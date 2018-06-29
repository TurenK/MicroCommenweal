package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

public class VertifyPerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_back;
    private ImageButton select_pic;
    private TextView et_name;
    private TextView et_id;
    private Button btn_submit_id;
    private static String TAG = "VertifyPerActivity";
    private static final int IMAGE_REQUEST_CODE = 0;
    private Uri selectedImage;
    private String pic_path;
    private ImageUpAndDownUtil imageUpAndDownUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_per);
        imageUpAndDownUtil = new ImageUpAndDownUtil(getApplicationContext());
        button_back = (Button)findViewById(R.id.button_back);
        select_pic = (ImageButton)findViewById(R.id.select_pic);
        btn_submit_id = (Button)findViewById(R.id.btn_submit_id);
        btn_submit_id.setOnClickListener(this);
        button_back.setOnClickListener(this);
        select_pic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_pic:
                // 调用android自带的图库
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(intent, CUT_PICTURE);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
                break;
            case R.id.button_back:
                finish();
                break;
            case R.id.btn_submit_id:
                sendInfo();
                break;
        }
    }

    private void sendInfo(){
        //创建网络访问对象
        JSONObject login_json = new JSONObject();
            login_json.put("userId", UserInfo.getUserInfo().getuId());
        Log.d(TAG, login_json.toString());

        StringEntity stringEntity = null;

        stringEntity = new StringEntity(login_json.toString(), "UTF-8");

        Log.d(TAG, login_json.toJSONString());
        Log.d(TAG, "prepare to send!");


        AsyncHttpUtil.post(this, this.getString(R.string.URL_VERTIFY_PER), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                Log.d(TAG, content);
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d(TAG, info);
                if (code == 200) {
                    Toast.makeText(VertifyPerActivity.this, "验证成功！", Toast.LENGTH_LONG).show();
                    UserInfo.getUserInfo().setuVerify(UserInfo.VERTIFIED);
                    finish();
                } else {
                    Toast.makeText(VertifyPerActivity.this, "验证失败！", Toast.LENGTH_LONG).show();
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "cannot connect to server!");
                Toast.makeText(VertifyPerActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        switch (requestCode) {
            case IMAGE_REQUEST_CODE://这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
//                                .openInputStream(selectedImage));
                        selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        pic_path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(pic_path);
                        Log.d("PublishActivity:", pic_path);
                        select_pic.setImageBitmap(bitmap);
                        //Thread.sleep(2000);

                        imageUpAndDownUtil.testPostImage(pic_path);
                        //Log.d("PublishActivity:", received_filepath);
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
