package controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.enise.bitirme_2.R;

public class LoginController extends Activity {
    TextView registerPage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        registerPage = findViewById(R.id.registerPage);
        registerPage.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RegisterController.class);
            startActivity(intent);
        });
    }
}
