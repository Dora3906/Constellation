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

/**
 * @author 14323
 */
public class Fragment_AI extends Fragment {

    public static final String APPKEY = "268a246ad10b6fd7";
    public static final String URL = "https://api.jisuapi.com/iqa/query";
    public String question = "";
    public String result = "";
    private List<Map<String,String>> contents = new ArrayList<>();

    private EditText etQuestion;
    private ImageButton btnSearch;
    private RecyclerView rvContent;

    private View view;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_ai,null);
            initViews(view);
        }
        return view;
    }

    private void setData() {
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvContent.getLayoutManager();
        linearLayoutManager.setStackFromEnd(true);
        rvContent.setLayoutManager(linearLayoutManager);
        rvContent.setAdapter(new MyRecyclerAdapter(getActivity()));
    }

    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerHolder>{

        private Context context;

        public MyRecyclerAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public MyRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_content,parent,false);

            return new MyRecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyRecyclerHolder holder, int position) {
            holder.tvQuestion.setText(contents.get(position).get("question"));
            holder.tvReponse.setText(contents.get(position).get("response"));
        }

        @Override
        public int getItemCount() {
            return contents.size();
        }
    }

    class MyRecyclerHolder extends RecyclerView.ViewHolder{

        public TextView tvQuestion;
        public TextView tvReponse;

        public MyRecyclerHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvReponse = itemView.findViewById(R.id.tvResponse);
        }
    }

    private void initViews(View view) {

        etQuestion = view.findViewById(R.id.etQuestion);
        btnSearch = view.findViewById(R.id.btnSearch);
        rvContent = view.findViewById(R.id.rvContent);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void getData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlStr = null;
                question = etQuestion.getText().toString();
                try {
                    urlStr = URL + "?appkey=" + APPKEY + "&question=" + URLEncoder.encode(question, "utf-8");
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
                        String type = resultarr.getString("type");
                        String content = resultarr.getString("content");
                        String relquestion = resultarr.getString("relquestion");
                        result = content;
                        System.out.println(result);
                        System.out.println(type + " " + content + " " + relquestion);

                        Map<String,String> map = new HashMap<>();

                        map.put("question",question);
                        map.put("response",result);

                        contents.add(map);

                        handler.sendEmptyMessageDelayed(1,10);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
