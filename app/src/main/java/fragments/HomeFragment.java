package fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;

import java.util.ArrayList;

import adapters.TrainerCourseAdapter;
import models.trainer.TrainerCourse;
import utils.AsyncResponse;
import utils.components.MyAlertDialog;
import utils.server.ServerGET;
import utils.user.StaticData;
import utils.extras.TransactionTypes;
import utils.extras.URLs;

public class HomeFragment extends Fragment implements AsyncResponse,TextWatcher {
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private ProgressBar mHomeContentProgressBar;
    private EditText txtSearchCourse;
    public HomeFragment(EditText txtSearchCourse){
        this.txtSearchCourse = txtSearchCourse;
    }
    private View mToolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_content, container, false);
        view = rootView;
        progressDialog = new ProgressDialog(view.getContext());
        txtSearchCourse.addTextChangedListener(this);
        ServerGET serverGET = new ServerGET(TransactionTypes.doGetCourses);
        serverGET.delegate = this;
        serverGET.execute(URLs.GetCourses(StaticData.getUserData().getUserId()));
        return rootView;
    }

    @Override
    public void processFinish(Object output) {
        if(output.getClass() == ArrayList.class){
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
        }else {
            Toast.makeText(view.getContext(),"Course not find",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ServerGET searchCourse = new ServerGET(TransactionTypes.doUserSearchCourse);
        searchCourse.delegate = this;
        searchCourse.execute(URLs.SearchCourse(s.toString()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
