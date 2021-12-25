package fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;

import java.util.ArrayList;

import adapters.TrainerCourseAdapter;
import models.TrainerCourse;
import utils.AsyncResponse;
import utils.ServerGET;
import utils.StaticData;
import utils.TransactionTypes;
import utils.URLs;

public class FragmentHome extends Fragment implements AsyncResponse {
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private ProgressBar mHomeContentProgressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_content, container, false);
        view = rootView;
        progressDialog = new ProgressDialog(view.getContext());
        ServerGET serverGET = new ServerGET(progressDialog, TransactionTypes.doGetCourses,"Gönderiler yükleniyor..");
        serverGET.delegate = this;
        serverGET.execute(URLs.GetCourses(StaticData.getUserData().getUserId()));
        return rootView;
    }

    @Override
    public void processFinish(Object output) {
        ArrayList<TrainerCourse> _lst = (ArrayList<TrainerCourse>) output;
        mRecyclerView = view.findViewById(R.id.homeContentRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new TrainerCourseAdapter(_lst,"Home");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mHomeContentProgressBar = view.findViewById(R.id.homeContentProgressBar);
        mHomeContentProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
