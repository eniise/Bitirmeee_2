package controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.enise.bitirme_2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragments.ChatFragment;
import fragments.HomeFragment;
import fragments.ProfilFragment;
import utils.extras.TransactionTypes;

public class Main extends AppCompatActivity {
    Toolbar mToolBar;
    Context context;
    private FrameLayout mFrameLayout;
    private RelativeLayout.LayoutParams mParams;
    private BottomNavigationView bottomNav;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        mToolBar = findViewById(R.id.toolbar);
        SetApplicationToolbar();
        mFrameLayout = findViewById(R.id.fragment_container);
        mParams = (RelativeLayout.LayoutParams) findViewById(R.id.fragment_container).getLayoutParams();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
        context = this;
        SetupNavigationBar();
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
        switch (item.getItemId()){
            case R.id.DetailFiltering:
                Intent _intent = new Intent(context,DetailFiltering.class);
                startActivity(_intent);
                break;
        }
        return true;
    }
    private void SetApplicationToolbar(){
        setActionBar(mToolBar);
        mToolBar.inflateMenu(R.menu.ust_menu);
        getActionBar().setTitle("");
    }
    private void SetupNavigationBar(){
        bottomNav = findViewById(R.id.alt_menu);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        mToolBar.setVisibility(View.VISIBLE);
                        mFrameLayout.setLayoutParams(mParams);
                        item.setChecked(true);
                        break;
                    case R.id.chat:
                        selectedFragment = new ChatFragment();
                        mToolBar.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams _tmp = new RelativeLayout.LayoutParams(mParams);
                        _tmp.topMargin = 0;
                        mFrameLayout.setLayoutParams(_tmp);
                        item.setChecked(true);
                        break;
                    case R.id.profil:
                        selectedFragment = new ProfilFragment();
                        mToolBar.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams _tmp2 = new RelativeLayout.LayoutParams(mParams);
                        _tmp2.topMargin = 0;
                        mFrameLayout.setLayoutParams(_tmp2);
                        item.setChecked(true);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;
            };

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            int mTransactionType = bundle.getInt(TransactionTypes.USER_COME_BACK);
            if (mTransactionType == TransactionTypes.USER_DELETE_MESSAGE) {
                navListener.onNavigationItemSelected(bottomNav.getMenu().findItem(R.id.chat));
            }
        }else {
            navListener.onNavigationItemSelected(bottomNav.getMenu().findItem(R.id.home));
        }
    }
}
