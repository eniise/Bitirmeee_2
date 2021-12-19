package controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.enise.bitirme_2.R;
import com.google.gson.Gson;

import models.User;
import utils.AsyncResponse;
import utils.MyAlertDialog;
import utils.ServerPOST;
import utils.TransactionTypes;
import utils.URLs;

public class Register extends Activity implements AsyncResponse {
    EditText txtRegisterMail;
    EditText txtRegisterName;
    EditText txtRegisterSurName;
    EditText txtRegisterPassword;
    RadioButton rdRegisterTrainer;
    RadioButton rdRegisterStudent;
    ProgressDialog doRegisterDialog;
    Button btnSignUp;
    private int RoleId=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regis_page);
        SetLayout();
        rdRegisterTrainer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    rdRegisterStudent.setChecked(false);
                RoleId = 1;
            }
        });
        rdRegisterStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    rdRegisterTrainer.setChecked(false);
                RoleId = 2;
            }
        });
        btnSignUp.setOnClickListener(v -> {
            if(DetectFreeSpace(v)){
                User user = new User(0,txtRegisterMail.getText().toString(),txtRegisterName.getText().toString(),txtRegisterSurName.getText().toString(),txtRegisterPassword.getText().toString(),RoleId);
                ServerPOST post = new ServerPOST(doRegisterDialog, TransactionTypes.doRegister,"Kayıt yapılıyor...");
                post.delegate = this;
                post.execute(URLs.RegisterURL,new Gson().toJson(user));
            }
        });
    }

    @Override
    public void processFinish(String output) {
        if(output.equals("true")){
            new MyAlertDialog(this,"Kayıt tamamlandı","Kayıt işleminiz başarılı, anasayfaya yönlendiriyorum.",R.drawable.ic_baseline_info_24)
                    .ShowMessage()
                    .setNegativeButton(R.string.okay,((dialog, which) -> {
                        Intent intent = new Intent(this,Login.class)
                                .putExtra("mail",txtRegisterMail.getText().toString())
                                .putExtra("password",txtRegisterPassword.getText().toString());
                        startActivity(intent);
                    }))
                    .show();
        }
    }
    private  void SetLayout(){
        txtRegisterMail = findViewById(R.id.txtRegisterMail);
        txtRegisterName = findViewById(R.id.txtRegisterName);
        txtRegisterSurName = findViewById(R.id.txtRegisterSurName);
        txtRegisterPassword = findViewById(R.id.txtRegisterPassword);
        rdRegisterStudent = findViewById(R.id.rdRegisterStudent);
        rdRegisterTrainer = findViewById(R.id.rdRegisterTrainer);
        btnSignUp = findViewById(R.id.btnSingUp);
    }
    private boolean DetectFreeSpace(View v){
        if (txtRegisterName.getText().toString().length() != 0){
            if(txtRegisterSurName.getText().toString().length() != 0){
                if(txtRegisterPassword.getText().toString().length() != 0){
                    if(txtRegisterMail.getText().toString().length() != 0){
                        if(rdRegisterStudent.isChecked() || rdRegisterTrainer.isChecked()){
                            doRegisterDialog = new ProgressDialog(v.getContext());
                            return true;
                        }else {
                            new MyAlertDialog(v.getContext(),"Rolünüz nedir?","Lütfen bir rol seçiniz.",R.drawable.ic_baseline_info_24)
                                    .ShowMessage()
                                    .setPositiveButton(R.string.okay,((dialog, which) -> {
                                    }))
                                    .show();
                            return false;
                        }
                    }else {
                        new MyAlertDialog(v.getContext(),"Mail boş olamaz","Lütfen mail adresinizi girin.",R.drawable.ic_baseline_info_24)
                                .ShowMessage()
                                .setPositiveButton(R.string.okay,((dialog, which) -> {
                                    txtRegisterMail.requestFocus();
                                }))
                                .show();
                        return false;
                    }
                }else {
                    new MyAlertDialog(v.getContext(),"Şifre boş olamaz","Lütfen şifre bilginizi girin",R.drawable.ic_baseline_info_24)
                            .ShowMessage()
                            .setPositiveButton(R.string.okay,((dialog, which) -> {
                                txtRegisterPassword.requestFocus();
                            }))
                            .show();
                    return false;
                }
            }else {
                new MyAlertDialog(v.getContext(),"Soyadınız boş olamaz","Lütfen soyad bilgisini giriniz",R.drawable.ic_baseline_info_24)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> {
                            txtRegisterSurName.requestFocus();
                        }))
                        .show();
                return false;
            }
        }else {
            new MyAlertDialog(v.getContext(),"Ad alanı boş olamaz","Lütfen ad alanı bilginizi girin.",R.drawable.ic_baseline_info_24)
                    .ShowMessage()
                    .setPositiveButton(R.string.okay,((dialog, which) -> {
                        txtRegisterName.requestFocus();
                    }))
                    .show();
            return false;
        }
    }
}
