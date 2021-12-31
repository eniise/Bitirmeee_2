package controllers;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.enise.bitirme_2.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import utils.MultiSpinner;

public class CourseUpload extends AppCompatActivity implements MultiSpinner.MultiSpinnerListener {
    MultiSpinner spinnerDistrict;
    MultiSpinner spinnerDays;
    MultiSpinner spinnerIntervals;
    EditText txtCourseUploadName;
    Switch swOnline;
    Switch swHome;
    Switch swStudent;
    Switch swOther;
    EditText txtCourseUploadCost;
    EditText txtCourseUploadDetail;
    List<String> mDistrict;
    List<String> mDays;
    List<String> mIntervals;
    Context context;
    Button btnCourseUpload;
    private static final  List<String> _selectedDistrict = new ArrayList<>();
    private static final  List<String> _selectedDays= new ArrayList<>();
    private static final List<String> _selectedIntervals= new ArrayList<>();
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_upload);
        mDistrict = Arrays.asList(getResources().getStringArray(R.array.district));
        mDays = Arrays.asList(getResources().getStringArray(R.array.weekDays));
        mIntervals = Arrays.asList(getResources().getStringArray(R.array.timeInterval));
        context = this;
        init();
    }
    void init(){
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
        btnCourseUpload = findViewById(R.id.btnCourseUpload);
        spinnerDistrict.setItems(mDistrict,getString(R.string.app_name),this);
        spinnerDays.setItems(mDays,getString(R.string.app_name),this);
        spinnerIntervals.setItems(mIntervals,getString(R.string.app_name),this);
        btnCourseUpload.setOnClickListener(v->{

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
}
