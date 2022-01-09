package controllers.pages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.enise.bitirme_2.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import fragments.ChatFragment;
import fragments.HomeFragment;
import fragments.ProfilFragment;
import utils.components.MyAlertDialog;
import utils.extras.TransactionTypes;

public class Main extends AppCompatActivity {
    Toolbar mToolBar;
    Context context;
    private FrameLayout mFrameLayout;
    private RelativeLayout.LayoutParams mParams;
    private com.ismaeldivita.chipnavigation.ChipNavigationBar bottomNav;
    private EditText txtSearchCourse;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_notes_24));
        SetApplicationToolbar();
        txtSearchCourse = findViewById(R.id.txtSearchCourse);
        SetupNavigationBar();
        mFrameLayout = findViewById(R.id.fragment_container);
        mParams = (RelativeLayout.LayoutParams) findViewById(R.id.fragment_container).getLayoutParams();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment(txtSearchCourse)).commit();
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
        switch (item.getItemId()){
            case R.id.DetailFiltering:
                Intent _intent = new Intent(context,DetailFiltering.class);
                startActivity(_intent);
                break;
        }
        return true;
    }
    private void SetApplicationToolbar(){
        setSupportActionBar(mToolBar);
        mToolBar.inflateMenu(R.menu.ust_menu);
    }
    private void SetupNavigationBar(){
        bottomNav = findViewById(R.id.alt_menu);
        bottomNav.setOnItemSelectedListener(navListener);
    }
    @SuppressLint("NonConstantResourceId")
    private final ChipNavigationBar.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item) {
                    case R.id.home:
                        selectedFragment = new HomeFragment(txtSearchCourse);
                        mToolBar.setVisibility(View.VISIBLE);
                        mFrameLayout.setLayoutParams(mParams);
                        txtSearchCourse.setVisibility(View.VISIBLE);
                        break;
                    case R.id.chat:
                        selectedFragment = new ChatFragment();
                        mToolBar.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams _tmp = new RelativeLayout.LayoutParams(mParams);
                        _tmp.topMargin = 0;
                        mFrameLayout.setLayoutParams(_tmp);
                        txtSearchCourse.setVisibility(View.GONE);
                        break;
                    case R.id.profil:
                        selectedFragment = new ProfilFragment();
                        mToolBar.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams _tmp2 = new RelativeLayout.LayoutParams(mParams);
                        _tmp2.topMargin = 0;
                        mFrameLayout.setLayoutParams(_tmp2);
                        txtSearchCourse.setVisibility(View.GONE);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
            };

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            int mTransactionType = bundle.getInt(TransactionTypes.USER_COME_BACK);
            if (mTransactionType == TransactionTypes.USER_DELETE_MESSAGE) {
                bottomNav.setItemSelected(bottomNav.findViewById(R.id.chat).getId(),true);
            }
        }else {
            bottomNav.setItemSelected(bottomNav.findViewById(R.id.home).getId(),true);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                Intent _intent = new Intent(context,DetailFiltering.class);
                startActivity(_intent);
                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);

    }
}
