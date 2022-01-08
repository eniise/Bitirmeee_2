package fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import adapters.TrainerCourseAdapter;
import controllers.course.CourseEdit;
import controllers.course.CourseUpload;
import controllers.user.ProfilSettings;
import models.trainer.TrainerCourse;
import utils.AsyncResponse;
import adapters.util.ImageDownloaderTask;
import utils.server.ServerGET;
import utils.user.StaticData;
import utils.extras.TransactionTypes;
import utils.extras.URLs;

public class ProfilFragment extends Fragment implements AsyncResponse,View.OnClickListener {
    private RoundedImageView btnProfileCourseUpload;
    private RoundedImageView btnProfileCourseEdit;
    private RoundedImageView mUserProfileImage;
    private TextView mUserProfileName;
    private TextView mUserProfileLikeCount;
    private RecyclerView mUserProfileRecylerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private ProgressBar profileProgressBar;
    private RoundedImageView btnBack;
    private RoundedImageView btnProfileSettings;
    @SuppressLint("SetTextI18n")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View  rootView = inflater.inflate(R.layout.user_profile_page,container,false);
        view = rootView;
        profileProgressBar = rootView.findViewById(R.id.profileProgressBar);
        profileProgressBar.setVisibility(View.VISIBLE);
        //setup user profile layout
        SetupProfileButtons(rootView,StaticData.getUserData().getRoleID());
        //do get user data's from server json data here
        //now get user data's from json data
        new ImageDownloaderTask(mUserProfileImage)
                .execute(URLs.GetPhoto(StaticData.getUserData().getUserId()));
        mUserProfileName.setText(StaticData.getUserData().getName());
        mUserProfileLikeCount.setText("Your like score : "+StaticData.getUserData().getUserLikesCount());
        ServerGET getMyLikes = new ServerGET(TransactionTypes.doGetMyLikeCourses);
        getMyLikes.delegate = this;
        getMyLikes.execute(URLs.GetUserLikeCourses(StaticData.getUserData().getUserId()));
        return rootView;
    }
    private void SetupProfileButtons(View v,int loginUserRoleId){
        if(loginUserRoleId == 1) {
            btnProfileCourseEdit = v.findViewById(R.id.btnProfileCourseUpload);
            btnProfileCourseUpload = v.findViewById(R.id.btnProfileCourseEdit);
            if (btnProfileCourseUpload != null) {
                btnProfileCourseUpload.setVisibility(View.VISIBLE);
                btnProfileCourseUpload.setOnClickListener(this);
            }
            if (btnProfileCourseEdit != null) {
                btnProfileCourseEdit.setVisibility(View.VISIBLE);
                btnProfileCourseEdit.setOnClickListener(this);
            }
        }else{
            btnProfileCourseEdit = v.findViewById(R.id.btnProfileCourseUpload);
            btnProfileCourseUpload = v.findViewById(R.id.btnProfileCourseEdit);
            btnProfileCourseEdit.setVisibility(View.GONE);
            btnProfileCourseUpload.setVisibility(View.GONE);
        }
        mUserProfileImage = v.findViewById(R.id.imgProfile);
        mUserProfileLikeCount = v.findViewById(R.id.txtProfileLikeCount);
        mUserProfileRecylerView = v.findViewById(R.id.recylerUserProfile);
        mUserProfileName = v.findViewById(R.id.txtProfileNameSurname);
        btnProfileSettings = v.findViewById(R.id.profileSettings);
        btnProfileSettings.setOnClickListener(v1 -> {
            startActivity(new Intent(getContext(), ProfilSettings.class).putExtra("userSee",TransactionTypes.TRAINER_SEE_COURSE_UPLOAD));
        });
        btnBack = v.findViewById(R.id.profileBack);
        btnBack.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        //buttons click -> course upload end course edit
        if(v.getId() == R.id.btnProfileCourseUpload){
            startActivity(new Intent(getContext(),CourseUpload.class).putExtra("userSee",TransactionTypes.TRAINER_SEE_COURSE_UPLOAD));
        }
        if(v.getId() == R.id.btnProfileCourseEdit){
            startActivity(new Intent(getContext(), CourseEdit.class));
        }
    }

    @Override
    public <T> void processFinish(T result) {
        ArrayList<TrainerCourse> _lst = (ArrayList<TrainerCourse>) result;
        mUserProfileRecylerView = view.findViewById(R.id.recylerUserProfile);
        mUserProfileRecylerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new TrainerCourseAdapter(_lst,"Home");
        mUserProfileRecylerView.setLayoutManager(mLayoutManager);
        mUserProfileRecylerView.setAdapter(mAdapter);
        mUserProfileRecylerView.setVisibility(View.VISIBLE);
        profileProgressBar.setVisibility(View.GONE);
    }


}
