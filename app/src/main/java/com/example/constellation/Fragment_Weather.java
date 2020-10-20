package com.example.constellation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import bean.Weather;


public class Fragment_Weather extends Fragment {

    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    public static final String APPKEY = "268a246ad10b6fd7 ";
    public static final String URL = "https://api.jisuapi.com/weather/query";
    public static String city = "南京";

    public static AlertDialog dialog;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private View view;

    public static Weather weatherBean;
    public static Map<String, Integer> mapWeatherIcon =  new HashMap<>();
    private MyRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_weather, container, false);
            initViews(view);
        }
        return view;
    }

    private void verifyPermission(Activity activity) {
        int requestCode = 1;

        String[] permissions = { Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,};

        List<String> permissionRequested = new ArrayList<>();

        for (String strings : permissions) {
            try {
                int permission = ActivityCompat.checkSelfPermission(activity, strings);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    permissionRequested.add(strings);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!permissionRequested.isEmpty()){
            ActivityCompat.requestPermissions(activity,
                    permissionRequested.toArray(new String[permissionRequested.size()]) ,
                    requestCode);
        }

    }

    private void initViews(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        weatherBean = new Weather();

        verifyPermission(getActivity());
        getLocation();
        getWeatherIcon();

        swipeRefreshLayout.setColorSchemeResources(R.color.blue);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "加载" + city + "天气成功！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder>{

        private Context context;

        public MyRecyclerViewAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_weather, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
            holder.tvArea.setText(weatherBean.getResult().get("city"));
            holder.tvTemperature.setText(weatherBean.getResult().get("temp"));
            holder.tvWeather.setText(weatherBean.getResult().get("weather"));
            holder.tvQuality.setText(weatherBean.getAqi().get("quality"));
            holder.tvAqi.setText(weatherBean.getAqi().get("aqi"));

            holder.ivIcoToday.setImageResource(mapWeatherIcon.get(weatherBean.getDaily().get(0).get("img_day")));
            holder.tvWeatherToday.setText(weatherBean.getDaily().get(0).get("weather_day"));
            holder.tvMaxToday.setText(weatherBean.getDaily().get(0).get("temphigh"));
            holder.tvMinToday.setText(weatherBean.getDaily().get(0).get("templow"));

            holder.ivIcoTomorrow.setImageResource(mapWeatherIcon.get(weatherBean.getDaily().get(1).get("img_day")));
            holder.tvWeatherTomorrow.setText(weatherBean.getDaily().get(1).get("weather_day"));
            holder.tvMaxTomorrow.setText(weatherBean.getDaily().get(1).get("temphigh"));
            holder.tvMinTomorrow.setText(weatherBean.getDaily().get(1).get("templow"));

            holder.ivIcoWeek.setImageResource(mapWeatherIcon.get(weatherBean.getDaily().get(2).get("img_day")));
            holder.tvWeek.setText(weatherBean.getDaily().get(2).get("week"));
            holder.tvWeatherWeek.setText(weatherBean.getDaily().get(2).get("weather_day"));
            holder.tvMaxWeek.setText(weatherBean.getDaily().get(2).get("temphigh"));
            holder.tvMinWeek.setText(weatherBean.getDaily().get(2).get("templow"));

            holder.cardView.setCardBackgroundColor(Color.parseColor(weatherBean.getAqi().get("color")));

            holder.btnWeatherMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout,new Fragment_WeatherMore())
                            .addToBackStack(null)
                            .commit();
                }
            });

            holder.ibAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {

                            getData();

                            swipeRefreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(true);
                                    adapter.notifyDataSetChanged();
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });

                        }
                    });
                }
            });

            holder.tvArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showDialog();

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {

                            getData();

                            swipeRefreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(true);
                                    adapter.notifyDataSetChanged();
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });

                        }
                    });

                }
            });

            holder.tvSunrise.setText(weatherBean.getDaily().get(0).get("sunrise"));
            holder.tvSunset.setText(weatherBean.getDaily().get(0).get("sunset"));
            holder.tvWindPower.setText(weatherBean.getResult().get("windPower"));
            holder.tvWindDirect.setText(weatherBean.getResult().get("windDirect"));
            holder.tvHumidity.setText(weatherBean.getResult().get("humidity"));
            holder.tvPressure.setText(weatherBean.getResult().get("pressure"));

            holder.tvNameCondition.setText(weatherBean.getIndex().get(0).get("name"));
            holder.tvValueCondition.setText(weatherBean.getIndex().get(0).get("value"));
            holder.tvDetailCondition.setText(weatherBean.getIndex().get(0).get("detail"));

            holder.tvNameSport.setText(weatherBean.getIndex().get(1).get("name"));
            holder.tvValueSport.setText(weatherBean.getIndex().get(1).get("value"));
            holder.tvDetailSport.setText(weatherBean.getIndex().get(1).get("detail"));

            holder.tvNameUltraviolet.setText(weatherBean.getIndex().get(2).get("name"));
            holder.tvValueUltraviolet.setText(weatherBean.getIndex().get(2).get("value"));
            holder.tvDetailUltraviolet.setText(weatherBean.getIndex().get(2).get("detail"));
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.chosecity,null);
        builder.setView(linearLayout);
        dialog = builder.create();
        dialog.show();

        linearLayout.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etCity = linearLayout.findViewById(R.id.etCity);

                city = etCity.getText().toString();

                dialog.dismiss();
            }
        });

        linearLayout.findViewById(R.id.tvLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocation();

                dialog.dismiss();
            }
        });
    }

    private void getLocation(){

        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setNeedNewVersionRgc(true);
        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }

    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){

            String addr = location.getAddrStr();
            //获取详细地址信息
            String country = location.getCountry();
            //获取国家
            String province = location.getProvince();
            //获取省份
            String city1 = location.getCity();
            //获取城市
            String district = location.getDistrict();
            //获取区县
            String street = location.getStreet();
            //获取街道信息
            String adcode = location.getAdCode();
            //获取adcode
            String town = location.getTown();
            //获取乡镇信息

            //从手机定位中获得所在区县
            city = district;

            //关闭定位
            mLocationClient.stop();

            getData();

        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageButton ibAdd;
        public TextView tvArea;
        public TextView tvTemperature;
        public TextView tvWeather;
        public TextView tvQuality;
        public TextView tvAqi;
        public ImageView ivIcoToday;
        public TextView tvWeatherToday;
        public TextView tvMaxToday;
        public TextView tvMinToday;
        public ImageView ivIcoTomorrow;
        public TextView tvWeatherTomorrow;
        public TextView tvMaxTomorrow;
        public TextView tvMinTomorrow;
        public ImageView ivIcoWeek;
        public TextView tvWeek;
        public TextView tvWeatherWeek;
        public TextView tvMaxWeek;
        public TextView tvMinWeek;
        public Button btnWeatherMore;
        public CardView cardView;

        public TextView tvSunrise;
        public TextView tvSunset;
        public TextView tvWindPower;
        public TextView tvWindDirect;
        public TextView tvHumidity;
        public TextView tvPressure;
        public TextView tvNameCondition;
        public TextView tvValueCondition;
        public TextView tvDetailCondition;
        public TextView tvNameSport;
        public TextView tvValueSport;
        public TextView tvDetailSport;
        public TextView tvNameUltraviolet;
        public TextView tvValueUltraviolet;
        public TextView tvDetailUltraviolet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ibAdd = itemView.findViewById(R.id.ibAdd);
            tvArea = itemView.findViewById(R.id.tvArea);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvWeather = itemView.findViewById(R.id.tvWeather);
            tvQuality = itemView.findViewById(R.id.tvQuality);
            tvAqi = itemView.findViewById(R.id.tvAqi);
            ivIcoToday = itemView.findViewById(R.id.ivIcoToday);
            tvWeatherToday = itemView.findViewById(R.id.tvWeatherToday);
            tvMaxToday = itemView.findViewById(R.id.tvMaxToday);
            tvMinToday = itemView.findViewById(R.id.tvMinToday);
            ivIcoTomorrow = itemView.findViewById(R.id.ivIcoTomorrow);
            tvWeatherTomorrow = itemView.findViewById(R.id.tvWeatherTomorrow);
            tvMaxTomorrow = itemView.findViewById(R.id.tvMaxTomorrow);
            tvMinTomorrow = itemView.findViewById(R.id.tvMinTomorrow);
            ivIcoWeek = itemView.findViewById(R.id.ivIcoWeek);
            tvWeek = itemView.findViewById(R.id.tvWeek);
            tvWeatherWeek = itemView.findViewById(R.id.tvWeatherWeek);
            tvMaxWeek = itemView.findViewById(R.id.tvMaxWeek);
            tvMinWeek = itemView.findViewById(R.id.tvMinWeek);
            btnWeatherMore = itemView.findViewById(R.id.btnWeatherMore);
            cardView = itemView.findViewById(R.id.cardView);

            tvSunrise = itemView.findViewById(R.id.tvSunrise);
            tvSunset = itemView.findViewById(R.id.tvSunset);
            tvWindPower = itemView.findViewById(R.id.tvWindPower);
            tvWindDirect = itemView.findViewById(R.id.tvWindDirect);
            tvHumidity = itemView.findViewById(R.id.tvHumidity);
            tvPressure = itemView.findViewById(R.id.tvPressure);
            tvNameCondition = itemView.findViewById(R.id.tvName_condition);
            tvValueCondition = itemView.findViewById(R.id.tvValue_condition);
            tvDetailCondition = itemView.findViewById(R.id.tvDetail_condition);
            tvNameSport = itemView.findViewById(R.id.tvName_sport);
            tvValueSport = itemView.findViewById(R.id.tvValue_sport);
            tvDetailSport = itemView.findViewById(R.id.tvDetail_sport);
            tvNameUltraviolet = itemView.findViewById(R.id.tvName_ultraviolet);
            tvValueUltraviolet = itemView.findViewById(R.id.tvValue_ultraviolet);
            tvDetailUltraviolet = itemView.findViewById(R.id.tvDetail_ultraviolet);

        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                setData();
            }
        }
    };

    private void setData() {
        adapter = new MyRecyclerViewAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    public void getData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                String urlStr = null;

                try {
                    System.out.println(city);
                    urlStr = URL  + "?appkey=" + APPKEY + "&city=" + URLEncoder.encode(city, "utf-8");

                    System.out.println(urlStr);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    java.net.URL url = new URL(urlStr);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

                    String inputLine;
                    StringBuilder resultSB = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        resultSB.append(inputLine);
                        resultSB.append("\r\n");
                    }
                    in.close();
                    connection.disconnect();
                    result = resultSB.toString();

                    JSONObject json = JSONObject.fromObject(result);

                    if (json.getInt("status") != 0) {
                        System.out.println(json.getString("msg"));
                    } else {
                        JSONObject resultarr = json.optJSONObject("result");
                        String city = resultarr.getString("city");
                        String temp = resultarr.getString("temp");
                        String weather = resultarr.getString("weather");
                        String tempHigh = resultarr.getString("temphigh");
                        String tempLow = resultarr.getString("templow");
                        String img = resultarr.getString("img");
                        String humidity = resultarr.getString("humidity");
                        String pressure = resultarr.getString("pressure");
                        String windSpeed = resultarr.getString("windspeed");
                        String windDirect = resultarr.getString("winddirect");
                        String windPower = resultarr.getString("windpower");

                        Map<String,String> resultMap = new HashMap<>();
                        resultMap.put("city",city);
                        resultMap.put("temp",temp);
                        resultMap.put("weather",weather);
                        resultMap.put("tempHigh",tempHigh);
                        resultMap.put("tempLow",tempLow);
                        resultMap.put("img",img);
                        resultMap.put("humidity",humidity);
                        resultMap.put("pressure",pressure);
                        resultMap.put("windSpeed",windSpeed);
                        resultMap.put("windDirect",windDirect);
                        resultMap.put("windPower",windPower);

                        weatherBean.setResult(resultMap);

                        if (resultarr.opt("index") != null){
                            JSONArray index = resultarr.optJSONArray("index");
                            List<Map<String,String>> listIndex = new ArrayList<>();
                            for (int i = 0; i < index.size(); i++) {
                                JSONObject obj = (JSONObject) index.opt(i);
                                String iname = obj.getString("iname");
                                String ivalue = obj.getString("ivalue");
                                String detail = obj.getString("detail");

                                Map<String,String> map = new HashMap<>();
                                map.put("name",iname);
                                map.put("value",ivalue);
                                map.put("detail",detail);

                                listIndex.add(map);

                                System.out.println("指数:" + iname + " " + ivalue + " " + detail);
                            }

                            weatherBean.setIndex(listIndex);
                        }

                        if (resultarr.opt("aqi") != null) {
                            JSONObject aqi = resultarr.optJSONObject("aqi");
                            String so2 = aqi.getString("so2");
                            String so224 = aqi.getString("so224");
                            String no2 = aqi.getString("no2");
                            String no224 = aqi.getString("no224");
                            String co = aqi.getString("co");
                            String co24 = aqi.getString("co24");
                            String o3 = aqi.getString("o3");
                            String o38 = aqi.getString("o38");
                            String o324 = aqi.getString("o324");
                            String pm10 = aqi.getString("pm10");
                            String pm1024 = aqi.getString("pm1024");
                            String pm2_5 = aqi.getString("pm2_5");
                            String pm2_524 = aqi.getString("pm2_524");
                            String iso2 = aqi.getString("iso2");
                            String ino2 = aqi.getString("ino2");
                            String ico = aqi.getString("ico");
                            String io3 = aqi.getString("io3");
                            String io38 = aqi.getString("io38");
                            String ipm10 = aqi.getString("ipm10");
                            String ipm2_5 = aqi.getString("ipm2_5");
                            String aqi1 = aqi.getString("aqi");
                            String primarypollutant = aqi.getString("primarypollutant");
                            String quality = aqi.getString("quality");
                            String timepoint = aqi.getString("timepoint");

                            Map<String, String> mapAqi = new HashMap<>();
                            mapAqi.put("pm2_5",pm2_5);
                            mapAqi.put("ico",ico);
                            mapAqi.put("aqi",aqi1);
                            mapAqi.put("quality",quality);

                            System.out.println("空气质量指数:" + so2 + " " + so224 + " " + no2 + " " + no224 + " " + co + " " + co24
                                    + " " + o3 + " " + o38 + " " + o324 + " " + pm10 + " " + pm1024 + " " + pm2_5 + " "
                                    + pm2_524 + " " + iso2 + " " + ino2 + " " + ico + " " + io3 + " " + io38 + " " + ipm10 + " "
                                    + ipm2_5 + " " + aqi1 + " " + primarypollutant + " " + quality + " " + timepoint);

                            if (aqi.opt("aqiinfo") != null) {
                                JSONObject aqiinfo = aqi.optJSONObject("aqiinfo");
                                String level = aqiinfo.getString("level");
                                String color = aqiinfo.getString("color");
                                String affect = aqiinfo.getString("affect");
                                String measure = aqiinfo.getString("measure");

                                mapAqi.put("level",level);
                                mapAqi.put("color",color);
                                mapAqi.put("affect",affect);
                                mapAqi.put("measure",measure);

                                System.out.println(level + " " + color + " " + affect + " " + measure);
                            }

                            weatherBean.setAqi(mapAqi);
                        }

                        if (resultarr.opt("daily") != null) {
                            JSONArray daily = resultarr.optJSONArray("daily");
                            List<Map<String, String>> listDaily = new ArrayList<>();

                            for (int i = 0; i < daily.size(); i++) {
                                JSONObject obj = (JSONObject) daily.opt(i);
                                Map<String, String> mapDaily = new HashMap<>();

                                String date1 = obj.getString("date");
                                String week1 = obj.getString("week");
                                String sunrise = obj.getString("sunrise");
                                String sunset = obj.getString("sunset");

                                mapDaily.put("date",date1);
                                mapDaily.put("week",week1);
                                mapDaily.put("sunrise",sunrise);
                                mapDaily.put("sunset",sunset);

                                System.out.println("未来几天天气:" + date1 + " " + week1 + " " + sunrise + " " + sunset);

                                if (obj.opt("night") != null) {
                                    JSONObject night = (JSONObject) obj.opt("night");
                                    String weather1 = night.getString("weather");
                                    String templow = night.getString("templow");
                                    String img1 = night.getString("img");
                                    String winddirect1 = night.getString("winddirect");
                                    String windpower1 = night.getString("windpower");

                                    mapDaily.put("weather_night",weather1);
                                    mapDaily.put("templow",templow);
                                    mapDaily.put("img_night",img1);
                                    mapDaily.put("winddirect_night",winddirect1);
                                    mapDaily.put("windpower_night",windpower1);

                                    System.out.println(
                                            weather1 + " " + templow + " " + img1 + " " + winddirect1 + " " + windpower1);
                                }

                                if (obj.opt("day") != null) {
                                    JSONObject day = obj.optJSONObject("day");
                                    String weather1 = day.getString("weather");
                                    String temphigh = day.getString("temphigh");
                                    String img1 = day.getString("img");
                                    String winddirect1 = day.getString("winddirect");
                                    String windpower1 = day.getString("windpower");

                                    mapDaily.put("weather_day",weather1);
                                    mapDaily.put("temphigh",temphigh);
                                    mapDaily.put("img_day",img1);
                                    mapDaily.put("winddirect_day",winddirect1);
                                    mapDaily.put("windpower_day",windpower1);


                                    System.out.println(
                                            weather1 + " " + temphigh + " " + img1 + " " + winddirect1 + " " + windpower1);
                                }

                                listDaily.add(mapDaily);
                            }

                            weatherBean.setDaily(listDaily);

                        }


                        if (resultarr.opt("hourly") != null) {
                            JSONArray hourly = resultarr.optJSONArray("hourly");
                            List<Map<String, String>> listHourly = new ArrayList<>();

                            for (int i = 0; i < hourly.size(); i++) {
                                JSONObject obj = (JSONObject) hourly.opt(i);
                                Map<String, String> mapHourly = new HashMap<>();

                                String time = obj.getString("time");
                                String weather1 = obj.getString("weather");
                                String temp1 = obj.getString("temp");
                                String img1 = obj.getString("img");

                                mapHourly.put("time",time);
                                mapHourly.put("weather",weather1);
                                mapHourly.put("temp",temp1);
                                mapHourly.put("img",img1);

                                listHourly.add(mapHourly);
                                System.out.println("未来几小时天气:" + time + " " + weather1 + " " + temp1 + " " + img1);
                            }

                            weatherBean.setHourly(listHourly);
                        }

                    }

                    handler.sendEmptyMessageDelayed(1,100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getWeatherIcon() {

        mapWeatherIcon.put("0",R.drawable.w0);
        mapWeatherIcon.put("1",R.drawable.w1);
        mapWeatherIcon.put("2",R.drawable.w2);
        mapWeatherIcon.put("3",R.drawable.w3);
        mapWeatherIcon.put("4",R.drawable.w4);
        mapWeatherIcon.put("5",R.drawable.w5);
        mapWeatherIcon.put("6",R.drawable.w6);
        mapWeatherIcon.put("7",R.drawable.w7);
        mapWeatherIcon.put("8",R.drawable.w8);
        mapWeatherIcon.put("9",R.drawable.w9);
        mapWeatherIcon.put("10",R.drawable.w10);
        mapWeatherIcon.put("11",R.drawable.w11);
        mapWeatherIcon.put("12",R.drawable.w12);
        mapWeatherIcon.put("13",R.drawable.w13);
        mapWeatherIcon.put("14",R.drawable.w14);
        mapWeatherIcon.put("15",R.drawable.w15);
        mapWeatherIcon.put("16",R.drawable.w16);
        mapWeatherIcon.put("17",R.drawable.w17);
        mapWeatherIcon.put("18",R.drawable.w18);
        mapWeatherIcon.put("19",R.drawable.w19);
        mapWeatherIcon.put("20",R.drawable.w20);
        mapWeatherIcon.put("21",R.drawable.w21);
        mapWeatherIcon.put("22",R.drawable.w22);
        mapWeatherIcon.put("23",R.drawable.w23);
        mapWeatherIcon.put("24",R.drawable.w24);
        mapWeatherIcon.put("25",R.drawable.w25);
        mapWeatherIcon.put("26",R.drawable.w26);
        mapWeatherIcon.put("27",R.drawable.w27);
        mapWeatherIcon.put("28",R.drawable.w28);
        mapWeatherIcon.put("29",R.drawable.w29);
        mapWeatherIcon.put("30",R.drawable.w30);
        mapWeatherIcon.put("31",R.drawable.w31);
        mapWeatherIcon.put("32",R.drawable.w32);
        mapWeatherIcon.put("49",R.drawable.w49);
        mapWeatherIcon.put("53",R.drawable.w53);
        mapWeatherIcon.put("54",R.drawable.w54);
        mapWeatherIcon.put("55",R.drawable.w55);
        mapWeatherIcon.put("56",R.drawable.w56);
        mapWeatherIcon.put("57",R.drawable.w57);
        mapWeatherIcon.put("58",R.drawable.w58);
        mapWeatherIcon.put("99",R.drawable.w99);
        mapWeatherIcon.put("301",R.drawable.w301);
        mapWeatherIcon.put("302",R.drawable.w302);
    }
}
