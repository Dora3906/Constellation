package com.example.constellation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 14323
 */
public class Fragment_Search extends Fragment {

    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_search,container,false);

            initViews(view);
        }

        return view;
    }

    private void initViews(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MyRecycleAdapter(getActivity()));
    }

}

class MyRecycleAdapter extends RecyclerView.Adapter<MyRecyclerHolder>{

    private Context context;

    public MyRecycleAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_constellation,null);
        MyRecyclerHolder recyclerHolder = new MyRecyclerHolder(view);
        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerHolder holder, int position) {
        holder.imageView.setImageResource(DataActivity.constellations[position]);
        holder.tvConstellation.setText(DataActivity.constellationsName[position]);
        holder.tvContent.setText(getContent(position));
    }

    private String getContent(int position) {
        String result = "";
        String[] resultLine;
        String content = "";

        byte[] buffer;

        InputStream inputStream = context.getResources().openRawResource(R.raw.constellation_content);

        try{
            buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            result = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        resultLine = result.split("\n");
        content = resultLine[position].split("：")[1];

        return content;
    }

    @Override
    public int getItemCount() {
        return DataActivity.constellations.length;
    }
}

class MyRecyclerHolder extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public TextView tvConstellation;
    public TextView tvContent;

    public MyRecyclerHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.img);
        tvConstellation = itemView.findViewById(R.id.tvConstellation);
        tvContent = itemView.findViewById(R.id.tvContent);
    }
}
