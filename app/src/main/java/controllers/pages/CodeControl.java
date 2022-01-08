package controllers.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.enise.bitirme_2.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import models.user.ForgotPassword;
import utils.AsyncResponse;
import utils.components.MyAlertDialog;
import utils.extras.TransactionTypes;
import utils.extras.URLs;
import utils.server.ServerGET;
import utils.server.ServerPOST;

public class CodeControl extends AppCompatActivity implements AsyncResponse{
    private static final int millisFuture = 180000;
    private static final int countDownInterval = 1000;
    private TextView txtCodeControl;
    private Button btnCodeControl;
    private TextView changePassword;
    private TextView changeReEnterPassword;
    private Button btnChangePassword;
    private  models.user.ForgotPassword forgotPassword = null;
    private ConstraintLayout changePasswordLayout;
    private TextView txtTimer;
    CountDownTimer countDownTimer;
    private String Mail;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_code_control);
        init();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String data = bundle.getString("data");
        Mail = bundle.getString("mail");
        forgotPassword = new Gson().fromJson(data, ForgotPassword.class);
        countDownTimer = new CountDownTimer(millisFuture, countDownInterval) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                long Minutes = millisUntilFinished / (60 * 1000) % 60;
                long Seconds = millisUntilFinished / 1000 % 60;
                txtTimer.setText((Minutes < 10 ? 0 + "" + Minutes : Minutes) + ":" + (Seconds < 10 ? 0 + "" + Seconds + Seconds : Seconds));
                btnCodeControl.setText("Verify Code");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                btnCodeControl.setText("Send new code");
            }
        }.start();
    }
    void init(){
        RoundedImageView closeControlPage = findViewById(R.id.closeControlPage);
        closeControlPage.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        txtTimer = findViewById(R.id.txtTimer);
        txtCodeControl = findViewById(R.id.txtCodeControl);
        btnCodeControl = findViewById(R.id.btnCodeControl);
        btnCodeControl.setOnClickListener(v -> {
            if(!btnCodeControl.getText().toString().equals("Send new code")) {
                if (txtCodeControl.getText().toString().equals(forgotPassword.getCode())) {
                    countDownTimer.cancel();
                    changePassword = findViewById(R.id.changePassword);
                    changeReEnterPassword = findViewById(R.id.changeReEnterPassword);
                    btnChangePassword = findViewById(R.id.btnChangePassword);
                    changePasswordLayout = findViewById(R.id.changePasswordLayout);
                    changePasswordLayout.setVisibility(View.VISIBLE);
                    btnChangePassword.setOnClickListener(v1 -> {
                        if (changePassword.getText().length() > 0) {
                            if (changeReEnterPassword.getText().length() > 0) {
                                if (changeReEnterPassword.getText().toString().toLowerCase().equals(changePassword.getText().toString().toLowerCase())) {
                                    forgotPassword.setPassword(changeReEnterPassword.getText().toString());
                                    ServerPOST changePassword = new ServerPOST(TransactionTypes.doChangePassword);
                                    changePassword.delegate = this;
                                    changePassword.execute(URLs.ForgotPasswordChange(), new Gson().toJson(forgotPassword));
                                } else {
                                    @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this, findViewById(android.R.id.content), "The two passwords entered are not the same", BaseTransientBottomBar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        }
                    });
                } else {
                    @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this, findViewById(android.R.id.content), "Wrong code", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.show();
                }
            }else {
                ServerGET controlGet = new ServerGET(TransactionTypes.doReSendMailCode);
                controlGet.delegate = this;
                controlGet.execute(URLs.ForgotPassword(Mail));
            }
        });
    }

    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == String.class){
            if(String.valueOf(result).equals("true")){
                new MyAlertDialog(this,"Successfully","Password change process completed.",R.drawable.ic_message_info)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> finish())).show();
            }
            else {
                new MyAlertDialog(this,"ERROR","ERROR !! Password change process not completed.",R.drawable.ic_message_info)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> {
                            //
                        })).show();
            }
        }else if(result.getClass() == ForgotPassword.class){

            new MyAlertDialog(this,"Successfully","Re send completed.",R.drawable.ic_message_info)
                    .ShowMessage()
                    .setPositiveButton(R.string.okay,((dialog, which) -> this.forgotPassword = (ForgotPassword) result)).show();
            countDownTimer = new CountDownTimer(millisFuture, countDownInterval) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    long Minutes = millisUntilFinished / (60 * 1000) % 60;
                    long Seconds = millisUntilFinished / 1000 % 60;
                    txtTimer.setText((Minutes < 10 ? 0 + "" + Minutes : Minutes) + ":" + (Seconds < 10 ? 0 + "" + Seconds : Seconds));
                    btnCodeControl.setText("Verify Code");
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onFinish() {
                    btnCodeControl.setText("Send new code");
                }
            }.start();
        }
    }
}
