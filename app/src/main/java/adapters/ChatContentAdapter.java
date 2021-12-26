package adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import adapters.util.ImageDownloaderTask;
import controllers.ChatPage;
import models.ChatContent;
import utils.AsyncResponse;
import utils.TransactionTypes;

public class ChatContentAdapter extends RecyclerView.Adapter<ChatContentAdapter.ChatsViewHolder> implements AsyncResponse {
    private static ArrayList<ChatContent> mChatContents;
    private TrainerCourseAdapter.OnItemClickListener mListener;
    @Override
    public <T> void processFinish(T result) {
    }
    private Object mGetClassType(Object object){
        return object.getClass();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(TrainerCourseAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
    public static class ChatsViewHolder extends RecyclerView.ViewHolder {
        public AppCompatImageView mChatContentReceiverImage;
        public TextView mChatContentReceiverName;
        public TextView mChatContentReceiverLastMessage;
        public AppCompatImageView mChatContentSendMessage;
        public TextView txtMessageDate;
        private ArrayList<ChatContent> mChatContent;
        public ChatsViewHolder(View itemView) {
            super(itemView);
            mChatContentReceiverImage = itemView.findViewById(R.id.receiverChatContentImage);
            mChatContentReceiverName = itemView.findViewById(R.id.txtChatContentReceiverName);
            mChatContentReceiverLastMessage = itemView.findViewById(R.id.txtChatContentReceiverLastMessage);
            mChatContentSendMessage = itemView.findViewById(R.id.btnChatContentSendMessage);
            txtMessageDate = itemView.findViewById(R.id.txtMessageDate);
        }
        public ChatsViewHolder(View itemView,int viewType){
            super(itemView);
            if(viewType == TransactionTypes.USER_CHAT_CONTENT_TYPE_SEARCH) {
                mChatContentReceiverImage = itemView.findViewById(R.id.receiverChatContentImage);
                mChatContentReceiverName = itemView.findViewById(R.id.txtChatContentReceiverName);
                mChatContentReceiverLastMessage = itemView.findViewById(R.id.txtChatContentReceiverLastMessage);
                mChatContentSendMessage = itemView.findViewById(R.id.btnChatContentSendMessage);
                txtMessageDate = itemView.findViewById(R.id.txtMessageDate);
            }
        }
        public void SetData(ChatContent contents){
            new ImageDownloaderTask(mChatContentReceiverImage).execute(contents.getUserProfileImageUrl());
            mChatContentReceiverName.setText(String.valueOf(contents.getUserName()));
            mChatContentReceiverLastMessage.setText(String.valueOf(contents.getLastMessage()));
            String _time = DetectSendTime(contents.getLastMessageTime());
            txtMessageDate.setText(_time);
            mChatContentSendMessage.setOnClickListener(v -> {
                v.getContext().startActivity(new Intent(v.getContext(), ChatPage.class)
                        .putExtra("receiverId", contents.getReceiverId())
                        .putExtra("lastMessage", contents.getLastMessage())
                        .putExtra("lastMessageId", contents.getLastMessageId())
                        .putExtra("receiverImage", contents.getUserProfileImageUrl())
                        .putExtra("receiverName", contents.getUserName())
                        .putExtra("startChatDate", contents.getStartChatDate()));
            });
        }
    }
    public ChatContentAdapter(ArrayList<ChatContent> exampleList) {
        mChatContents = exampleList;
    }
    @Override
    public ChatContentAdapter.@NotNull ChatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TransactionTypes.USER_CHAT_CONTENT_TYPE_SEARCH){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_items, parent, false);
            return new ChatsViewHolder(v,viewType);
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_items, parent, false);
            return new ChatsViewHolder(v);
        }
    }
    @Override
    public void onBindViewHolder(ChatContentAdapter.@NotNull ChatsViewHolder holder, int position) {
        ChatContent mCurrentItem = mChatContents.get(position);
        if(mCurrentItem.isUserSearch() == 0) {
            new ImageDownloaderTask(holder.mChatContentReceiverImage).execute(mCurrentItem.getUserProfileImageUrl());
            holder.mChatContentReceiverName.setText(String.valueOf(mCurrentItem.getUserName()));
            holder.mChatContentReceiverLastMessage.setText(String.valueOf(mCurrentItem.getLastMessage()));
            String _time = DetectSendTime(mCurrentItem.getLastMessageTime());
            holder.txtMessageDate.setText(_time);
            holder.mChatContentSendMessage.setOnClickListener(v -> {
                v.getContext().startActivity(new Intent(v.getContext(), ChatPage.class)
                        .putExtra("receiverId", mCurrentItem.getReceiverId())
                        .putExtra("lastMessage", mCurrentItem.getLastMessage())
                        .putExtra("lastMessageId", mCurrentItem.getLastMessageId())
                        .putExtra("receiverImage", mCurrentItem.getUserProfileImageUrl())
                        .putExtra("receiverName", mCurrentItem.getUserName())
                        .putExtra("startChatDate", mCurrentItem.getStartChatDate()));
            });
        }else {
            ((ChatContentAdapter.ChatsViewHolder) holder).SetData(mChatContents.get(position));
        }
    }
    @Override
    public int getItemCount() {
        return mChatContents.size();
    }

    @Override
    public int getItemViewType(int position) {
        System.out.println(mChatContents.get(position).isUserSearch());
        if(mChatContents.get(position).isUserSearch() == 1) {
            return TransactionTypes.USER_CHAT_CONTENT_TYPE_SEARCH;
        }
        else
            return TransactionTypes.USER_CHAT_CONTENT_ALREADY_EXIST;
    }

    private static String DetectSendTime(String lastMessageTime){
        String _temp = lastMessageTime.substring(lastMessageTime.indexOf("T")+1,lastMessageTime.length()-1);
        String[] _arr = _temp.split(":");
        return _arr[0]+":"+_arr[1]+"";
    }
}
