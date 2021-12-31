package controllers;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.enise.bitirme_2.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import utils.AsyncResponse;
import utils.MyAlertDialog;
import utils.ServerPOST;
import utils.StaticData;
import utils.TransactionTypes;
import utils.URLs;

public class ProfilSettings extends AppCompatActivity implements AsyncResponse {
    private EditText txtPassword1;
    private EditText txtPassword2;
    private Button btnChange;
    Context context;
    private RoundedImageView profileSettingsBack;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_page_settings);
        context = this;
        init();
    }
    void init(){
        txtPassword1 = findViewById(R.id.txtRePassword);
        txtPassword2 = findViewById(R.id.txtRePassword2);
        btnChange = findViewById(R.id.btnChange);
        profileSettingsBack = findViewById(R.id.profileSettingsBack);
        profileSettingsBack.setOnClickListener(v -> {
            onBackPressed();
        });
        btnChange.setOnClickListener(v -> {
            if(txtPassword1.getText().length() > 0){
                if(txtPassword2.getText().length() > 0){
                    if(txtPassword2.getText().toString().toLowerCase().equals(txtPassword1.getText().toString().toLowerCase())){
                        StaticData.getUserData().setPassword(txtPassword2.getText().toString());
                        ServerPOST changePassword = new ServerPOST(TransactionTypes.doUserChangePassword);
                        changePassword.delegate = this;
                        changePassword.execute(URLs.ChangePassword(), new Gson().toJson(StaticData.getUserData()));
                    }
                }
            }
        });
    }

    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == String.class){
            if(result.toString().equals("true")){
                new MyAlertDialog(context,"Password change successfully","Your password has been changed.",R.drawable.ic_message_info)
                .ShowMessage()
                .setPositiveButton(R.string.okay,((dialog, which) -> {
                    //..
                })).show();

            }else {
                new MyAlertDialog(context,"Error","Error when changing your password.",R.drawable.ic_message_info)
                        .ShowMessage()
                        .setNegativeButton(R.string.okay,((dialog, which) -> {
                            //..
                        })).show();
            }
        }
    }
}
