package com.example.constellation;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

import java.util.List;

import bean.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ScrollView bg_register;
    private TextView tvRegister;
    private EditText editTextUserName;
    private EditText editTextPwd;
    private DatePicker dpBirthday;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private Button btnRegister;
    private ImageButton btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MyApplication.activityList.add(this);
        initViews();
        getData();
        setListener();
    }

    private void getData() {
        Intent intent = getIntent();
        boolean isUpdate = intent.getBooleanExtra("update",false);

        if (isUpdate){
            tvRegister.setText("修改资料");
            btnRegister.setText("确定");

            User user = (User) intent.getSerializableExtra("user");
            setData(user);
        }
    }

    private void setData(User user) {
        String[] birthday = user.getBirthday().split("-");
        int birthdayYear = Integer.parseInt(birthday[0]);
        int birthdayMonth = Integer.parseInt(birthday[1]);
        int birthdayDay = Integer.parseInt(birthday[2]);

        editTextUserName.setText(user.getName());
        editTextPwd.setText(user.getPassword());
        dpBirthday.updateDate(birthdayYear,birthdayMonth-1,birthdayDay);
        if ("女".equals(user.getGender())){
            radioButtonFemale.setChecked(true);
        }
    }

    private void setListener() {
        btnRegister.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initViews() {
        bg_register = findViewById(R.id.bg_register);
        tvRegister = findViewById(R.id.tvRegister);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPwd =  findViewById(R.id.editTextPwd);
        dpBirthday =  findViewById(R.id.dpBirthday);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);
        btnRegister =  findViewById(R.id.btnRegister);
        btnCancel =  findViewById(R.id.btnCancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                doRegister();
                break;

            case R.id.btnCancel:
                finish();
                break;

            default:break;
        }
    }

    private void doRegister() {

        boolean isValid;
        String userName = editTextUserName.getText().toString();
        String password = editTextPwd.getText().toString();
        String gender = getGender();
        int birthdayYear = dpBirthday.getYear();
        int birthdayMonth = dpBirthday.getMonth() + 1;
        int birthdayDay = dpBirthday.getDayOfMonth();
        String birthday = birthdayYear + "-" + birthdayMonth + "-" +birthdayDay;
        String constellation = getConstellation(birthdayMonth,birthdayDay);
        int astroid = getAstroicID(constellation);

        isValid = check(userName,password);

        if (isValid){

            User user = new User(userName,password,gender,birthday,constellation,astroid);

            if ("注册".equals(btnRegister.getText().toString())){
                user.save();
                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            } else {
                
                ContentValues values = new ContentValues();
                values.put("name",user.getName());
                values.put("password",user.getPassword());
                values.put("gender",user.getGender());
                values.put("birthday",user.getBirthday());
                values.put("constellation",user.getConstellation());
                values.put("astroid",user.getAstroid());
                
                if (LitePal.update(User.class,values,DataActivity.id) > 0){
                    Toast.makeText(RegisterActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                    DataActivity.userName = values.get("name").toString();
                    DataActivity.astroid = (int) values.get("astroid");
                    startActivity(new Intent(RegisterActivity.this,DataActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
                
                
            }


        }
    }

    private int getAstroicID(String constellation) {
        for (int i = 0; i < DataActivity.constellationsName.length ; i++) {
            if (constellation.equals(DataActivity.constellationsName[i])){
                return i + 1;
            }
        }

        return -1;
    }

    private boolean check(String userName, String password) {
        boolean isValid = true;

        if ("".equals(userName)){
            Toast.makeText(RegisterActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if ("".equals(password)){
            Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        List<User> users = LitePal.where("name = ?",userName).find(User.class);
        
        if ("注册".equals(btnRegister.getText().toString()) && users.size() > 0){
            isValid = false;
            Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }


    private String getGender() {
        String gender = "男";

        if (radioButtonFemale.isChecked()){
            gender = "女";
        }

        return gender;
    }

    private String getConstellation(int month, int day) {
        String star = "";
        if (month == 1 && day >= 20 || month == 2 && day <= 18) {
            star = "水瓶座";
        }
        if (month == 2 && day >= 19 || month == 3 && day <= 20) {
            star = "双鱼座";
        }
        if (month == 3 && day >= 21 || month == 4 && day <= 19) {
            star = "白羊座";
        }
        if (month == 4 && day >= 20 || month == 5 && day <= 20) {
            star = "金牛座";
        }
        if (month == 5 && day >= 21 || month == 6 && day <= 21) {
            star = "双子座";
        }
        if (month == 6 && day >= 22 || month == 7 && day <= 22) {
            star = "巨蟹座";
        }
        if (month == 7 && day >= 23 || month == 8 && day <= 22) {
            star = "狮子座";
        }
        if (month == 8 && day >= 23 || month == 9 && day <= 22) {
            star = "处女座";
        }
        if (month == 9 && day >= 23 || month == 10 && day <= 23) {
            star = "天秤座";
        }
        if (month == 10 && day >= 24 || month == 11 && day <= 22) {
            star = "天蝎座";
        }
        if (month == 11 && day >= 23 || month == 12 && day <= 21) {
            star = "射手座";
        }
        if (month == 12 && day >= 22 || month == 1 && day <= 19) {
            star = "摩羯座";
        }
        return star;
    }
}
