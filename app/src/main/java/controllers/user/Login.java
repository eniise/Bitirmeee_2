package controllers.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.enise.bitirme_2.R;

import java.util.concurrent.ExecutionException;

import controllers.pages.Main;
import utils.AsyncResponse;
import utils.server.ServerGET;
import utils.components.MyAlertDialog;
import utils.user.StaticData;
import utils.extras.TransactionTypes;
import utils.extras.URLs;

public class Login extends AppCompatActivity implements AsyncResponse {
    TextView registerPage;
    TextView forgotPassword;
    EditText txtMail;
    EditText txtPassword;
    Button btnLogin;
    ProgressDialog doLoginDialog;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        registerPage = findViewById(R.id.registerPage);
        btnLogin = findViewById(R.id.btnLogin);
        txtMail = findViewById(R.id.txtMailAddress);
        txtPassword = findViewById(R.id.txtLoginPassword);
        registerPage.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Register.class);
            startActivity(intent);
        });
        forgotPassword = findViewById(R.id.loginForgotPassword);
        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPassword.class));
        });
        btnLogin.setOnClickListener(v -> {
            String mail = txtMail.getText().toString();
            String password = txtPassword.getText().toString();
            if(mail.length() != 0 && password.length() != 0 ) {
                doLoginDialog = new ProgressDialog(v.getContext());
                ServerGET task =  new ServerGET(doLoginDialog, TransactionTypes.doLogin, "Login process is running");
                task.delegate = this;
                try {
                    task.execute(URLs.LoginURL + "userMail="+mail+"&userPassword="+password).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                new MyAlertDialog(v.getContext(),"Fields can not be empty","Mail field or password field can not be empty.",R.drawable.ic_baseline_info_24)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> {
                            if(txtMail.getText().toString().length() == 0)
                                txtMail.requestFocus();
                            else if(txtPassword.getText().length() == 0)
                                txtPassword.requestFocus();
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
        if(output.getClass() == String.class){
            if(String.valueOf(output).equals("true")) {
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
                finish();
            }
            else if(String.valueOf(output).equals("false")){
                new MyAlertDialog(this,"Login error","Mail address or password is incorrect",R.drawable.ic_baseline_info_24)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> {
                            txtMail.requestFocus();
                        }))
                        .show();
            }
        }
    }
}
