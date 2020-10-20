package com.example.constellation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import net.sf.json.JSONObject;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import bean.Fortune;
import bean.User;

public class DataActivity extends AppCompatActivity {

    public static String userName;
    public static long id;
    public static int astroid;
    public static Fortune fortune;

    public static int[] constellations = {
            R.drawable.baiyang,R.drawable.jinniu,R.drawable.shuangzi,
            R.drawable.juxie,R.drawable.shizi,R.drawable.chunv,
            R.drawable.tianping,R.drawable.tianxie,R.drawable.sheshou,
            R.drawable.mojie,R.drawable.shuiping,R.drawable.shuangyu
    };

    public static String[] constellationsName = {
            "白羊座","金牛座","双子座",
            "巨蟹座","狮子座","处女座",
            "天秤座","天蝎座","射手座",
            "魔羯座","水瓶座","双鱼座"
    };


    private static String httpRes = null;

    public static final String APPKEY = "268a246ad10b6fd7  ";
    public static final String URL = "https://api.jisuapi.com/astro/fortune";

    public static String date = "2020-5-21";

    @SuppressLint("HandlerLeak")
    private  Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1){
                getFortune();
            } else if (msg.what == 2){
                startActivity(new Intent(DataActivity.this,MainActivity.class));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.activityList.add(this);

        getData();
    }

    public void getData() {
        System.out.println(userName);
        User user = LitePal.where("name = ?",userName).find(User.class).get(0);
        astroid = user.getAstroid();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        date = simpleDateFormat.format(currentDate);
        System.out.println(date);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String realurl = URL + "?astroid=" + astroid + "&date=" + date + "&appkey=" + APPKEY;
                    java.net.URL url = new URL(realurl);
                    System.out.println(realurl);
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
                    httpRes = result.toString();
                    System.out.println(httpRes);

                    handler.sendEmptyMessageDelayed(1,100);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getFortune() {
        String result = null;
        Map<String,Object> fYear = new HashMap<>();
        Map<String,Object> fWeek = new HashMap<>();
        Map<String,Object> fToday = new HashMap<>();
        Map<String,Object> fTomorrow = new HashMap<>();
        Map<String,Object> fMonth = new HashMap<>();

        try {
            result = httpRes;
            JSONObject json = JSONObject.fromObject(result);
            System.out.println("");
            if (json.getInt("status") != 0) {
                System.out.println(json.getString("msg"));
            } else {
                JSONObject resultarr =  json.optJSONObject("result");
                String astroid = resultarr.getString("astroid");
                String astroname = resultarr.getString("astroname");
                System.out.println(astroid + " " + astroname);
                if (resultarr.opt("year") != null) {
                    JSONObject year =  resultarr.optJSONObject("year");
                    String date = year.getString("date");
                    String summary = year.getString("summary");
                    String money = year.getString("money");
                    String career = year.getString("career");
                    String love = year.getString("love");

                    fYear.put("date",date);
                    fYear.put("summary",summary);
                    fYear.put("money",money);
                    fYear.put("career",career);
                    fYear.put("love",love);
                    System.out.println(date + " " + summary + " " + money + " " + career + " " + love);
                    System.out.println(fYear.get("date") + " " + fYear.get("summary") + " " +fYear.get("money") + " " + fYear.get("career") + " " + fYear.get("love"));
                }
                if (resultarr.opt("week") != null) {
                    JSONObject week = resultarr.optJSONObject("week");
                    String date = week.getString("date");
                    String money = week.getString("money");
                    String career = week.getString("career");
                    String love = week.getString("love");
                    String health = week.getString("health");

                    fWeek.put("date",date);
                    fWeek.put("money",money);
                    fWeek.put("career",career);
                    fWeek.put("love",love);
                    fWeek.put("health",health);
                    System.out.println(date + " " + money + " " + career + " " + love + " " + health + " " );
                }
                if (resultarr.opt("today") != null) {
                    JSONObject today = resultarr.optJSONObject("today");
                    String date = today.getString("date");
                    String presummary = today.getString("presummary");
                    String star = today.getString("star");
                    String color = today.getString("color");
                    String number = today.getString("number");
                    String summary = today.getString("summary");
                    String money = today.getString("money");
                    String career = today.getString("career");
                    String love = today.getString("love");
                    String health = today.getString("health");

                    fToday.put("date",date);
                    fToday.put("presummary",presummary);
                    fToday.put("star",star);
                    fToday.put("color",color);
                    fToday.put("number",number);
                    fToday.put("summary",summary);
                    fToday.put("money",money);
                    fToday.put("career",career);
                    fToday.put("love",love);
                    fToday.put("health",health);
                    System.out.println(date + " " + presummary + " " + star + " " + color + " " + number + " " + summary
                            + " " + money + " " + career + " " + love + " " + health);
                }
                if (resultarr.opt("tomorrow") != null) {
                    JSONObject tomorrow = resultarr.optJSONObject("tomorrow");
                    String date = tomorrow.getString("date");
                    String presummary = tomorrow.getString("presummary");
                    String star = tomorrow.getString("star");
                    String color = tomorrow.getString("color");
                    String number = tomorrow.getString("number");
                    String summary = tomorrow.getString("summary");
                    String money = tomorrow.getString("money");
                    String career = tomorrow.getString("career");
                    String love = tomorrow.getString("love");
                    String health = tomorrow.getString("health");

                    fTomorrow.put("date",date);
                    fTomorrow.put("presummary",presummary);
                    fTomorrow.put("star",star);
                    fTomorrow.put("color",color);
                    fTomorrow.put("number",number);
                    fTomorrow.put("summary",summary);
                    fTomorrow.put("money",money);
                    fTomorrow.put("career",career);
                    fTomorrow.put("love",love);
                    fTomorrow.put("health",health);
                    System.out.println(date + " " + presummary + " " + star + " " + color + " " + number + " " + summary
                            + " " + money + " " + career + " " + love + " " + health);
                }
                if (resultarr.opt("month") != null) {
                    JSONObject month = resultarr.optJSONObject("month");
                    String date = month.getString("date");
                    String summary = month.getString("summary");
                    String money = month.getString("money");
                    String career = month.getString("career");
                    String love = month.getString("love");

                    fMonth.put("date",date);
                    fMonth.put("summary",summary);
                    fMonth.put("money",money);
                    fMonth.put("career",career);
                    fMonth.put("love",love);
                    System.out.println(date + " " + summary + " " + money + " " + career + " " + love);
                }

                fortune = new Fortune(fYear,fWeek,fToday,fTomorrow,fMonth);

                handler.sendEmptyMessageDelayed(2,100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
