package controllers.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import adapters.TrainerCourseAdapter;
import adapters.util.ImageDownloaderTask;
import models.trainer.TrainerCourse;
import utils.AsyncResponse;
import utils.server.ServerGET;
import utils.extras.TransactionTypes;
import utils.extras.URLs;

public class UserProfil extends AppCompatActivity implements AsyncResponse {
    private RoundedImageView btnProfileCourseUpload;
    private RoundedImageView btnProfileCourseEdit;
    private RoundedImageView mUserProfileImage;
    private RoundedImageView btnBack;
    private TextView mUserProfileName;
    private TextView mUserProfileLikeCount;
    private RecyclerView mUserProfileRecylerView;
    private TrainerCourseAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar profileProgressBar;
    private Context context;
    private RoundedImageView btnProfileSettings;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_page);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        profileProgressBar = findViewById(R.id.profileProgressBar);
        profileProgressBar.setVisibility(View.VISIBLE);
        btnProfileCourseEdit = findViewById(R.id.btnProfileCourseUpload);
        btnProfileCourseUpload = findViewById(R.id.btnProfileCourseEdit);
        btnProfileSettings = findViewById(R.id.profileSettings);
        btnProfileCourseEdit.setVisibility(View.GONE);
        btnProfileCourseUpload.setVisibility(View.GONE);
        btnProfileSettings.setVisibility(View.GONE);
        int userId = bundle.getInt("userId");
        context = this;
        init();
        ServerGET getMyLikes = new ServerGET(TransactionTypes.doUserGetProfile);
        getMyLikes.delegate = this;
        getMyLikes.execute(URLs.GetUserProfil(userId));
    }
    void init(){
        mUserProfileImage = findViewById(R.id.imgProfile);
        mUserProfileLikeCount = findViewById(R.id.txtProfileLikeCount);
        mUserProfileRecylerView = findViewById(R.id.recylerUserProfile);
        mUserProfileName = findViewById(R.id.txtProfileNameSurname);
        btnBack = findViewById(R.id.profileBack);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
    @SuppressLint("SetTextI18n")
    @Override
    public <T> void processFinish(T result) {
        ArrayList<TrainerCourse> _lst = (ArrayList<TrainerCourse>) result;
        if(_lst.size() > 1) {
            mUserProfileRecylerView = findViewById(R.id.recylerUserProfile);
            mUserProfileRecylerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(context);
            mAdapter = new TrainerCourseAdapter(_lst, "Home");
            mUserProfileRecylerView.setLayoutManager(mLayoutManager);
            mUserProfileRecylerView.setAdapter(mAdapter);
            mUserProfileRecylerView.setVisibility(View.VISIBLE);
            profileProgressBar.setVisibility(View.GONE);
            mUserProfileName.setText(_lst.get(0).getmUserName());
            mUserProfileLikeCount.setText("User like score : "+_lst.get(0).getLikeCount());
            new ImageDownloaderTask(mUserProfileImage)
                    .execute(_lst.get(0).getmProfileImage());
        }else {
            mUserProfileName.setText(_lst.get(0).getmUserName());
            mUserProfileLikeCount.setText("User like score : "+_lst.get(0).getLikeCount());
            new ImageDownloaderTask(mUserProfileImage)
                    .execute(_lst.get(0).getmProfileImage());
            profileProgressBar.setVisibility(View.GONE);
        }
    }
}
