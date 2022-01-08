package controllers.course;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;

import java.util.ArrayList;

import adapters.TrainerCourseAdapter;
import models.trainer.TrainerCourse;
import utils.AsyncResponse;
import utils.extras.TransactionTypes;
import utils.extras.URLs;
import utils.server.ServerGET;
import utils.user.StaticData;

public class CourseEdit extends AppCompatActivity implements AsyncResponse {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mHomeContentProgressBar;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_course_home_page);
        ServerGET serverGET = new ServerGET(TransactionTypes.doTrainerGetMyUploads);
        serverGET.delegate = this;
        serverGET.execute(URLs.GetMyUploadCourses(StaticData.getUserData().getUserId()));
    }

    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == ArrayList.class){
            ArrayList<TrainerCourse> _lst = (ArrayList<TrainerCourse>) result;
            mRecyclerView = findViewById(R.id.editPageRecyler);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new TrainerCourseAdapter(_lst,"Edit");
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mHomeContentProgressBar = findViewById(R.id.editPageProgressbar);
            mHomeContentProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
