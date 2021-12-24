package controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import adapters.ChatDetailAdapter;
import adapters.message.SendMessage;
import models.Chat;
import models.ChatDetail;
import models.CurrentChatLastMessageInfo;
import utils.AsyncResponse;
import utils.ServerGET;
import utils.ServerPOST;
import utils.StaticData;
import utils.TransactionTypes;
import utils.URLs;

public class ChatPage extends AppCompatActivity implements AsyncResponse {
    private ServerGET getLastMessage;
    private String lastMessage;
    private int receiverId;
    private int lastMessageId;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    public Bitmap receiverUserImageBmp;
    private boolean mPageFirstOpen;
    private ProgressBar mProgressBar;
    private TextView mReceiverName;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ChatDetail> chatDetails;
    private AppCompatImageView btnMessageSend;
    private TextView inputMessage;
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
        btnMessageSend.setOnClickListener(v->{
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
                        StaticData.getUserData().getUserId());
                new SendMessage(receiverId,
                        StaticData.getUserData().getUserId(),
                        chatData,
                        mAdapter).SendMyMessage();
                ChatDetail _tmpDetail = new ChatDetail(
                        message
                        ,getDateTime()
                        ,StaticData.getUserData().getUserId()
                );
                chatDetails.add(_tmpDetail);
            }else {
                Toast.makeText(v.getContext(),"Please type a message.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public <T> void processFinish(T result) {
        Object result_type = result.getClass();
        if(result_type == CurrentChatLastMessageInfo.class && !mPageFirstOpen) {
            CurrentChatLastMessageInfo _lst = (CurrentChatLastMessageInfo) result;
            System.out.println("message from id : "+_lst.getMessageFromId()+" user id = "+StaticData.getUserData().getUserId()+" last message id = "+_lst.getLastMessageId()+" last message current id = "+lastMessageId);
            if ((_lst.getMessageFromId() != StaticData.getUserData().getUserId()) && (_lst.getLastMessageId() != lastMessageId)) {
                //new message detected on listener
                ChatDetail detailTemp = new ChatDetail(_lst.getLastMessage(),_lst.getMessageTime(),receiverId);
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
        }else if(result_type == ArrayList.class) {
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
        String receiverUserImage = bundle.getString("receiverImage");
        new BitmapTask().execute(receiverUserImage);
        mProgressBar = findViewById(R.id.progressBar);
        mReceiverName = findViewById(R.id.txtReceiverName);
        mReceiverName.setText(bundle.getString("receiverName"));
        btnMessageSend = findViewById(R.id.messageSend);
        inputMessage = findViewById(R.id.inputMessage);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateTime(){
        java.util.Date date = new java.util.Date();
        LocalDateTime localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        DateTimeFormatter _formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return _formater.format(localDate);
    }
}
