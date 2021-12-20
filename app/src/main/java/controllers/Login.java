package controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.enise.bitirme_2.R;

import java.util.concurrent.ExecutionException;

import utils.AsyncResponse;
import utils.ServerGET;
import utils.MyAlertDialog;
import utils.StaticData;
import utils.TransactionTypes;
import utils.URLs;

public class Login extends Activity implements AsyncResponse {
    TextView registerPage;
    EditText txtMail;
    EditText txtPassword;
    Button btnLogin;
    ProgressDialog doLoginDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        registerPage = findViewById(R.id.registerPage);
        btnLogin = findViewById(R.id.btnLogin);
        txtMail = findViewById(R.id.txtMailAddress);
        txtPassword = findViewById(R.id.txtPassword);
        registerPage.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Register.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(v -> {
            String mail = txtMail.getText().toString();
            String password = txtPassword.getText().toString();
            if(mail.length() != 0 && password.length() != 0 ) {
                doLoginDialog = new ProgressDialog(v.getContext());
                ServerGET task =  new ServerGET(null,doLoginDialog, TransactionTypes.doLogin, "Giriş yapılıyor");
                task.delegate = this;
                try {
                    task.execute(URLs.LoginURL + "userMail="+mail+"&userPassword="+password).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

            }else {
                new MyAlertDialog(v.getContext(),"Alanlar boş olamaz","Mail Adresi veya şifre alanı boş olamaz.",R.drawable.ic_baseline_info_24)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> {
                            if(txtMail.getText().toString().length() == 0)
                                txtMail.requestFocus();
                            else if(txtPassword.getText().length() == 0)
                                txtPassword.requestFocus();
                            else
                                Toast.makeText(v.getContext(), "Boş alan algılanmadı.", Toast.LENGTH_SHORT).show();
                        }))
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            String mail = intent.getStringExtra("mail");
            String password = intent.getStringExtra("password");
            txtMail.setText(mail);
            txtPassword.setText(password);
        }
    }

    @Override
    public void processFinish(Object output) {
        if(String.valueOf(output).equals("true")){
            System.out.println(StaticData.getUserData().getMail());
            Intent intent = new Intent(this,Main.class);
            startActivity(intent);
            finish();
        }
        if(String.valueOf(output).equals("false")){
            new MyAlertDialog(this,"Hatalı giriş","Mail adresi veya şifre hatalı",R.drawable.ic_baseline_info_24)
                    .ShowMessage()
                    .setPositiveButton(R.string.okay,((dialog, which) -> {
                        txtMail.requestFocus();
                    }))
                    .show();
        }
    }
}
