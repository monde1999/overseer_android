package com.adventurers.overseer.signup.views;

import static com.adventurers.overseer.Constants.EC_EMAIL_REGISTERED;
import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adventurers.overseer.R;
import com.adventurers.overseer.helpers.LoadingDialog;
import com.adventurers.overseer.map.views.MapActivity;
import com.adventurers.overseer.signup.presenters.SignupPresenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements ISignUpView {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String password2;
    private EditText et_username;
    private EditText et_firstname;
    private EditText et_lastname;
    private EditText et_password;
    private EditText et_password2;
    private TextView tv_username_error;
    private TextView tv_firstname_error;
    private TextView tv_lastname_error;
    private TextView tv_password_error;
    private TextView tv_password2_error;
    private CheckBox cb_show_password;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        et_username = findViewById(R.id.signup_et_username);
        et_firstname = findViewById(R.id.signup_et_firstname);
        et_lastname = findViewById(R.id.signup_et_lasttname);
        et_password = findViewById(R.id.signup_et_password);
        et_password2 = findViewById(R.id.signup_et_password2);
        tv_username_error = findViewById(R.id.signup_tv_username_error);
        tv_firstname_error = findViewById(R.id.signup_tv_firstname_error);
        tv_lastname_error = findViewById(R.id.signup_tv_lastname_error);
        tv_password_error = findViewById(R.id.signup_tv_password_error);
        tv_password2_error = findViewById(R.id.signup_tv_password2_error);
        cb_show_password = findViewById(R.id.signup_cb_show_password);
        loadingDialog = new LoadingDialog(this);
        TextView tv_have_account = findViewById(R.id.signup_tv_have_account);
        Button btn_submit = findViewById(R.id.signup_btn_submit);
        setTextListeners();

        tv_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Temporary values
        et_username.setText("overseer@gmail.com");
        et_firstname.setText("Over");
        et_lastname.setText("Seer");
        et_password.setText("Overseer123");
        et_password2.setText("Overseer123");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                firstname = et_firstname.getText().toString();
                lastname = et_lastname.getText().toString();
                password = et_password.getText().toString();
                password2 = et_password2.getText().toString();
                if(isValid()){
                    SignupPresenter signupPresenter = new SignupPresenter(SignupActivity.this);
                    signupPresenter.handleSignup(username, firstname, lastname, password);
                }
            }
        });

        cb_show_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show/Hide password
                if(cb_show_password.isChecked()){
                    et_password.setTransformationMethod(null);
                    et_password2.setTransformationMethod(null);
                }
                else{
                    et_password.setTransformationMethod(new PasswordTransformationMethod());
                    et_password2.setTransformationMethod(new PasswordTransformationMethod());
                }
                et_password.setSelection(et_password.length());
                et_password2.setSelection(et_password2.length());
            }
        });
    }

    // region ISignupView...
    @Override
    public void renderSignupFailure(int errorCode, String errorMessage) {
        switch (errorCode){
            case EC_SERVER_FAILED:
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                break;
            case EC_EMAIL_REGISTERED:
                tv_username_error.setText(errorMessage);
                tv_username_error.setVisibility(View.VISIBLE);
                break;
        }
        loadingDialog.dismissDialog();
    }

    @Override
    public void renderSignupProgressing() {
        loadingDialog.showDialog();
    }

    @Override
    public void renderSignupSuccess() {
        Intent i = new Intent(this, MapActivity.class);
        finishAffinity();
        startActivity(i);
        finish();
    }

    // endregion

    // region Validation...
    private boolean isValid(){
        return isValidUsername() && isValidName() && isValidPassword();
    }

    private boolean isValidUsername(){
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

    private boolean isValidName(){
        return isValidFirstName() && isValidLastName();
    }

    private boolean isValidFirstName(){
        boolean valid = true;
        if(firstname.equals("")){
            valid = false;
            tv_firstname_error.setText("This field is required.");
            tv_firstname_error.setVisibility(View.VISIBLE);
        }
        else{
            if(!firstname.matches("[a-zA-Z ]+")){
                valid = false;
                tv_firstname_error.setText("Must only contain letters.");
                tv_firstname_error.setVisibility(View.VISIBLE);
            }
        }
        return valid;
    }

    private boolean isValidLastName(){
        boolean valid = true;
        if(lastname.equals("")){
            valid = false;
            tv_lastname_error.setText("This field is required.");
            tv_lastname_error.setVisibility(View.VISIBLE);
        }
        else{
            if(!lastname.matches("[a-zA-Z ]+")){
                valid = false;
                tv_lastname_error.setText("Must only contain letters.");
                tv_lastname_error.setVisibility(View.VISIBLE);
            }
        }
        return valid;
    }

    private boolean isValidPassword(){
        return isValidPassword1() && isValidPassword2();
    }

    private boolean isValidPassword1(){
        boolean valid = true;
        if(password.equals("")){
            valid = false;
            tv_password_error.setText("This field is required.");
            tv_password_error.setVisibility(View.VISIBLE);
        }
        else{
            if(password.length()<8){
                valid = false;
                tv_password_error.setText("Password must be 8-16 characters in length.");
                tv_password_error.setVisibility(View.VISIBLE);
            }
            else{
                boolean isDigit = false, isLetter = false, noSymbol = true;
                for(char c : password.toCharArray()){
                    if(Character.isDigit(c)) isDigit = true;
                    else if(Character.isLetter(c)) isLetter = true;
                    else {
                        noSymbol = false;
                        break;
                    }
                }
                valid = isDigit && isLetter && noSymbol;
                if(!valid){
                    if(!noSymbol)
                        tv_password_error.setText("Must have numbers and letters only.");
                    else
                        tv_password_error.setText("Must contain both numbers and letters.");
                    tv_password_error.setVisibility(View.VISIBLE);
                }
            }
        }
        return valid;
    }

    private boolean isValidPassword2(){
        boolean valid = true;
        if(password2.equals("")){
            valid = false;
            tv_password2_error.setText("This field is required.");
            tv_password2_error.setVisibility(View.VISIBLE);
        }
        else{
            if(!password2.equals(password)){
                valid = false;
                tv_password2_error.setText("Passwords do not match.");
                tv_password2_error.setVisibility(View.VISIBLE);
            }
        }
        return valid;
    }
    // endregion

    private void setTextListeners() {
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tv_username_error.getVisibility() == View.VISIBLE) {
                    tv_username_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tv_firstname_error.getVisibility() == View.VISIBLE) {
                    tv_firstname_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tv_lastname_error.getVisibility() == View.VISIBLE) {
                    tv_lastname_error.setVisibility(View.GONE);
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
                if (tv_password_error.getVisibility() == View.VISIBLE) {
                    tv_password_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tv_password2_error.getVisibility() == View.VISIBLE) {
                    tv_password2_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}