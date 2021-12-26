package adapters.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.enise.bitirme_2.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import models.Chat;
import models.TrainerCourse;
import utils.AsyncResponse;
import utils.MyAlertDialog;
import utils.ServerPOST;
import utils.StaticData;
import utils.TransactionTypes;
import utils.URLs;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PopupWindow implements AsyncResponse {
    private View mView;
    private Context mContext;
    private TrainerCourse mCourse;
    private RoundedImageView _ProfileImage;
    private  TextView txtTrainerName;
    private  TextView txtDetail;
    private  AppCompatImageView sendMessage;
    private  EditText txtSendMessage;
    private TextView swOnline;
    private TextView swOther;
    private TextView swHome;
    private TextView swStudent;
    private TextView txtIntervals;
    private TextView txtDays;
    private TextView txtCity;
    private TextView popupCost;
    private android.widget.PopupWindow mPopupWindow;
    public PopupWindow(View mView, Context mContext, TrainerCourse mCourse) {
        this.mView = mView;
        this.mContext = mContext;
        this.mCourse = mCourse;
    }
    private void init(View popupView){
        _ProfileImage = popupView.findViewById(R.id.popupTrainerImage);
        txtTrainerName = popupView.findViewById(R.id.popupTrainerName);
        txtDetail = popupView.findViewById(R.id.popupTrainerDetail);
        sendMessage = popupView.findViewById(R.id.popupMessageSend);
        txtSendMessage = popupView.findViewById(R.id.txtPopupMessage);
        popupCost = popupView.findViewById(R.id.popupCost);
        swOnline = popupView.findViewById(R.id.swOnline);
        swOther = popupView.findViewById(R.id.swOther);
        swHome = popupView.findViewById(R.id.swHome);
        swStudent = popupView.findViewById(R.id.swStudent);
        txtIntervals = popupView.findViewById(R.id.txtIntervals);
        txtDays = popupView.findViewById(R.id.txtDays);
        txtCity = popupView.findViewById(R.id.txtCity);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onButtonShowPopupWindowClick() {
        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.send_course_message_popup, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final android.widget.PopupWindow popupWindow = new android.widget.PopupWindow(popupView, width, height, focusable);
        popupWindow.setAnimationStyle(R.style.popupWindow);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
        mPopupWindow = popupWindow;
        init(popupView);
        SetData(popupWindow);
        DetectAndSetIntervals(mCourse.getmTimeIntervals());
        DetectAndSetPlan(mCourse.getmTypes());
        DetectAndSetDistict(mCourse.getmDistrict());
        DetectAndSetDays(mCourse.getmDays());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SetData(android.widget.PopupWindow popupWindow){
        new ImageDownloaderTask(_ProfileImage).execute(mCourse.getmTrainerImage());
        txtTrainerName.setText(mCourse.getmName());
        txtDetail.setText(mCourse.getmDetail());
        swStudent.setVisibility(View.GONE);
        swOnline.setVisibility(View.GONE);
        swHome.setVisibility(View.GONE);
        swOther.setVisibility(View.GONE);
        sendMessage.setOnClickListener(v->{
            ServerPOST sendMessage = new ServerPOST(TransactionTypes.doUserSendMessageWithCourse);
            sendMessage.delegate = this;
            Chat _chat = new Chat(0,
                    mCourse.getmUserId(),
                    StaticData.getUserData().getUserId(),
                    2,txtSendMessage.getText().toString(),
                    getDateTime(),
                    StaticData.getUserData().getUserId());
            sendMessage.execute(URLs.SendMessageWithCourse(),new Gson().toJson(_chat));
        });
    }
    private void DetectAndSetPlan(String types){
        if(types.length()>0){
            if(types.indexOf(",")>0){
                String[] _ = types.split(",");
                for (String i:_) {
                    if (i.toLowerCase().equals("home")){
                        swHome.setVisibility(View.VISIBLE);
                    }else if(i.toLowerCase().equals("online")){
                        swOnline.setVisibility(View.VISIBLE);
                    }else if(i.toLowerCase().equals("other")){
                        swOther.setVisibility(View.VISIBLE);
                    }else if(i.toLowerCase().equals("student")){
                        swStudent.setVisibility(View.VISIBLE);
                    }
                }
            }else {
                if (types.toLowerCase().equals("home")){
                    swHome.setVisibility(View.VISIBLE);
                }else if(types.toLowerCase().equals("online")){
                    swOnline.setVisibility(View.VISIBLE);
                }else if(types.toLowerCase().equals("other")){
                    swOther.setVisibility(View.VISIBLE);
                }else if(types.toLowerCase().equals("student")){
                    swStudent.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void DetectAndSetIntervals(String intervals){
        if(intervals.length()>0){
            System.out.println(mCourse.getmTimeIntervals()+" "+mCourse.getmTypes());
            txtIntervals.setText(String.valueOf("The instructor gives courses at the times : "+intervals));
        }
    }
    private void DetectAndSetDistict(String district){
        if(district.length()>0){
            System.out.println(mCourse.getmTimeIntervals()+" "+mCourse.getmTypes());
            txtCity.setText(String.valueOf("The instructor gives courses at the district : "+district+"bla bla bla bla bla bla"));
        }
    }
    private void DetectAndSetDays(String days){
        if(days.length()>0){
            System.out.println(mCourse.getmTimeIntervals()+" "+mCourse.getmTypes());
            txtDays.setText(String.valueOf("The instructor gives courses at the days : "+days));
        }
    }

    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == Boolean.class)
            if ((boolean) result){
                new MyAlertDialog(mContext,"Successfuly","Message has been send.",R.drawable.ic_message_info)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay, ((dialog, which) -> {
                            this.mPopupWindow.dismiss();
                        }))
                        .show();
            }else {
                new MyAlertDialog(mContext,"Error","Please try again later.",R.drawable.ic_message_info)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay, ((dialog, which) -> {
                            //error
                        }))
                        .show();
            }
        else {
            new MyAlertDialog(mContext,"Error","Please try again later.",R.drawable.ic_message_info)
                    .ShowMessage()
                    .setPositiveButton(R.string.okay, ((dialog, which) -> {
                        //error
                    }))
                    .show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateTime(){
        java.util.Date date = new java.util.Date();
        LocalDateTime localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        DateTimeFormatter _formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return _formater.format(localDate);
    }
}
