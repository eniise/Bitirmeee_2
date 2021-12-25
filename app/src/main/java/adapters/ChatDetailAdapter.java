package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;

import adapters.util.ImageDownloaderTask;
import models.ChatDetail;
import utils.TransactionTypes;

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
        }else if(viewType == VIEW_TYPE_RECEIVER) {
            return new ReceiverMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_detail_receiver_items, parent, false));
        }else if(viewType == TransactionTypes.MESSAGE_TYPE_SENDER_WITH_COURSE){
            return new SenderCourseMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_detail_sender_course_item,parent,false));
        }else if(viewType == TransactionTypes.MESSAGE_TYPE_RECEIVER_WITH_COURSE){
            return new ReceiverCourseMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_detail_sender_course_item,parent,false));
        }else {
            return null;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            if(mChatDetail.get(position).getMessageType() == TransactionTypes.MESSAGE_TYPE_STRING) {
                ((SentMessageViewHolder) holder).SetData(mChatDetail.get(position));
            }else {
                ((SenderCourseMessageViewHolder) holder).SetData(mChatDetail.get(position));
            }
        }else {
            if(mChatDetail.get(position).getMessageType() == TransactionTypes.MESSAGE_TYPE_STRING) {
                ((ReceiverMessageViewHolder) holder).SetData(mChatDetail.get(position));
            }else{
                ((ReceiverCourseMessageViewHolder) holder).SetData(mChatDetail.get(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChatDetail.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mChatDetail.get(position).getMessageFromId() == mSenderId){
            if(mChatDetail.get(position).getMessageType() == TransactionTypes.MESSAGE_TYPE_STRING)
                return VIEW_TYPE_SENT;
            else if(mChatDetail.get(position).getMessageType() == TransactionTypes.MESSAGE_TYPE_COURSE)
                return TransactionTypes.MESSAGE_TYPE_SENDER_WITH_COURSE;
            else
                return VIEW_TYPE_SENT;
        }else{
            if(mChatDetail.get(position).getMessageType() == TransactionTypes.MESSAGE_TYPE_STRING)
                return VIEW_TYPE_RECEIVER;
            else if(mChatDetail.get(position).getMessageType() == TransactionTypes.MESSAGE_TYPE_COURSE)
                return TransactionTypes.MESSAGE_TYPE_RECEIVER_WITH_COURSE;
            else
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
        @RequiresApi(api = Build.VERSION_CODES.O)
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
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void SetData(ChatDetail chat){
            txtChatReceiverMessage.setText(chat.getMessage());
            txtChatReceiverDateTime.setText(chat.getMessageDate());
            imageReceiver.setImageBitmap(receiverProfileImage);
        }
    }
    static class ReceiverCourseMessageViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private RoundedImageView roundedImageView;
        private Button btnReceiverCourseSend;
        private TextView txtReceiverSendDate;
        ReceiverCourseMessageViewHolder(final View itemView){
            super(itemView);
            this.itemView = itemView;
            roundedImageView = itemView.findViewById(R.id.imageChatDetailCourseProfile);
            btnReceiverCourseSend = itemView.findViewById(R.id.btntReceiverCourseMessage);
            txtReceiverSendDate = itemView.findViewById(R.id.receiverCourseDateTime);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void SetData(ChatDetail chat){
            roundedImageView.setImageBitmap(receiverProfileImage);
            btnReceiverCourseSend.setText(chat.getMessage());
            txtReceiverSendDate.setText(chat.getMessageDate());
        }
    }
    static class SenderCourseMessageViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private RoundedImageView roundedImageView;
        private Button btnSenderCourseSend;
        private TextView txtSenderSendDate;
        SenderCourseMessageViewHolder(final View itemView){
            super(itemView);
            this.itemView = itemView;
            roundedImageView = itemView.findViewById(R.id.imageChatDetailCourseProfile);
            btnSenderCourseSend = itemView.findViewById(R.id.btntReceiverCourseMessage);
            txtSenderSendDate = itemView.findViewById(R.id.receiverCourseDateTime);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void SetData(ChatDetail chat){
            roundedImageView.setImageBitmap(receiverProfileImage);
            btnSenderCourseSend.setText(chat.getMessage());
            txtSenderSendDate.setText(chat.getMessageDate());
            btnSenderCourseSend.setOnClickListener(v-> {
                //do click course
            });
        }
    }
}