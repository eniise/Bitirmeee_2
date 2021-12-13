package controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.enise.bitirme_2.R;

public class Main extends Activity {
    Toolbar mToolBar;
    Context context;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        mToolBar = findViewById(R.id.toolbar);
        SetApplicationToolbar(mToolBar);
        context = this;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ust_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(context,"asdjasjdasd",Toast.LENGTH_LONG).show();
        switch (item.getItemId()){
            case R.id.DetailFiltering:
                Toast.makeText(context,"Detaylı arama sayfası detail",Toast.LENGTH_LONG).show();
                break;
            case R.id.MapFiltering:
                Toast.makeText(context,"Detaylı arama sayfası map",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
    private void SetApplicationToolbar(Toolbar toolbar){
        setActionBar(mToolBar);
        mToolBar.inflateMenu(R.menu.ust_menu);
        getActionBar().setTitle("");
    }
}
