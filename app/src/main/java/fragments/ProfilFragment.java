package fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import adapters.TrainerCourseAdapter;
import models.TrainerCourse;
import utils.AsyncResponse;
import adapters.util.ImageDownloaderTask;
import utils.ServerGET;
import utils.StaticData;
import utils.TransactionTypes;
import utils.URLs;

public class ProfilFragment extends Fragment implements AsyncResponse,View.OnClickListener {
    private Button btnProfileCourseUpload;
    private Button btnProfileCourseEdit;
    private ImageView mUserProfileImage;
    private TextView mUserProfileName;
    private TextView mUserProfileLikeCount;
    private RecyclerView mUserProfileRecylerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View  rootView = inflater.inflate(R.layout.user_profile_page,container,false);
        view = rootView;
        //setup user profile layout
        SetupProfileButtons(rootView,StaticData.getUserData().getRoleID());
        //do get user data's from server json data here
        //now get user data's from json data
        new ImageDownloaderTask(mUserProfileImage)
                .execute(StaticData.getUserData().getUserProfileImageUrl());
        mUserProfileName.setText(StaticData.getUserData().getName());
        mUserProfileLikeCount.setText(String.valueOf(StaticData.getUserData().getUserLikesCount()));
        ServerGET getMyLikes = new ServerGET(new ProgressDialog(
                rootView.getContext()),
                TransactionTypes.doGetMyLikeCourses,
                " loading your likes courses");
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
        }
        mUserProfileImage = v.findViewById(R.id.imgProfile);
        mUserProfileLikeCount = v.findViewById(R.id.txtProfileLikeCount);
        mUserProfileRecylerView = v.findViewById(R.id.recylerUserProfile);
        mUserProfileName = v.findViewById(R.id.txtProfileNameSurname);
    }

    @Override
    public void onClick(View v) {
        //buttons click -> course upload end course edit
    }

    @Override
    public <T> void processFinish(T result) {
        ArrayList<TrainerCourse> _lst = (ArrayList<TrainerCourse>) result;
        mUserProfileRecylerView = view.findViewById(R.id.recylerUserProfile);
        mUserProfileRecylerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new TrainerCourseAdapter(_lst,"Profile");
        mUserProfileRecylerView.setLayoutManager(mLayoutManager);
        mUserProfileRecylerView.setAdapter(mAdapter);
    }
}
