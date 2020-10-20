package com.example.constellation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.sf.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import arcSoft.activity.ChooseFunctionActivity;

/**
 * @author 14323
 */
public class MainActivity extends AppCompatActivity {

    private static Bitmap bitmap;
    private List<Fragment> fragmentList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private LinearLayout linearLayout;
    private boolean isPause = true;
    private String musicUrl;
    private String musicPicUrl;
    private String musicName;
    private String artistsName;
    private MediaPlayer mediaPlayer;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                linearLayout.setBackground(new BitmapDrawable(bitmap));
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handlerMusic = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                Uri uri = Uri.parse(musicUrl);

                System.out.println(uri.toString());
                mediaPlayer = MediaPlayer.create(MainActivity.this,uri);

                mediaPlayer.start();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication.activityList.add(this);

        initViews();
        setListener();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("关于");
                StringBuilder about = new StringBuilder();
                about.append("学号：");
                about.append("1713212035");
                about.append("\n");
                about.append("姓名：");
                about.append("赵玉龙");
                about.append("\n");
                about.append("功能介绍：");
                about.append("无");
                about.append("\n");
                about.append("版本：");
                about.append("0.0.1");
                builder.setMessage(about);
                builder.setPositiveButton("ok",null);
                builder.show();
                break;

            case R.id.DKGRAY:
                linearLayout.setBackgroundColor(Color.DKGRAY);
                break;

            case R.id.GRAY:
                linearLayout.setBackgroundColor(Color.GRAY);
                break;

            case R.id.WHITE:
                linearLayout.setBackgroundColor(Color.WHITE);
                break;

            case R.id.IMG:
                linearLayout.setBackground(getDrawable(R.drawable.bg));
                break;

            case R.id.RANDOM:
                getBitMap();
                break;

            case R.id.switchMusic:
                playMusic();
                break;

            case R.id.faceSetting:
                startActivity(new Intent(MainActivity.this, ChooseFunctionActivity.class));
                break;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBitMap() {
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
                        bitmap =  BitmapFactory.decodeStream(inputStream);
                        handler.sendEmptyMessageDelayed(1,1);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void playMusic(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        } else if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

//    private void playMusic() {
//        getMusic();
//    }

    private void getMusic(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlStr = "https://api.uomg.com/api/rand.music?sort=%E7%83%AD%E6%AD%8C%E6%A6%9C&format=json";
                try {
                    URL url = new URL(urlStr);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String inputLine;
                    StringBuilder result = new StringBuilder();
                    while ((inputLine = in.readLine()) != null){
                        result.append(inputLine);
                        result.append("\r\n");
                    }
                    in.close();
                    connection.disconnect();

                    JSONObject jsonObject = JSONObject.fromObject(result.toString());

                    if(jsonObject.getInt("code") != 1){
                        Toast.makeText(MainActivity.this, "网络出错！", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject data = jsonObject.optJSONObject("data");

                        String name = data.getString("name");
                        String url1 = data.getString("url");
                        String picurl = data.getString("picurl");
                        String artistsname = data.getString("artistsname");

                        musicName = name;
                        musicUrl = url1;
                        musicPicUrl = picurl;
                        artistsName = artistsname;

                        handlerMusic.sendEmptyMessageDelayed(1,100);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setListener() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                switch (itemId){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout,fragmentList.get(0),"home")
                                .addToBackStack(null)
                                .commit();
                        break;

                    case R.id.search:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout,fragmentList.get(1),"search")
                                .addToBackStack(null)
                                .commit();
                        break;

                    case R.id.ai:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout,fragmentList.get(2),"ai")
                                .addToBackStack(null)
                                .commit();
                        break;

                    case R.id.person:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout,fragmentList.get(3),"person")
                                .addToBackStack(null)
                                .commit();
                        break;


                    default:break;
                }
                return true;
            }
        });

    }

    @SuppressLint("InflateParams")
    private void initViews() {
        linearLayout = findViewById(R.id.linearLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        fragmentList.add(new Fragment_Home());
        fragmentList.add(new Fragment_Explore());
        fragmentList.add(new Fragment_AI());
        fragmentList.add(new Fragment_Person());

        getSupportFragmentManager().beginTransaction().
                add(R.id.frameLayout,fragmentList.get(0),"home").addToBackStack(null).commit();

        showDialog();

    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("授权提醒");
        builder.setIcon(R.drawable.waring);
        builder.setMessage("本应用需要获得所需权限方可正常运行！");
        builder.setPositiveButton("授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                verifyPermission(MainActivity.this);
            }
        });
        builder.show();
    }

    private void verifyPermission(Activity activity) {
        int requestCode = 1;
        //请求码

        String[] permissions = { Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,};
        //所需要申请的权限

        List<String> permissionRequested = new ArrayList<>();
        //未得到授权的权限（需要进去动态授权）

        for (String strings : permissions) {
            //遍历所有需要的权限，将没有获得权限的权限放到permissionRequested列表中，进行一次性申请
            try {
                int permission = ActivityCompat.checkSelfPermission(activity, strings);
                //检查是否有此权限
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    permissionRequested.add(strings);
                    //若没有此权限，则将此权限放入permissionRequested以待申请
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //对没有得到用户授权的权限进行动态申请
        if (!permissionRequested.isEmpty()){
            ActivityCompat.requestPermissions(activity,
                    permissionRequested.toArray(new String[permissionRequested.size()]) ,
                    requestCode);
        }

    }

    public void getCity(View view){
        TextView textView = (TextView) view;

        Fragment_Weather.city = textView.getText().toString();

        Fragment_Weather.dialog.dismiss();

    }
}

