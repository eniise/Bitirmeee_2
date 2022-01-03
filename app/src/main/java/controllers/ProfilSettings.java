package controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.enise.bitirme_2.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import utils.AsyncResponse;
import utils.components.MyAlertDialog;
import utils.server.ServerPOST;
import utils.user.StaticData;
import utils.extras.TransactionTypes;
import utils.extras.URLs;

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
        Button btnChangeImage = findViewById(R.id.btnChangeImage);
        btnChangeImage.setOnClickListener(v->{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                txtPassword1.setText(selectedImage.toString());
            }
    }
}
