package com.example.constellation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class SplashActivity extends AppCompatActivity {

    public static Bitmap bitmap;

    private ImageView background;
    private TextView tvQuote;
    private ProgressBar progressBar;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && progressBar.getProgress() < progressBar.getMax()){
                progressBar.setProgress(progressBar.getProgress()+2);
                handler.sendEmptyMessageDelayed(1,50);
            } else if (progressBar.getProgress() >= progressBar.getMax()){
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            } else if (msg.what == 2){
//                background.setImageBitmap((Bitmap) msg.obj);
            } else if (msg.what == 3){
                String res = msg.obj.toString().split("\r")[0] + "。";
                System.out.println(res);
                tvQuote.setText(res);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LitePal.getDatabase();

        MyApplication.activityList.add(this);
        initView();
        setData();
    }

    public void setData() {
        //获取背景图片
        new Thread(new Runnable() {
            final String urlStr = "https://bing.ioliu.cn/v1/rand?w=720&h=1080";
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(3000);
                    int code = connection.getResponseCode();
                    if (code == 200){
                        InputStream inputStream = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        Message message = Message.obtain();
                        message.obj = bitmap;
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //获取名人语录
        new Thread(new Runnable() {
            final String urlStr = "https://www.inqingdao.cn/hitokoto/";
            @Override
            public void run() {
                try {
                    java.net.URL url = new URL(urlStr);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

                    String inputLine;
                    StringBuilder result = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        result.append(inputLine);
                        result.append("\r\n");
                    }
                    in.close();
                    connection.disconnect();

                    Message message = Message.obtain();
                    message.obj = result.toString();
                    message.what = 3;
                    handler.sendMessage(message);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initView() {
        tvQuote = findViewById(R.id.tvQuote);
        progressBar = findViewById(R.id.progressBar);
        background = findViewById(R.id.background);

        handler.sendEmptyMessageDelayed(1,1);
    }
}
