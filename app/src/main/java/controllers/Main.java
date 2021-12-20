package controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.enise.bitirme_2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragments.FragmentHome;

public class Main extends AppCompatActivity {
    Toolbar mToolBar;
    Context context;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        mToolBar = findViewById(R.id.toolbar);
        SetApplicationToolbar(mToolBar);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragmentHome()).commit();
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
                Intent intent = new Intent(context,Maps.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    private void SetApplicationToolbar(Toolbar toolbar){
        setActionBar(mToolBar);
        mToolBar.inflateMenu(R.menu.ust_menu);
        getActionBar().setTitle("");
    }
    private void SetupNavigationBar(){
        BottomNavigationView bottomNav = findViewById(R.id.alt_menu);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new FragmentHome();
                        break;
                    case R.id.chat:
                        //selectedFragment = new sepet();
                        break;
                    case R.id.profil:
                        //selectedFragment = new profil();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;
            };
}
