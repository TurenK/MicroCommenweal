package com.example.hfp.MicroCommonweal.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageUpAndDownUtil {

    private Context context;
    private String received_filepath = null;
    private final String receiveurl = "http://39.106.113.247/dscj/";
    private final String handleurl = "http://39.106.113.247/dscj/handleImage.php";

    public ImageUpAndDownUtil(Context context){
        this.context = context;
    }

    public void initOKHTTP(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public void initCookie(Activity activity){
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(activity.getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public void testPostImage(String path){
        final File file=new File(path);

        compress(Bitmap.CompressFormat.JPEG, path,path.replace(".jpg","")+"_compressed.jpg", 50);
        path = path.replace(".jpg","")+"_compressed.jpg";

        Log.d("imageupanddown:", path+handleurl);
        OkHttpUtils.post()
                .url(handleurl)
                //.addParams("userid",userid)
                .addFile("file",path,file)//传递一张图片 (前面的字段和php协商好,保持一致都)
//                .addParams("imgNum",tag) // 个人根据需求添加的判断字段,直接无视...
//                .addParams("water","20") //...无视
//                .addParams("count","10") //..无视
//                .addParams("title",writing_title.getText().toString()) //editText取得的文字内容
//                .addParams("articles",writing_content.getText().toString()) //editText取得的文字内容
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws IOException {
                        return response.body().string();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("flag", "--------------------->UploadImageOnError: " +e.getMessage());
                        Toast.makeText(context, "图片上传失败!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(Object content, int id) {
                        try {
                            String string = (String)content;
                            JSONObject jsonObject = JSONObject.parseObject(string);
                            int code = jsonObject.getInteger("code");
                            String info = jsonObject.getString("message");
                            Log.d("uploadImage", jsonObject.toString());
                            if(code == 200){
                                Toast.makeText(context, "图片上传成功!", Toast.LENGTH_LONG).show();
                                received_filepath = jsonObject.getString("data");
                            } else
                                Toast.makeText(context, "图片上传失败!", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void testDownloadImage(String filepath, final ImageView imageView){
        if(filepath!=null){
        OkHttpUtils//
                .get()//
                .url(receiveurl+filepath.substring(2))//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),filepath.substring(6)) {

                    @Override
                    public void inProgress(float progress, long total , int id) {
                        Log.i("pengfeiwuer",String.valueOf(progress));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Picasso.with(context)
                                .load(R.drawable.loadfail)
                                .into(imageView);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        try{
                        Picasso.with(context)
                                .load(response)
                                .into(imageView);
                        }catch (Exception e){
                            Toast.makeText(context, "图片上传失败!"+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        }else {
            Picasso.with(context)
                    .load(R.drawable.loadfail)
                    .into(imageView);
        }
    }

    public String getReceived_filepath() {
        return received_filepath;
    }

    public void setReceived_filepath(String received_filepath) {
        this.received_filepath = received_filepath;
    }

    /**
     * 质量压缩
     *
     * @param format  图片格式   PNG，JPEG，WEBP
     * @param quality 图片的质量 1-100
     */
    public void compress(Bitmap.CompressFormat format, String inputpath, String outputpath,  int quality) {
        FileOutputStream fos = null;
        try {
            //得到一个储存路径
            File outputfile = new File(outputpath);
            //得到一个文件输入流
            fos = new FileOutputStream(outputfile);
            //开始压缩
            Bitmap bitmap = BitmapFactory.decodeFile(inputpath);
            bitmap.compress(format, quality, fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void compressBitmapToFile(Bitmap bmp, File file){
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 2;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
