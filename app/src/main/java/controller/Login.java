package controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.enise.bitirme_2.R;

public class Login extends Activity {
    TextView registerPage;
    Button btnLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        registerPage = findViewById(R.id.registerPage);
        btnLogin = findViewById(R.id.btnLogin);
        registerPage.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Register.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Main.class);
            startActivity(intent);
        });
    }
}
