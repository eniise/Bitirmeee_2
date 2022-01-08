package controllers.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.enise.bitirme_2.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import controllers.pages.CodeControl;
import utils.AsyncResponse;
import utils.extras.TransactionTypes;
import utils.extras.URLs;
import utils.server.ServerGET;

public class ForgotPassword extends AppCompatActivity implements AsyncResponse {
    EditText txtForgotMailAddress;
    Button btnSendCode;
    RoundedImageView forgotBack;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        init();
    }
    void init(){
        txtForgotMailAddress = findViewById(R.id.txtForgotMailAddress);
        btnSendCode = findViewById(R.id.btnSendCode);
        forgotBack = findViewById(R.id.forgotBack);
        forgotBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        btnSendCode.setOnClickListener(v -> {
            ServerGET controlGet = new ServerGET(TransactionTypes.doForgotPassword);
            controlGet.delegate = this;
            controlGet.execute(URLs.ForgotPassword(txtForgotMailAddress.getText().toString()));
        });
    }
    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == String.class){
            if(!result.equals("false")){
                models.user.ForgotPassword forgotPassword = new Gson().fromJson(String.valueOf(result), models.user.ForgotPassword.class);
                if(forgotPassword.getUserId() != 0) {
                    startActivity(new Intent(this, CodeControl.class)
                            .putExtra("data", String.valueOf(result))
                            .putExtra("mail",String.valueOf(txtForgotMailAddress.getText().toString())));
                }else {
                    @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this,findViewById(android.R.id.content),"E-mail address not found!", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.show();
                }
            }else {
                @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this,findViewById(android.R.id.content),"E-mail address not found!", BaseTransientBottomBar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
}
