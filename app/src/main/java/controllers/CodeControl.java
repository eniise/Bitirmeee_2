package controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.enise.bitirme_2.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import models.user.ForgotPassword;
import utils.AsyncResponse;
import utils.components.MyAlertDialog;
import utils.extras.TransactionTypes;
import utils.extras.URLs;
import utils.server.ServerGET;
import utils.server.ServerPOST;
import utils.user.StaticData;

public class CodeControl extends AppCompatActivity implements AsyncResponse{
    private TextView txtCodeControl;
    private Button btnCodeControl;
    private TextView changePassword;
    private TextView changeReEnterPassword;
    private Button btnChangePassword;
    models.user.ForgotPassword forgotPassword = null;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_code_control);
        init();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String data = bundle.getString("data");
        forgotPassword = new Gson().fromJson(data, ForgotPassword.class);
    }
    void init(){
        txtCodeControl = findViewById(R.id.txtCodeControl);
        btnCodeControl = findViewById(R.id.btnCodeControl);
        btnCodeControl.setOnClickListener(v -> {
            if(txtCodeControl.getText().toString().equals(forgotPassword.getCode())){
                changePassword = findViewById(R.id.changePassword);
                changeReEnterPassword = findViewById(R.id.changeReEnterPassword);
                btnChangePassword = findViewById(R.id.btnChangePassword);
                changePassword.setVisibility(View.VISIBLE);
                changeReEnterPassword.setVisibility(View.VISIBLE);
                btnChangePassword.setVisibility(View.VISIBLE);
                btnChangePassword.setOnClickListener(v1 -> {
                    if(changePassword.getText().length() > 0){
                        if(changeReEnterPassword.getText().length() > 0){
                            if(changeReEnterPassword.getText().toString().toLowerCase().equals(changePassword.getText().toString().toLowerCase())){
                                forgotPassword.setPassword(changeReEnterPassword.getText().toString());
                                ServerPOST changePassword = new ServerPOST(TransactionTypes.doChangePassword);
                                changePassword.delegate = this;
                                changePassword.execute(URLs.ForgotPasswordChange(), new Gson().toJson(forgotPassword));
                            }else {
                                @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this,findViewById(android.R.id.content),"The two passwords entered are not the same", BaseTransientBottomBar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }
                });
            }else {
                @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this,findViewById(android.R.id.content),"Wrong code", BaseTransientBottomBar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == String.class){
            if(String.valueOf(result).equals("true")){
                new MyAlertDialog(this,"Successfully","Password change process completed.",R.drawable.ic_message_info)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> {
                            finish();
                        })).show();
            }else {
                new MyAlertDialog(this,"ERROR","ERROR !! Password change process not completed.",R.drawable.ic_message_info)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> {
                            //
                        })).show();
            }
        }
    }
}
