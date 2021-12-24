package adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import adapters.util.ImageDownloaderTask;
import controllers.ChatPage;
import models.ChatContent;
import utils.AsyncResponse;

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
        public ImageView mChatContentReceiverImage;
        public TextView mChatContentReceiverName;
        public TextView mChatContentReceiverLastMessage;
        public ImageButton mChatContentSendMessage;
        public ChatsViewHolder(View itemView) {
            super(itemView);
            mChatContentReceiverImage = itemView.findViewById(R.id.receiverChatContentImage);
            mChatContentReceiverName = itemView.findViewById(R.id.txtChatContentReceiverName);
            mChatContentReceiverLastMessage = itemView.findViewById(R.id.txtChatContentReceiverLastMessage);
            mChatContentSendMessage = itemView.findViewById(R.id.btnChatContentSendMessage);
        }
    }
    public ChatContentAdapter(ArrayList<ChatContent> exampleList) {
        mChatContents = exampleList;
    }
    @Override
    public ChatContentAdapter.@NotNull ChatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_items, parent, false);
        return new ChatsViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ChatContentAdapter.@NotNull ChatsViewHolder holder, int position) {
        ChatContent mCurrentItem = mChatContents.get(position);
        new ImageDownloaderTask(holder.mChatContentReceiverImage).execute(mCurrentItem.getUserProfileImageUrl());
        holder.mChatContentReceiverName.setText(String.valueOf(mCurrentItem.getUserName()));
        holder.mChatContentReceiverLastMessage.setText(String.valueOf(mCurrentItem.getLastMessage()));
        holder.mChatContentSendMessage.setOnClickListener(v -> {
            System.out.print("RECEİVER ID= "+mCurrentItem.getReceiverId());
            v.getContext().startActivity(new Intent(v.getContext(),ChatPage.class)
                    .putExtra("receiverId",mCurrentItem.getReceiverId())
                    .putExtra("lastMessage",mCurrentItem.getLastMessage())
                    .putExtra("lastMessageId",mCurrentItem.getLastMessageId())
                    .putExtra("receiverImage",mCurrentItem.getUserProfileImageUrl())
                    .putExtra("receiverName",mCurrentItem.getUserName()));
        });
    }
    @Override
    public int getItemCount() {
        return mChatContents.size();
    }

}
