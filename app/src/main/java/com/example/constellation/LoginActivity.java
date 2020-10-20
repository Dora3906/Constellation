package com.example.constellation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.List;

import arcSoft.activity.RegisterAndRecognizeActivity;
import bean.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout bg_login;
    private EditText editTextUserName;
    private EditText editTextPwd;
    private Button btnRegister;
    private Button btnLogin;
    private Button btnFaceLogin;
    private ImageButton btnCancel;
    private CheckBox cbIsRemember;
    private Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MyApplication.activityList.add(this);

        initViews();
        setListener();
    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = getSharedPreferences("accountData", MODE_PRIVATE);
        String userName, password;
        boolean isChecked;
        userName = sharedPreferences.getString("userName", "");
        password = sharedPreferences.getString("password", "");
        isChecked = sharedPreferences.getBoolean("isRemembered", false);

        editTextUserName.setText(userName);
        editTextPwd.setText(password);
        cbIsRemember.setChecked(isChecked);
        super.onResume();
    }

    private void setListener() {
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnFaceLogin.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initViews() {
        bg_login = findViewById(R.id.bg_login);
        bg_login.setBackground(new BitmapDrawable(SplashActivity.bitmap));
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPwd = findViewById(R.id.editTextPwd);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnFaceLogin = findViewById(R.id.btnFaceLogin);
        btnCancel = findViewById(R.id.btnCancel);
        cbIsRemember = findViewById(R.id.cbIsRemember);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                doLogin(editTextUserName.getText().toString(), editTextPwd.getText().toString());
                break;

            case R.id.btnFaceLogin:
                Intent intent = new Intent(LoginActivity.this, RegisterAndRecognizeActivity.class);
                intent.putExtra("isLogin", true);
                startActivity(intent);
                break;

            case R.id.btnRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.btnCancel:
                MyApplication.exit();
                break;

            default:
                break;
        }
    }


    private boolean check(String userName, String password) {

        boolean isValid = true;

        if ("".equals(userName)) {
            Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if ("".equals(password)) {
            Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void doLogin(String userName, String password) {

        if (check(userName, password)) {

            List<User> users = LitePal.where("name = ? and password = ? ", userName, password).find(User.class);

            if (users.size() == 1) {

                if (cbIsRemember.isChecked()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("accountData", MODE_PRIVATE);
                    @SuppressLint("CommitPrefEdits")
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", userName);
                    editor.putString("password", password);
                    editor.putBoolean("isRemembered", cbIsRemember.isChecked());
                    editor.apply();
                }

                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                DataActivity.userName = userName;
                DataActivity.id = users.get(0).getId();
                Intent intent = new Intent(LoginActivity.this, DataActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(LoginActivity.this, "用户名或密码不正确！", Toast.LENGTH_SHORT).show();
            }

        }

    }


    IUiListener listener = new BaseUiListener() {
        @Override
        public void onComplete(Object o) {
            super.onComplete(o);
        }
    };

    public void loginQQ(View view) {

        mTencent = Tencent.createInstance("101877985", this.getApplicationContext(), "com.tencent.sample.fileprovider");

        mTencent.login(this, "all", listener);

    }

    private void doLoginQQ(String userName, String gender) {

        List<User> users = LitePal.where("name = ? ", userName).find(User.class);

        if (users.size() == 1) {

            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
            DataActivity.userName = userName;
            DataActivity.id = users.get(0).getId();
            Intent intent = new Intent(LoginActivity.this, DataActivity.class);
            startActivity(intent);

        } else {
            User user = new User(userName, "123", gender, "1998-8-27", "处女座", 6);
            user.save();
            Toast.makeText(LoginActivity.this, "首次登录使用默认星座！", Toast.LENGTH_SHORT).show();

            doLoginQQ(user.getName(), user.getGender());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            JSONObject response = (JSONObject) o;
            try {
                String openID = response.getString("openid");
                String accessToken = response.getString("access_token");
                String expires = response.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                UserInfo userInfo = new UserInfo(getApplicationContext(), qqToken);
                userInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        JSONObject response = (JSONObject) o;
                        try {
                            String nickName = response.getString("nickname");
                            String gender = response.getString("gender");
                            doLoginQQ(nickName, gender);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(o.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, uiError.toString(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "取消授权", Toast.LENGTH_SHORT).show();

        }
    }

}
