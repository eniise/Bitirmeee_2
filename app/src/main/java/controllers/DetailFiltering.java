package controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.enise.bitirme_2.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import models.trainer.TrainerCourse;
import utils.AsyncResponse;
import utils.components.MyAlertDialog;
import utils.extras.TransactionTypes;
import utils.extras.URLs;
import utils.server.ServerGET;

public class DetailFiltering extends AppCompatActivity implements AsyncResponse {
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
    private View view;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail_filtering);
        init();
    }
    void init(){

        btnSearchBack = findViewById(R.id.btnSearchBack);
        btnPlaceSearch = findViewById(R.id.btnPlaceSearch);
        btnSearchBack.setOnClickListener(v -> {
            onBackPressed();
        });
        swStudentSearch = findViewById(R.id.swStudentSearch);
        swOtherPlaceSearch = findViewById(R.id.swOtherPlaceSearch);
        swOnlineSearch = findViewById(R.id.swOnlineSearch);
        swHomeSearch = findViewById(R.id.swHomeSearch);
        txtSearchMax = findViewById(R.id.txtSearchMax);
        txtSearchMin = findViewById(R.id.txtSearchMin);
        txtSearchName = findViewById(R.id.txtSearchName);
        btnCourseNameSearch = findViewById(R.id.btnCourseNameSearch);
        btnCourseNameSearch.setOnClickListener(v->{
            btnCourseNameSearch.setEnabled(false);
            ServerGET serverGET = new ServerGET(TransactionTypes.doSearch);
            serverGET.delegate = this;
            if (txtSearchName.getText().length()>0) {
                serverGET.execute(URLs.SearchName(txtSearchName.getText().toString()));
            } else {
                new MyAlertDialog(this, "Not null", "Please enter course name", R.drawable.ic_message_info)
                        .ShowMessage()
                        .setNegativeButton(R.string.okay, ((dialog, which) -> {

                        })).show();
            }
        });
        btnCostSearch = findViewById(R.id.btnCostSearch);
        btnCostSearch.setOnClickListener(v -> {
            btnCostSearch.setEnabled(false);
            ServerGET serverGET = new ServerGET(TransactionTypes.doSearch);
            serverGET.delegate = this;
            if (txtSearchMax.getText().length() > 0 && txtSearchMin.getText().length() > 0) {
                serverGET.execute(URLs.SearchCost(Float.parseFloat(txtSearchMax.getText().toString()), Float.parseFloat(txtSearchMin.getText().toString())));
            } else {
                new MyAlertDialog(this, "Not null", "Please enter cost max and min", R.drawable.ic_message_info)
                        .ShowMessage()
                        .setNegativeButton(R.string.okay, ((dialog, which) -> {

                        })).show();
            }
        });
        btnPlaceSearch.setOnClickListener(v->{
            btnPlaceSearch.setEnabled(false);
            ServerGET serverGET = new ServerGET(TransactionTypes.doSearch);
            serverGET.delegate = this;
            if (DetectHastag().length()>0) {
                serverGET.execute(URLs.SearchPlace(DetectHastag()));
            } else {
                new MyAlertDialog(this, "Not null", "Please select place", R.drawable.ic_message_info)
                        .ShowMessage()
                        .setNegativeButton(R.string.okay, ((dialog, which) -> {

                        })).show();
            }
        });
    }
    private String DetectHastag(){
        List<String> _tmp = new ArrayList<>();
        if(swOnlineSearch.isChecked())
            _tmp.add("Online");
        if(swHomeSearch.isChecked())
            _tmp.add("Trainer Home");
        if(swOtherPlaceSearch.isChecked())
            _tmp.add("Other place");
        if(swStudentSearch.isChecked())
            _tmp.add("Student home");
        String s = _tmp.toString();//%20
        String z = s.replace("[","").replace("]","");
        String x = z.replace(" ","%20");
        return x;
    }
    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == String.class){
            TrainerCourse[] courses = null;
            try {
                courses = new Gson().fromJson(String.valueOf(result), TrainerCourse[].class);
                if(courses.length > 0) {
                    startActivity(new Intent(this, SearchResultPage.class).putExtra("result", String.valueOf(result)));
                    @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this,findViewById(android.R.id.content),"Found!", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this, findViewById(android.R.id.content), "Not Found!", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.show();
                }
            }catch (JsonSyntaxException ex){
                ex.printStackTrace();
                @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this,findViewById(android.R.id.content),"Not Found!", BaseTransientBottomBar.LENGTH_LONG);
                snackbar.show();
            }
        }
        btnPlaceSearch.setEnabled(true);
        btnCostSearch.setEnabled(true);
        btnCourseNameSearch.setEnabled(true);
    }
}
