package controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.enise.bitirme_2.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.trainer.TrainerCourse;
import utils.AsyncResponse;
import utils.components.MultiSpinner;
import utils.components.MyAlertDialog;
import utils.extras.TransactionTypes;
import utils.extras.URLs;
import utils.server.ServerPOST;
import utils.user.StaticData;

public class CourseUpload extends AppCompatActivity implements MultiSpinner.MultiSpinnerListener, AsyncResponse {
    private  MultiSpinner spinnerDistrict;
    private  MultiSpinner spinnerDays;
    private MultiSpinner spinnerIntervals;
    private  EditText txtCourseUploadName;
    private Switch swOnline;
    private Switch swHome;
    private Switch swStudent;
    private Switch swOther;
    private EditText txtCourseUploadCost;
    private EditText txtCourseUploadDetail;
    private List<String> mDistrict;
    private List<String> mDays;
    private List<String> mIntervals;
    private static final  List<String> _selectedDistrict = new ArrayList<>();
    private static final  List<String> _selectedDays= new ArrayList<>();
    private static final List<String> _selectedIntervals= new ArrayList<>();
    private String userSee;
    private Bundle pageBundle;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_upload);
        Intent pageIntent = getIntent();
        pageBundle = pageIntent.getExtras();
        mDistrict = Arrays.asList(getResources().getStringArray(R.array.district));
        mDays = Arrays.asList(getResources().getStringArray(R.array.weekDays));
        mIntervals = Arrays.asList(getResources().getStringArray(R.array.timeInterval));
        init();
    }
    @SuppressLint("SetTextI18n")
    void init(){
        userSee = pageBundle.getString("userSee");
        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        spinnerDays = findViewById(R.id.spinnerDays);
        spinnerIntervals = findViewById(R.id.spinnerIntervals);
        txtCourseUploadName = findViewById(R.id.txtCourseUploadName);
        swOnline = findViewById(R.id.swOnline);
        swHome = findViewById(R.id.swHome);
        swStudent = findViewById(R.id.swStudent);
        swOther = findViewById(R.id.swOther);
        txtCourseUploadCost = findViewById(R.id.txtCourseUploadCost);
        txtCourseUploadDetail = findViewById(R.id.txtCourseUploadDetail);
        Button btnCourseUpload = findViewById(R.id.btnCourseUpload);
        spinnerDistrict.setItems(mDistrict,getString(R.string.app_name),this);
        spinnerDays.setItems(mDays,getString(R.string.app_name),this);
        spinnerIntervals.setItems(mIntervals,getString(R.string.app_name),this);
        TrainerCourse course1 = new Gson().fromJson(pageBundle.getString("course"), TrainerCourse.class);
        if(course1 != null){
            SetDays(course1.getmDays());
            SetIntervals(course1.getmTimeIntervals());
            SetDistrict(course1.getmDistrict());
            SetHastag(course1.getmTypes());
            txtCourseUploadDetail.setText(course1.getmDetail());
            txtCourseUploadCost.setText(String.valueOf(course1.getmCost()));
            txtCourseUploadName.setText(course1.getmName());
            btnCourseUpload.setText("Edit Complete");
        }
        btnCourseUpload.setOnClickListener(v->{
            if((_selectedDistrict.size() == 0) || (_selectedIntervals.size() == 0) || (_selectedDays.size() == 0))
                new MyAlertDialog(this,"Not null","Please enter; days,intervals and district.",R.drawable.ic_message_info)
                        .ShowMessage()
                        .setNegativeButton(R.string.okay,((dialog, which) -> {
                        })).show();
            else {
                if(txtCourseUploadName.getText().toString().length() == 0){
                    new MyAlertDialog(this,"Not null","Please enter; course name",R.drawable.ic_message_info)
                            .ShowMessage()
                            .setNegativeButton(R.string.okay,((dialog, which) -> {
                            })).show();
                }else {
                    if(txtCourseUploadCost.getText().length() == 0){
                        new MyAlertDialog(this,"Not null","Please enter; cost",R.drawable.ic_message_info)
                                .ShowMessage()
                                .setNegativeButton(R.string.okay,((dialog, which) -> {
                                })).show();
                    }else {
                        if(txtCourseUploadDetail.getText().length() == 0){
                            new MyAlertDialog(this,"Not null","Please enter; detail",R.drawable.ic_message_info)
                                    .ShowMessage()
                                    .setNegativeButton(R.string.okay,((dialog, which) -> {
                                    })).show();
                        }else {
                            String _district = _selectedDistrict.toString().replace("[", "").replace("]", "").trim();
                            String _days = _selectedDays.toString().replace("[", "").replace("]", "").trim();
                            String _intervals = _selectedIntervals.toString().replace("[", "").replace("]", "").trim();
                            TrainerCourse course = new TrainerCourse(
                                    userSee.equals(TransactionTypes.TRAINER_SEE_COURSE_EDIT) ? course1.getmId():0,
                                    StaticData.getUserData().getUserId(),
                                    txtCourseUploadName.getText().toString(),
                                    DetectHastag(),
                                    _intervals,
                                    _days,
                                    _district,
                                    Double.parseDouble(txtCourseUploadCost.getText().toString()),
                                    txtCourseUploadDetail.getText().toString(),
                                    0,
                                    "",
                                    "",
                                    "",
                                    0
                                    );

                            if(userSee.equals(TransactionTypes.TRAINER_SEE_COURSE_UPLOAD)) {
                                ServerPOST postCourse = new ServerPOST(TransactionTypes.doTrainerCourseUpload);
                                postCourse.delegate = this;
                                postCourse.execute(URLs.UploadCourse(), new Gson().toJson(course));
                            }
                            if(userSee.equals(TransactionTypes.TRAINER_SEE_COURSE_EDIT)){
                                ServerPOST postCourse = new ServerPOST(TransactionTypes.doTrainerUpdateMyCourse);
                                postCourse.delegate = this;
                                postCourse.execute(URLs.UpdateMyCourse(), new Gson().toJson(course));
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onItemsSelected(boolean[] selected,Object c) {
        MultiSpinner _temp = (MultiSpinner) c;
        if(_temp.getId() == R.id.spinnerDistrict){
            if(_selectedDistrict.size() > 0){_selectedDistrict.clear();}
            for (int i=0; i<selected.length;i++) {
                if(selected[i]){
                    _selectedDistrict.add(mDistrict.get(i));
                }
            }
        }
        if(_temp.getId() == R.id.spinnerDays){
            if(_selectedDays.size() > 0){_selectedDays.clear();}
            for (int i=0; i<selected.length;i++) {
                if(selected[i]){
                    _selectedDays.add(mDays.get(i));
                }
            }
        }
        if(_temp.getId() == R.id.spinnerIntervals){
            if(_selectedIntervals.size() > 0){_selectedIntervals.clear();}
            for (int i=0; i<selected.length;i++) {
                if(selected[i]){
                    _selectedIntervals.add(mIntervals.get(i));
                }
            }
        }
    }
    private String DetectHastag(){
        List<String> _tmp = new ArrayList<>();
        if(swOnline.isChecked())
            _tmp.add("Online");
        if(swHome.isChecked())
            _tmp.add("Trainer Home");
        if(swOther.isChecked())
            _tmp.add("Other place");
        if(swStudent.isChecked())
            _tmp.add("Student home");
        return _tmp.toString().replace("[","").replace("]","").trim();
    }
    private void SetDays(String days){
        String[] _days = days.split(",");
        for (String day : _days) {
            for (int j = 0; j < mDays.size(); j++) {
                String _tmp = day.replace(" ","");
                if (_tmp.equals(mDays.get(j))) {
                    spinnerDays.onClick(null, mDays.indexOf(_tmp), true);
                    _selectedDays.add(_tmp);
                }
            }
        }
    }
    private void SetIntervals(String intervals){
        String[] _intervals = intervals.split(",");
        for (String interval : _intervals) {
            for (int j = 0; j < mIntervals.size(); j++) {
                String _tmp = interval.replace(" ","");
                if (_tmp.equals(mIntervals.get(j))) {
                    spinnerIntervals.onClick(null, mIntervals.indexOf(_tmp), true);
                    _selectedIntervals.add(_tmp);
                }
            }
        }
    }
    private void SetDistrict(String districts){
        String[] _districts = districts.split(",");
        for (String district : _districts) {
            for (int j = 0; j < mDistrict.size(); j++) {
                String _tmp = district.replace(" ","");
                if (_tmp.equals(mDistrict.get(j))) {
                    spinnerDistrict.onClick(null, mDistrict.indexOf(_tmp), true);
                    _selectedDistrict.add(_tmp);
                }
            }
        }
    }
    private void SetHastag(String hastag){
        String[] _hastag = hastag.split(",");
        for(String has : _hastag){
            String _tmp = has.replace(" ","");
            System.out.println(_tmp);
            if(_tmp.equals("Online"))
                swOnline.setChecked(true);
            if(_tmp.equals("TrainerHome"))
                swHome.setChecked(true);
            if(_tmp.equals("Otherplace"))
                swOther.setChecked(true);
            if(_tmp.equals("Studenthome"))
                swStudent.setChecked(true);
        }
    }
    @Override
    public <T> void processFinish(T result) {
        if(result.toString().equals("true") && userSee.equals(TransactionTypes.TRAINER_SEE_COURSE_UPLOAD)){
            new MyAlertDialog(this,"Successfully","Course has been uploaded.",R.drawable.ic_message_info)
                    .ShowMessage()
                    .setPositiveButton(R.string.okay,((dialog, which) -> {
                        finish();
                    }))
                    .show();
        }else if(result.toString().equals("false") && userSee.equals(TransactionTypes.TRAINER_SEE_COURSE_UPLOAD)) {
            new MyAlertDialog(this,"Error","Course upload process error",R.drawable.ic_message_info)
                    .ShowMessage()
                    .setNegativeButton(R.string.okay,((dialog, which) -> {
                        //
                    }))
                    .show();
        }else if(result.toString().equals("true") && userSee.equals(TransactionTypes.TRAINER_SEE_COURSE_EDIT)){
            new MyAlertDialog(this,"Successfully","Course has been updated.",R.drawable.ic_message_info)
                    .ShowMessage()
                    .setPositiveButton(R.string.okay,((dialog, which) -> {
                        finish();
                    }))
                    .show();
        }else if(result.toString().equals("true") && userSee.equals(TransactionTypes.TRAINER_SEE_COURSE_EDIT)){
            new MyAlertDialog(this,"Error","Course edit process error",R.drawable.ic_message_info)
                    .ShowMessage()
                    .setNegativeButton(R.string.okay,((dialog, which) -> {
                        //
                    }))
                    .show();
        }
    }
}
