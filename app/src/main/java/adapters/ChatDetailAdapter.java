package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import adapters.util.ImageDownloaderTask;
import models.ChatDetail;

public class ChatDetailAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final ArrayList<ChatDetail> mChatDetail;
    public static Bitmap receiverProfileImage = null;
    private final int mSenderId;
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVER = 2;
    private Context context;
    public ChatDetailAdapter(ArrayList<ChatDetail> mChatDetail, Bitmap receiverProfileImage, int mSenderId, Context context) {
        this.mChatDetail = mChatDetail;
        this.receiverProfileImage = receiverProfileImage;
        this.mSenderId = mSenderId;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT){
            return new SentMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_detail_sender_items, parent, false));
        }else {
            return new ReceiverMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_detail_receiver_items, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).SetData(mChatDetail.get(position));
        }else {
            ((ReceiverMessageViewHolder) holder).SetData(mChatDetail.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mChatDetail.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mChatDetail.get(position).getMessageFromId() == mSenderId){
            return VIEW_TYPE_SENT;
        }else{
            return  VIEW_TYPE_RECEIVER;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private TextView txtChatSender;
        private TextView txtSenderDateTime;
        SentMessageViewHolder(final View itemView){
            super(itemView);
            this.itemView = itemView;
            this.txtChatSender = itemView.findViewById(R.id.txtChatDetailSenderMessage);
            this.txtSenderDateTime = itemView.findViewById(R.id.txtSenderDateTime);
        }
        public void SetData(ChatDetail chat){
            txtChatSender.setText(chat.getMessage());
            txtSenderDateTime.setText(chat.getMessageDate());
        }
    }
    static class ReceiverMessageViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private RoundedImageView imageReceiver;
        private TextView txtChatReceiverMessage;
        private TextView txtChatReceiverDateTime;
        ReceiverMessageViewHolder(final View itemView){
            super(itemView);
            this.itemView = itemView;
            imageReceiver = itemView.findViewById(R.id.imageChatDetailProfile);
            txtChatReceiverMessage = itemView.findViewById(R.id.textReceiverMessage);
            txtChatReceiverDateTime = itemView.findViewById(R.id.receiverDateTime);
        }
        public void SetData(ChatDetail chat){
            txtChatReceiverMessage.setText(chat.getMessage());
            txtChatReceiverDateTime.setText(chat.getMessageDate());
            imageReceiver.setImageBitmap(receiverProfileImage);
        }
    }
}