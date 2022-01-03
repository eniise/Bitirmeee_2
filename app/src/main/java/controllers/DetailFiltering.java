package controllers;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.enise.bitirme_2.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class DetailFiltering extends AppCompatActivity {
    private RoundedImageView btnSearchBack;
    private Switch swStudentSearch;
    private Switch swOtherPlaceSearch;
    private Switch swOnlineSearch;
    private Switch swHomeSearch;
    private EditText txtSearchMax;
    private EditText txtSearchMin;
    private RoundedImageView btnCostSearch;
    private RoundedImageView btnCourseNameSearch;
    private RoundedImageView btnPlaceSearch;
    private EditText txtSearchName;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail_filtering);
        init();
    }
    void init(){
        btnSearchBack = findViewById(R.id.btnSearchBack);
        btnPlaceSearch.setOnClickListener(v -> {
            onBackPressed();
        });
        swStudentSearch = findViewById(R.id.swStudentSearch);
        swOtherPlaceSearch = findViewById(R.id.swOtherPlaceSearch);
        swOnlineSearch = findViewById(R.id.swOnlineSearch);
        swHomeSearch = findViewById(R.id.swHomeSearch);
        txtSearchMax = findViewById(R.id.txtSearchMax);
        txtSearchMin = findViewById(R.id.txtSearchMin);
        btnCostSearch = findViewById(R.id.btnCostSearch);
        btnCourseNameSearch = findViewById(R.id.btnCourseNameSearch);
        btnPlaceSearch = findViewById(R.id.btnPlaceSearch);
        txtSearchName = findViewById(R.id.txtSearchName);
    }
}
