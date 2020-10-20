package com.example.constellation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.litepal.LitePal;

import arcSoft.activity.RegisterAndRecognizeActivity;
import bean.User;

public class Fragment_Person extends Fragment {

    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_person,container,false);

            initViews(view);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews(view);
    }

    private void initViews(View view) {
        ImageView ivConstellation;
        TextView tvConstellation;
        TextView tvUserName;
        TextView tvBirthday;
        TextView tvGender;
        Button btnUpdate;
        Button btnLogout;
        Button btnDel;
        final Button btnAddFace;

        ivConstellation = view.findViewById(R.id.ivConstellation);
        tvConstellation = view.findViewById(R.id.tvConstellation);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvBirthday = view.findViewById(R.id.tvBirthday);
        tvGender = view.findViewById(R.id.tvGender);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnDel = view.findViewById(R.id.btnDel);
        btnAddFace = view.findViewById(R.id.btnAddFace);

        final User user = LitePal.where("name = ?",DataActivity.userName).find(User.class).get(0);
        ivConstellation.setImageResource(DataActivity.constellations[DataActivity.astroid-1]);
        tvConstellation.setText(user.getConstellation());
        tvUserName.setText(user.getName());
        tvBirthday.setText(user.getBirthday());
        tvGender.setText(user.getGender());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RegisterActivity.class);
                intent.putExtra("user",user);
                intent.putExtra("update",true);
                startActivity(intent);
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.ic_error_outline_black_24dp);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (LitePal.delete(User.class,DataActivity.id) > 0){
                            Toast.makeText(getActivity(), "删除账号成功！", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(),LoginActivity.class));
                        } else {
                            Toast.makeText(getActivity(), "删除账号失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNeutralButton("取消",null);
                builder.setTitle("删除账号");
                builder.setMessage("你确认要删除该账号吗？删除后你将无法用该账号进行登录！");
                builder.show();

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });

        btnAddFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterAndRecognizeActivity.class);
                intent.putExtra("userName",DataActivity.userName);
                startActivity(intent);
            }
        });
    }

}
