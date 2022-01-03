package controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import adapters.ChatDetailAdapter;
import adapters.message.SendMessage;
import models.chat.Chat;
import models.chat.ChatDetail;
import models.chat.CurrentChatLastMessageInfo;
import utils.AsyncResponse;
import utils.server.ServerGET;
import utils.user.StaticData;
import utils.extras.TransactionTypes;
import utils.extras.URLs;

public class ChatPage extends AppCompatActivity implements AsyncResponse, View.OnClickListener {
    private ServerGET getLastMessage;
    private String lastMessage;
    private int receiverId;
    private int lastMessageId;
    private String receiverUserName;
    private String mStartChatDate;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    public Bitmap receiverUserImageBmp;
    private boolean mPageFirstOpen;
    private ProgressBar mProgressBar;
    private TextView mReceiverName;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ChatDetail> chatDetails;
    private AppCompatImageView btnMessageSend;
    private TextView inputMessage;
    private boolean isActivityActive = false;
    private AppCompatImageView imgBack;
    private  String receiverUserImage;
    private AppCompatImageView imgChatInfo;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageFirstOpen = true;
        setContentView(R.layout.chat_detail_content);
        init();
        getLastMessage  = new ServerGET(TransactionTypes.doGetChatDetail);
        getLastMessage.delegate = this;
        if(mPageFirstOpen) {
            getLastMessage.execute(URLs.GetChatDetail(StaticData.getUserData().getUserId(), receiverId));
            mPageFirstOpen = false;
        }
        isActivityActive = true;
    }
    @Override
    public <T> void processFinish(T result) {
        Object result_type = result.getClass();
        if(result_type == CurrentChatLastMessageInfo.class && !mPageFirstOpen && isActivityActive) {
            CurrentChatLastMessageInfo _lst = (CurrentChatLastMessageInfo) result;
            if ((_lst.getMessageFromId() != StaticData.getUserData().getUserId()) && (_lst.getLastMessageId() != lastMessageId)) {
                //new message detected on listener
                ChatDetail detailTemp = new ChatDetail(_lst.getLastMessage(),_lst.getMessageTime(),receiverId,_lst.getTransaction());
                chatDetails.add(detailTemp);
                mAdapter.notifyDataSetChanged();
                this.lastMessageId = _lst.getLastMessageId();
                //return new listener..
                getLastMessage = new ServerGET(TransactionTypes.doGetMyCurrentLastMessage);
                getLastMessage.delegate = this;
                getLastMessage.execute(URLs.GetCurrentLastMessage(StaticData.getUserData().getUserId(),receiverId));
            }else {
                //will not a new message??? yeah, create async task..
                getLastMessage = new ServerGET(TransactionTypes.doGetMyCurrentLastMessage);
                getLastMessage.delegate = this;
                getLastMessage.execute(URLs.GetCurrentLastMessage(StaticData.getUserData().getUserId(),receiverId));
            }
        }else if(result_type == ArrayList.class && isActivityActive) {
            //if page first opened? init items and run listener.. infinite...
            chatDetails = (ArrayList<ChatDetail>) result;
            mRecyclerView = findViewById(R.id.chatDetailContent);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new ChatDetailAdapter(chatDetails,receiverUserImageBmp,StaticData.getUserData().getUserId(),this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            getLastMessage = new ServerGET(TransactionTypes.doGetMyCurrentLastMessage);
            getLastMessage.delegate = this;
            getLastMessage.execute(URLs.GetCurrentLastMessage(StaticData.getUserData().getUserId(),receiverId));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.messageSend){
            if(inputMessage.getText().toString().length() != 0) {
                String message = inputMessage.getText().toString();
                inputMessage.setText("");
                Chat chatData = new Chat(
                        0,
                        receiverId,
                        StaticData.getUserData().getUserId(),
                        1,
                        message,
                        getDateTime(),
                        StaticData.getUserData().getUserId(), 0);
                new SendMessage(receiverId,
                        StaticData.getUserData().getUserId(),
                        chatData,
                        mAdapter).SendMyMessage();
                ChatDetail _tmpDetail = new ChatDetail(
                        message
                        ,getDateTime()
                        ,StaticData.getUserData().getUserId()
                        ,1
                );
                chatDetails.add(_tmpDetail);
                mAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(v.getContext(),"Please type a message.",Toast.LENGTH_SHORT).show();
            }
         }
        if(v.getId() == R.id.imageBack){
            onBackPressed();
        }
        if(v.getId() == R.id.imageInfo && isActivityActive){
            startActivity(new Intent(this,MessageInfo.class)
                                    .putExtra("userProfil",receiverUserImage)
                                    .putExtra("userName",receiverUserName)
                                    .putExtra("receiverUserId",receiverId)
                                    .putExtra("startChatDate",mStartChatDate));
        }
    }

    @SuppressLint("StaticFieldLeak")
    class BitmapTask extends AsyncTask<String, Bitmap, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            InputStream input = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }
        protected void onPostExecute(Bitmap result) {
            receiverUserImageBmp=result;
        }
    }
    private void init(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        receiverId = bundle.getInt("receiverId");
        lastMessage = bundle.getString("lastMessage");
        lastMessageId = bundle.getInt("lastMessageId");
        receiverUserName = bundle.getString("receiverName");
        mStartChatDate = bundle.getString("startChatDate");
        receiverUserImage = bundle.getString("receiverImage");
        new BitmapTask().execute(receiverUserImage);
        mProgressBar = findViewById(R.id.progressBar);
        mReceiverName = findViewById(R.id.txtReceiverName);
        mReceiverName.setText(receiverUserName);
        btnMessageSend = findViewById(R.id.messageSend);
        btnMessageSend.setOnClickListener(this);
        inputMessage = findViewById(R.id.inputMessage);
        imgBack = findViewById(R.id.imageBack);
        imgBack.setOnClickListener(this);
        imgChatInfo = findViewById(R.id.imageInfo);
        imgChatInfo.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateTime(){
        java.util.Date date = new java.util.Date();
        LocalDateTime localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        DateTimeFormatter _formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return _formater.format(localDate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityActive = true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        isActivityActive = false;
    }
}
