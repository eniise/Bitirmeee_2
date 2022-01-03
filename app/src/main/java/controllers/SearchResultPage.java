package controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import adapters.TrainerCourseAdapter;
import models.trainer.TrainerCourse;

public class SearchResultPage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_content);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TrainerCourse[] courses = new Gson().fromJson(bundle.getString("result"),TrainerCourse[].class);
        ArrayList<TrainerCourse> _lst = new ArrayList<>(Arrays.asList(courses));
        init(_lst);
    }
    void init(ArrayList<TrainerCourse> output){
        RecyclerView mRecyclerView = findViewById(R.id.searchContentRecyler);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new TrainerCourseAdapter(output, "Home");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        ProgressBar mSearchContentProgressBar = findViewById(R.id.searchProgressBar);
        mSearchContentProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
