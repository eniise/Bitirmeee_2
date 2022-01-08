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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import adapters.ChatContentAdapter;
import models.chat.Chat;
import models.trainer.TrainerCourse;
import utils.AsyncResponse;
import utils.components.MyAlertDialog;
import utils.server.ServerPOST;
import utils.user.StaticData;
import utils.extras.TransactionTypes;
import utils.extras.URLs;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PopupWindow implements AsyncResponse {
    public View mView;
    public Context mContext;
    public TrainerCourse mCourse;
    private RoundedImageView _ProfileImage;
    private TextView txtTrainerName;
    private TextView txtDetail;
    private AppCompatImageView sendMessage;
    private EditText txtSendMessage;
    private TextView swOnline;
    private TextView swOther;
    private TextView swHome;
    private TextView swStudent;
    private TextView txtIntervals;
    private TextView txtDays;
    private TextView txtCity;
    private TextView popupCost;
    public android.widget.PopupWindow mPopupWindow;
    public RecyclerView mRecyclerView;
    public ChatContentAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public ProgressBar chatContent;
    public View searchingView;
    public View view;
    private EditText txtSearchUser;
    private int userSee;
    private String layout;
    public PopupWindow(View mView, Context mContext, TrainerCourse mCourse,int userSee,String layout) {
        this.mView = mView;
        this.mContext = mContext;
        this.mCourse = mCourse;
        this.userSee = userSee;
        this.layout = layout;
    }
    public void init(View popupView){
        if(layout.equals(TransactionTypes.LAYOUT_MESSAGE_SEND)) {
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
            if (userSee == TransactionTypes.USER_SEE_CHAT) {
                txtSendMessage.setVisibility(View.GONE);
                sendMessage.setVisibility(View.GONE);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onButtonShowPopupWindowClick() {
        if(layout.equals(TransactionTypes.LAYOUT_MESSAGE_SEND)) {
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
            popupCost.setText(String.valueOf("Cost: " + mCourse.getmCost()));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SetData(android.widget.PopupWindow popupWindow){
        new ImageDownloaderTask(_ProfileImage).execute(URLs.GetPhoto(mCourse.getmUserId()));
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
                    StaticData.getUserData().getUserId(), mCourse.getmId());
            sendMessage.execute(URLs.SendMessageWithCourse(),new Gson().toJson(_chat));
        });
    }
    private void DetectAndSetPlan(String types){
        if(types.length()>0){
            if(types.indexOf(",")>0){
                String[] split = types.split(",");
                for (String i:split) {
                    String _tmp = i.replace(" ","");
                    if (_tmp.equals("TrainerHome")){
                        swHome.setVisibility(View.VISIBLE);
                    }else if(_tmp.equals("Online")){
                        swOnline.setVisibility(View.VISIBLE);
                    }else if(_tmp.equals("Otherplace")){
                        swOther.setVisibility(View.VISIBLE);
                    }else if(_tmp.equals("Studenthome")){
                        swStudent.setVisibility(View.VISIBLE);
                    }
                }
            }else {
                switch (types) {
                    case "Trainer Home":
                        swHome.setVisibility(View.VISIBLE);
                        break;
                    case "Online":
                        swOnline.setVisibility(View.VISIBLE);
                        break;
                    case "Other place":
                        swOther.setVisibility(View.VISIBLE);
                        break;
                    case "Student home":
                        swStudent.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }
    private void DetectAndSetIntervals(String intervals){
        if(intervals.length()>0){
            txtIntervals.setText(String.valueOf("The instructor gives courses at the times : "+intervals));
        }
    }
    private void DetectAndSetDistict(String district){
        if(district.length()>0){
            txtCity.setText(String.valueOf("The instructor gives courses at the district : "+district));
        }
    }
    private void DetectAndSetDays(String days){
        if(days.length()>0){
            txtDays.setText(String.valueOf("The instructor gives courses at the days : "+days));
        }
    }

    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == Boolean.class)
            if ((boolean) result){
                new MyAlertDialog(mContext,"Successfully","Message has been send.",R.drawable.ic_message_info)
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
