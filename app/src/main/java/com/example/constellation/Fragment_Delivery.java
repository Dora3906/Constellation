package com.example.constellation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Fragment_Delivery extends Fragment {

    public static final String APPKEY = "268a246ad10b6fd7";
    public static final String URL = "https://api.jisuapi.com/express/query";
    public static final String TYPE = "auto";
    public String number = "";
    public String result = "";
    private String typeName = "";
    private String logo = "";
    private int deliveryStatus ;
    private List<Map<String, String>> contents = new ArrayList<>();

    private EditText etNumber;
    private ImageButton btnSearch;
    private TextView tvTypeName, tvNumber;
    private RecyclerView rvContent;

    private View view;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                setData();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_delivery, null);
            initViews(view);
        }
        return view;
    }

    private void setData() {
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvContent.getLayoutManager();
        rvContent.setLayoutManager(linearLayoutManager);
        rvContent.setAdapter(new MyRecyclerAdapter(getActivity()));
        tvTypeName.setText(typeName);
    }

    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerHolder> {

        private Context context;

        public MyRecyclerAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public MyRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_delivery, null);

            final MyRecyclerHolder holder = new MyRecyclerHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyRecyclerHolder holder, int position) {
            holder.tvTime.setText(contents.get(position).get("time"));
            holder.tvStatus.setText(contents.get(position).get("status"));
        }

        @Override
        public int getItemCount() {
            return contents.size();
        }
    }

    class MyRecyclerHolder extends RecyclerView.ViewHolder {

        public TextView tvTime;
        public TextView tvStatus;

        public MyRecyclerHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    private void initViews(View view) {
        etNumber = view.findViewById(R.id.etNumber);
        btnSearch = view.findViewById(R.id.btnSearch);
        tvTypeName = view.findViewById(R.id.tvTypeName);
        tvNumber = view.findViewById(R.id.tvNumber);
        rvContent = view.findViewById(R.id.rvContent);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                tvTypeName.setText(typeName);
                tvNumber.setText(number);
            }
        });
    }

    private void getData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                contents.clear();
                String urlStr = null;
                number = etNumber.getText().toString();
                urlStr = URL + "?appkey=" + APPKEY + "&type=" + TYPE + "&number=" + number;
                System.out.println(urlStr);

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
                        JSONObject resultarr = (JSONObject) json.opt("result");
                        if (resultarr != null) {

                            typeName = resultarr.getString("typename");
                            logo = resultarr.getString("logo");
                            deliveryStatus = resultarr.getInt("deliverystatus");

                            if (resultarr.opt("list") != null) {
                                JSONArray list = resultarr.optJSONArray("list");
                                for (int j = 0; j < list.size(); j++) {
                                    JSONObject list_obj = (JSONObject) list.opt(j);
                                    if (list_obj != null) {
                                        String time = list_obj.getString("time");
                                        String status = list_obj.getString("status");

                                        Map<String,String> map = new HashMap<>();
                                        map.put("time",time);
                                        map.put("status",status);

                                        contents.add(map);
                                        System.out.println(time + " " + status);

                                    }
                                }
                            }
                        }

                        handler.sendEmptyMessageDelayed(1,100);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
