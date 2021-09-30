package com.adventurers.overseer.login.views;

import static com.adventurers.overseer.Constants.EC_ACCOUNT_NOT_EXIST;
import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EC_WRONG_PASSWORD;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adventurers.overseer.R;
import com.adventurers.overseer.helpers.LoadingDialog;
import com.adventurers.overseer.login.presenters.LoginPresenter;
import com.adventurers.overseer.map.views.MapActivity;
import com.adventurers.overseer.signup.views.SignupActivity;
import com.adventurers.overseer.user.handlers.UserInfoHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements ILoginView {
    private String username;
    private String password;
    private EditText et_username;
    private EditText et_password;
    private TextView tv_username_error;
    private TextView tv_password_error;
    private LoadingDialog loadingDialog;
    private final String preferencesKey = "com.adventurers.overseer";
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = findViewById(R.id.login_et_username);
        et_password = findViewById(R.id.login_et_password);
        tv_username_error = findViewById(R.id.login_tv_username_error);
        tv_password_error = findViewById(R.id.login_tv_password_error);
        loadingDialog = new LoadingDialog(this);
        TextView tv_create_account = findViewById(R.id.login_tv_create_account);
        Button btn_login = findViewById(R.id.login_btn_login);
        setTextListeners();

        sharedPreferences = getSharedPreferences(preferencesKey, Context.MODE_PRIVATE);
        // Temporary values
        et_username.setText("overseer@gmail.com");
        et_password.setText("Overseer123");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                if(isValid()){
                    LoginPresenter loginPresenter = new LoginPresenter(LoginActivity.this);
                    loginPresenter.handleLogin(username, password);
                }
            }
        });

        tv_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
            }
        });
    }

    // region ILoginView...
    @Override
    public void renderLoginFailure(int errorCode, String errorMessage) {
        switch (errorCode){
            case EC_SERVER_FAILED:
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                break;
            case EC_ACCOUNT_NOT_EXIST:
                tv_username_error.setText(errorMessage);
                tv_username_error.setVisibility(View.VISIBLE);
                break;
            case EC_WRONG_PASSWORD:
                tv_password_error.setText(errorMessage);
                tv_password_error.setVisibility(View.VISIBLE);
                break;
        }
        loadingDialog.dismissDialog();
    }

    @Override
    public void renderLoginProgressing() {
        loadingDialog.showDialog();
    }

    @Override
    public void renderLoginSuccess() {
        Toast.makeText(getApplicationContext(), UserInfoHandler.getCurrentUser(sharedPreferences).toString()+
                "\ntoken: "+ UserInfoHandler.getCurrentAccountToken(sharedPreferences),Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
    // endregion

    // region Validation...
    private boolean isValid(){
        return isValidUser() && isValidPassword();
    }

    private boolean isValidUser() {
        boolean valid = true;
        if(username.equals("")){
            valid = false;
            tv_username_error.setText("This field is required.");
            tv_username_error.setVisibility(View.VISIBLE);
        }
        else{
            Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
            Matcher matcher = pattern.matcher(username);
            if(!matcher.matches()){
                valid = false;
                tv_username_error.setText("Invalid email.");
                tv_username_error.setVisibility(View.VISIBLE);
            }
        }
        return valid;
    }

    private boolean isValidPassword() {
        boolean valid = true;
        if(password.equals("")){
            valid = false;
            tv_password_error.setText("This field is required.");
            tv_password_error.setVisibility(View.VISIBLE);
        }
        else{
            if(password.length()<6){
                valid = false;
                tv_password_error.setText("Please input a valid character combination.");
                tv_password_error.setVisibility(View.VISIBLE);
            }
        }
        return valid;
    }
    // endregion

    private void setTextListeners(){
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Hides the error message as the user types
                if(tv_username_error.getVisibility() == View.VISIBLE){
                    tv_username_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Hides the error message as the user types
                if(tv_password_error.getVisibility() == View.VISIBLE){
                    tv_password_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}