package com.example.hfp.MicroCommonweal.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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
import com.example.hfp.MicroCommonweal.Utils.Utils;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.FileStorage;
import com.example.hfp.MicroCommonweal.object.PermissionsChecker;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PersonalInfoActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final int IMAGE_REQUEST_CODE = 0;
    protected static final int CHOOSE_PICTURE = 10; //相册选取
    protected static final int TAKE_PICTURE = 30; //拍照
    private static final int REQUEST_PERMISSION = 40;  //权限请求
    private static final int CROP_SMALL_PICTURE = 20; //剪裁图片
    protected static Uri tempUri;
    private PermissionsChecker mPermissionsChecker;
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private boolean isClickCamera;

    private final String TAG = "PersonalInfoActivity";
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
    private Button btn_submit_personal_info;
    private ImageUpAndDownUtil imageUpAndDownUtil;
    private String received_filepath;
    private String pic_path;
    String username;
    String usergender;
    String userdesc;
    int userphone;
    String userimage;

    private String[] sexArry = new String[]{ "女", "男"};// 性别选择
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        imageUpAndDownUtil = new ImageUpAndDownUtil(getApplicationContext());
        //初始化控件
        btn_submit_personal_info = (Button) findViewById(R.id.btn_submit_personal_info);
        btn_submit_personal_info.setOnClickListener(this);
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

        mPermissionsChecker = new PermissionsChecker(this);

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
                        Log.d(TAG,selectedImage.toString());
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
            case CHOOSE_PICTURE://选择图片
                startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                break;
            case CROP_SMALL_PICTURE://裁剪完成
                if (data != null) {
                    setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                }
                break;
            case REQUEST_PERMISSION://权限请求
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    finish();
                } else {
                    if (isClickCamera) {
                        openCamera();
                    } else {
                        selectFromAlbum();
                    }
                }
                break;
            case TAKE_PICTURE://拍照
                startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                break;
        }
    }

    private void setInfo(){
        UserInfo recentRegister = UserInfo.getUserInfo();
        //创建网络访问对象
        imageUpAndDownUtil.testDownloadImage(recentRegister.getuAvatar(),iv_avator);
        tv_userid.setText(recentRegister.getuId());
        tv_name.setText(recentRegister.getuName());
        tv_gender.setText(recentRegister.getuGender());
        tv_signature.setText(recentRegister.getuIntro());
        tv_phone.setText(recentRegister.getuPhone());
        if(UserInfo.getUserInfo().getType()==UserInfo.CHARITY_ORG){
            tv_gender.setText("无");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_userid:
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
                break;
            case R.id.iv_avator:
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, IMAGE_REQUEST_CODE);
                showChoosePicDialog();
                break;
            case R.id.btn_back:
                //提交信息
                finish();
                break;
            case R.id.btn_submit_personal_info:
                submitInfo();
                break;
        }
    }

    private void submitInfo(){
        username = tv_name.getText().toString();
        usergender = tv_gender.getText().toString();
        userdesc = tv_signature.getText().toString();
        //userphone = Integer.valueOf(tv_phone.getText().toString());
        userimage = imageUpAndDownUtil.getReceived_filepath();

        //创建网络访问对象
        JSONObject main_json = new JSONObject();
        if(UserInfo.getUserInfo().getType()==UserInfo.CHARITY_USER){
            main_json.put("userName", username);
            main_json.put("userImage", userimage);
            main_json.put("userIntro", userdesc);
            main_json.put("userGender", usergender);
        }else {
            main_json.put("groupName", username);
            main_json.put("groupImage", userimage);
            main_json.put("groupIntro", userdesc);
        }

        Log.d(TAG, main_json.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(main_json.toJSONString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String phpUrl = null;
        if(UserInfo.getUserInfo().getType()==UserInfo.CHARITY_ORG){
            phpUrl = this.getString(R.string.URL_GROUP_INFO_MODIFY);
        }else {
            phpUrl = this.getString(R.string.URL_USER_INFO_MODIFY);
        }

        AsyncHttpUtil.post(getApplicationContext(), phpUrl, stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d(TAG, jsonObject.toString());
                if (code == 200){
                    //TODO get more JSON objects!
                    Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_LONG).show();
                    UserInfo.getUserInfo().setuIntro(userdesc);
                    UserInfo.getUserInfo().setuGender(usergender);
                    UserInfo.getUserInfo().setuAvatar(userimage);
                    UserInfo.getUserInfo().setuName(username);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "修改失败!请检查网络", Toast.LENGTH_LONG).show();
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "cannot connect to server!");
                Toast.makeText(getApplicationContext(), "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 选择本地照片
                        Log.d(TAG,"local");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                                Log.d(TAG,"check permission");
                                startPermissionsActivity();
                            } else {
                                Log.d(TAG,"start choose");
                                selectFromAlbum();
                            }
                        } else {
                            Log.d(TAG,"start choose");
                            selectFromAlbum();
                        }
                        isClickCamera = false;
                        break;
                    case 1: // 拍照
                        //检查权限(6.0以上做权限判断)
                        Log.d(TAG,"photo");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                                //Toast.makeText(InformationActivity.this, "版本太高了", Toast.LENGTH_LONG).show();
                                Log.d(TAG,"request");
                                startPermissionsActivity();
                            } else {
                                // Toast.makeText(InformationActivity.this, "请输入完整手机号码", Toast.LENGTH_LONG).show();
                                Log.d(TAG,"opencamera");
                                openCamera();
                            }
                        } else {
                            //Toast.makeText(InformationActivity.this, "版本低", Toast.LENGTH_LONG).show();
                            Log.d(TAG,"opencamera");
                            openCamera();
                        }
                        isClickCamera = true;
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void selectFromAlbum(){
        Log.d(TAG,"choosing");
        Intent openAlbumIntent = new Intent(
                Intent.ACTION_PICK);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
    }

    private void openCamera(){
        Log.d(TAG, "open camera");

        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempUri = FileProvider.getUriForFile(PersonalInfoActivity.this, "com.example.hfp.MicroCommonweal.activity.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            tempUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, TAKE_PICTURE);
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                PERMISSIONS);
    }

    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }else{
            Log.d(TAG, uri.toString());
        }
        Log.d(TAG, "start zooming");
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    protected void setImageToView(Intent data) {
        Log.d(TAG, "save image");
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            iv_avator.setImageBitmap(photo);

            String imagePath = Utils.savePhoto(photo, Environment.getExternalStorageDirectory().getAbsolutePath(), "avatar");
//            uploadPic(imagePath);
            imageUpAndDownUtil.testPostImage(imagePath);
        }
//        Bundle extras = data.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
//            headImage.setImageBitmap(photo);
//            uploadPic(photo);
//        }
    }

}
