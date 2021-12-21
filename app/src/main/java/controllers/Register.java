package controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.enise.bitirme_2.R;
import com.google.gson.Gson;

import models.User;
import utils.AsyncResponse;
import utils.MyAlertDialog;
import utils.ServerPOST;
import utils.TransactionTypes;
import utils.URLs;

public class Register extends Activity implements AsyncResponse, CompoundButton.OnCheckedChangeListener {
    EditText txtRegisterMail;
    EditText txtRegisterName;
    EditText txtRegisterSurName;
    EditText txtRegisterPassword;
    RadioButton rdRegisterTrainer;
    RadioButton rdRegisterStudent;
    ProgressDialog doRegisterDialog;
    Button btnSignUp;
    RadioButton rdRegisterGenderFamale;
    RadioButton rdRegisterGenderMale;
    private int RoleId=0;
    private final String Famale = "https://www.pngkit.com/png/full/391-3919484_bayan-retmen-profil-kafa-kadn-female-user-icon.png";
    private final String Male = "https://www.pngkit.com/png/full/203-2035270_user-man-profile-male-face-gui-user-user.png";
    private String mUserSelectedGender = "";
    private boolean mUserGenderType = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regis_page);
        SetLayout();
        btnSignUp.setOnClickListener(v -> {
            if(DetectFreeSpace(v)){
                User user = new User(
                        0,
                        txtRegisterMail.getText().toString(),
                        txtRegisterName.getText().toString(),
                        txtRegisterSurName.getText().toString(),
                        txtRegisterPassword.getText().toString(),RoleId,
                        mUserSelectedGender,
                        mUserGenderType,
                        0);
                ServerPOST post = new ServerPOST(doRegisterDialog, TransactionTypes.doRegister,"Kayıt yapılıyor...");
                post.delegate = this;
                post.execute(URLs.RegisterURL,new Gson().toJson(user));
            }
        });
    }
    //register async finished ? show user alert dialog.
    @Override
    public void processFinish(Object output) {
        if(String.valueOf(output).equals("true")){
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
        rdRegisterGenderFamale = findViewById(R.id.rdRegisterGenderFamale);
        rdRegisterGenderMale = findViewById(R.id.rdRegisterGenderMale);
        rdRegisterGenderMale.setOnCheckedChangeListener(this);
        rdRegisterGenderFamale.setOnCheckedChangeListener(this);
        rdRegisterStudent.setOnCheckedChangeListener(this);
        rdRegisterTrainer.setOnCheckedChangeListener(this);
    }
    //detect user register fields. and show messages.
    private boolean DetectFreeSpace(View v){
        if (txtRegisterName.getText().toString().length() != 0){
            if(txtRegisterSurName.getText().toString().length() != 0){
                if(txtRegisterPassword.getText().toString().length() != 0){
                    if(txtRegisterMail.getText().toString().length() != 0){
                        if(rdRegisterGenderFamale.isChecked() || rdRegisterGenderMale.isChecked()) {
                            if (rdRegisterStudent.isChecked() || rdRegisterTrainer.isChecked()) {
                                doRegisterDialog = new ProgressDialog(v.getContext());
                                return true;
                            } else {
                                new MyAlertDialog(v.getContext()
                                        ,  getResources().getString(R.string.selectRole)
                                        , getResources().getString(R.string.pleaseSelectRole)
                                        , R.drawable.ic_baseline_info_24)
                                        .ShowMessage()
                                        .setPositiveButton(R.string.okay, ((dialog, which) -> {
                                        }))
                                        .show();
                                return false;
                            }
                        }else {
                            new MyAlertDialog(v.getContext()
                                    , getResources().getString(R.string.selectGender)
                                    , getResources().getString(R.string.pleaseSelectGender)
                                    , R.drawable.ic_baseline_info_24)
                                    .ShowMessage()
                                    .setPositiveButton(R.string.okay, ((dialog, which) -> {
                                    }))
                                    .show();
                            return false;
                        }
                    }else {
                        new MyAlertDialog(v.getContext()
                                ,getResources().getString(R.string.mailNotNull)
                                ,getResources().getString(R.string.pleaseEnterMail)
                                ,R.drawable.ic_baseline_info_24)
                                .ShowMessage()
                                .setPositiveButton(R.string.okay,((dialog, which) -> {
                                    txtRegisterMail.requestFocus();
                                }))
                                .show();
                        return false;
                    }
                }else {
                    new MyAlertDialog(v.getContext(),
                            getResources().getString(R.string.passwordNotNull),
                            getResources().getString(R.string.pleaseEnterPassowrd),
                            R.drawable.ic_baseline_info_24)
                            .ShowMessage()
                            .setPositiveButton(R.string.okay,((dialog, which) -> {
                                txtRegisterPassword.requestFocus();
                            }))
                            .show();
                    return false;
                }
            }else {
                new MyAlertDialog(v.getContext(),
                        getResources().getString(R.string.surNameNotNull),
                        getResources().getString(R.string.pleaseEnterSurName),
                        R.drawable.ic_baseline_info_24)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> {
                            txtRegisterSurName.requestFocus();
                        }))
                        .show();
                return false;
            }
        }else {
            new MyAlertDialog(v.getContext(),
                    getResources().getString(R.string.nameNotNull),
                    getResources().getString(R.string.pleaseEnterName),
                    R.drawable.ic_baseline_info_24)
                    .ShowMessage()
                    .setPositiveButton(R.string.okay,((dialog, which) -> {
                        txtRegisterName.requestFocus();
                    }))
                    .show();
            return false;
        }
    }
    //user select register option gender,user type end setting ui&code
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.rdRegisterGenderFamale:
                if(isChecked)
                    rdRegisterGenderMale.setChecked(false);
                mUserSelectedGender = Famale;
                mUserGenderType = false;
                break;
            case R.id.rdRegisterGenderMale:
                if(isChecked)
                    rdRegisterGenderFamale.setChecked(false);
                mUserSelectedGender = Male;
                mUserGenderType = true;
                break;
            case R.id.rdRegisterStudent:
                if(isChecked)
                    rdRegisterTrainer.setChecked(false);
                RoleId = 2;
                break;
            case R.id.rdRegisterTrainer:
                if(isChecked)
                    rdRegisterStudent.setChecked(false);
                RoleId = 1;
                break;
        }
    }
}
