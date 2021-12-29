package adapters.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;

import java.util.ArrayList;

import adapters.ChatContentAdapter;
import models.ChatContent;
import models.TrainerCourse;
import utils.AsyncResponse;
import utils.ServerGET;
import utils.StaticData;
import utils.TransactionTypes;
import utils.URLs;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MessageShare extends PopupWindow implements AsyncResponse, TextWatcher {
    private EditText txtSearchUser;
    public MessageShare(View mView, Context mContext, TrainerCourse mCourse, int userSee, String layout) {
        super(mView, mContext, mCourse, userSee, layout);
    }

    @Override
    public <T> void processFinish(T result) {
        ArrayList<Object> _tmp = (ArrayList<Object>) result;
        ArrayList<ChatContent> _lst;
        if((int)_tmp.get(0) == TransactionTypes.USER_CHAT_CONTENT_TYPE_SEARCH){
            _lst = (ArrayList<ChatContent>) _tmp.get(1);
            searchingView.setVisibility(View.GONE);
            chatContent.setVisibility(View.GONE);
        }else{
            _lst = (ArrayList<ChatContent>) _tmp.get(1);
            searchingView.setVisibility(View.GONE);
            chatContent.setVisibility(View.GONE);
        }
        mRecyclerView = view.findViewById(R.id.chatContentRecyler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mAdapter = new ChatContentAdapter(_lst,TransactionTypes.LAYOUT_MESSAGE_SHARE,mCourse,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        chatContent.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onButtonShowPopupWindowClick() {
        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.chat_content, null);
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
    }

    @Override
    public void init(View popupView) {
        view = popupView;
        chatContent = popupView.findViewById(R.id.chatContent);
        txtSearchUser = popupView.findViewById(R.id.txtSearchUser);
        txtSearchUser.addTextChangedListener(this);
        searchingView = popupView.findViewById(R.id.searchingView);
        ServerGET getMyContent = new ServerGET(TransactionTypes.doGetMyChatContent);
        getMyContent.delegate = this;
        getMyContent.execute(URLs.GetChatContent(StaticData.getUserData().getUserId()));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length()>0) {
            searchingView.setVisibility(View.VISIBLE);
            chatContent.setVisibility(View.VISIBLE);
            ServerGET getMyContent = new ServerGET(TransactionTypes.doUserSearch);
            getMyContent.delegate = this;
            getMyContent.execute(URLs.SearchUser(s.toString(),StaticData.getUserData().getUserId()));
        }else {
            searchingView.setVisibility(View.GONE);
            chatContent.setVisibility(View.VISIBLE);
            ServerGET getMyContent = new ServerGET(TransactionTypes.doGetMyChatContent);
            getMyContent.delegate = this;
            getMyContent.execute(URLs.GetChatContent(StaticData.getUserData().getUserId()));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
