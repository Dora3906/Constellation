package com.example.constellation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragment_Explore extends Fragment {

    private View view;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_explore,container,false);
            initViews();
        }

        return view;
    }

    private void initViews() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(new MyRecycleViewAdapter(getActivity()));
    }


    class MyRecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private Context context;

        private int[] functionImg = {R.drawable.weather,R.drawable.delivery,
                R.drawable.constellation};

        public MyRecycleViewAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_function,null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.imageButton.setImageResource(functionImg[position]);
            holder.imageButton.setOnClickListener(new MyClickListener(position));
        }

        @Override
        public int getItemCount() {
            return functionImg.length;
        }


        class MyClickListener implements View.OnClickListener {
            private int postion;

            public MyClickListener(int postion) {
                this.postion = postion;
            }

            @Override
            public void onClick(View v) {
                switch (postion){
                    case 0:
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout,new Fragment_Weather())
                                .addToBackStack(null)
                                .commit();
                        break;

                    case 1:
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout,new Fragment_Delivery())
                                .addToBackStack(null)
                                .commit();
                        break;

                    case 2:
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout,new Fragment_Search())
                                .addToBackStack(null)
                                .commit();
                        break;

                    default:
                        break;
                }
            }
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageButton imageButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.ibFunction);
        }

    }

}


