package adapters.message;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import models.Chat;
import utils.AsyncResponse;
import utils.ServerPOST;
import utils.TransactionTypes;
import utils.URLs;

public class SendMessage implements AsyncResponse {
    private int mReceiverId;
    private int mSenderId;
    private Chat mChatData;
    private ServerPOST sendMessage = null;
    private RecyclerView.Adapter mAdapter;
    public SendMessage(int receiverId, int senderId, Chat chat,RecyclerView.Adapter adapter){
        this.mReceiverId = receiverId;
        this.mSenderId = senderId;
        this.mChatData = chat;
        this.mAdapter = adapter;
        sendMessage = new ServerPOST(TransactionTypes.doSendMessage);
        sendMessage.delegate = this;
    }
    public void SendMyMessage(){
        sendMessage.execute(URLs.SendMessage(),new Gson().toJson(mChatData));
    }
    @Override
    public <T> void processFinish(T result) {
        //if send message async thread finished
        mAdapter.notifyDataSetChanged();
    }
}
