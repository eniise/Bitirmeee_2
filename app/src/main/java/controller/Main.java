package controller;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.enise.bitirme_2.R;

public class Main extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
    }
}
